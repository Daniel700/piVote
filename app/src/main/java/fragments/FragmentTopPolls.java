package fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import adapter.TopPollsAdapter;
import database.DatabaseEndpoint;
import database.DatabaseLogEndpoint;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.PollBeanApi;
import model.pollBeanApi.model.PollBean;
import piv.pivote.R;
import utils.Settings;

/**
 * This class provides the view for the Top100 List
 * Created by Daniel on 11.08.2015.
 */
public class FragmentTopPolls extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private PollBeanApi pollBeanApi;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static int refreshCounter = 0;
    private static boolean taskStarted = false;
    private static long startTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pollBeanApi = DatabaseEndpoint.instantiateConnection();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_polls, container, false);

            if (Settings.AD_MOB_TEST_ENVIRONMENT)
            {
                //Ad Mob test version
                AdView mAdView = (AdView) rootView.findViewById(R.id.adView_top100_polls);
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("2D18A580DC26C325F086D6FB9D84F765").build();
                mAdView.loadAd(adRequest);
            }
            else
            {
                //Ad Mob productive version
                AdView mAdView = (AdView) rootView.findViewById(R.id.adView_top100_polls);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_top_polls);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_top_polls);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_top_polls);
        swipeRefreshLayout.setOnRefreshListener(this);

        new LoadContentTask(false).execute();

        return rootView;
    }


    /**
     * Refresh Listener for SwipeLayout
     */
    @Override
    public void onRefresh() {
        try{
            new LoadContentTask(true).execute();
        }
        catch (Exception e){
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertTask("FragmentTopPolls - swipeRefresh", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
            e.printStackTrace();
        }
        finally {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * This method will refresh the Poll list for REFRESH_NUMBER times automatically for convenience
     */
    @Deprecated
    public void updateView(){
        // refreshing the Adapter for REFRESH_NUMBER times
        if (refreshCounter != 0 && refreshCounter < Settings.REFRESH_NUMBER)
        {
            new LoadContentTask(false).execute();
            refreshCounter++;
        }
        // if allowed Number of refreshes is reached start a timer to reset this value
        else if (refreshCounter == Settings.REFRESH_NUMBER){
            if (!taskStarted){
                taskStarted = true;
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        refreshCounter = 1;
                        taskStarted = false;
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, Settings.DURATION);
                startTime = System.nanoTime();
            }
            else {
                long elapsedTime = Settings.DURATION - TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
                String elapsedTimeString = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(elapsedTime),
                        TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % TimeUnit.MINUTES.toSeconds(1));
                //Snackbar.make(getActivity().getCurrentFocus(), "Elapsed Time " + elapsedTimeString, Snackbar.LENGTH_LONG).show();
            }
        }
        // increment the counter because of the initial request in onCreate() / (do nothing)
        if (refreshCounter == 0)
        {
            refreshCounter++;
        }
    }


    /**
     * Requests the Top 100 Polls from the remote Database.
     */
    private class LoadContentTask extends AsyncTask<Void, Void, List<Poll>> {

        boolean isSwipe;
        LoadContentTask(boolean isSwipe){
            this.isSwipe = isSwipe;
        }

        @Override
        protected List<Poll> doInBackground(Void... params) {

            List<PollBean> pollBeanList = null;
            ModelTransformer transformer = new ModelTransformer();
            List<Poll> pollList = new ArrayList<>();

            try {
                pollBeanList = pollBeanApi.getTop100PollBeans().execute().getItems();
            }
            catch (IOException e) {
                DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                endpoint.insertTask("FragmentTopPolls - Async", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
                e.printStackTrace();
            }

            if (pollBeanList != null)
                for (PollBean pollBean: pollBeanList) {
                    pollList.add(transformer.transformPollBeanToPoll(pollBean));
                }

            return pollList;
        }

        @Override
        protected void onPostExecute(List<Poll> pollList) {
            super.onPostExecute(pollList);

            if (FragmentTopPolls.this.isVisible()){
                progressBar.setVisibility(View.GONE);
                mAdapter = new TopPollsAdapter(pollList, getContext());
                recyclerView.setAdapter(mAdapter);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isSwipe)
            progressBar.setVisibility(View.VISIBLE);
        }
    }


}
