package fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import adapter.MyPollAdapter;
import database.DatabaseEndpoint;
import database.DatabaseLogEndpoint;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.PollBeanApi;
import model.pollBeanApi.model.PollBean;
import piv.pivote.PollCreateActivity;
import piv.pivote.R;
import utils.Settings;

/**
 * Created by Daniel on 28.07.2015.
 */
public class FragmentMyPolls extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private String uuid;
    private PollBeanApi pollBeanApi;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static int refreshCounter = 0;
    private static boolean taskStarted = false;
    private static long startTime;

    private static int limitCounter = 0;
    private static boolean limitTaskStarted = false;
    private static long limitStartTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("InstallSettings", Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString("UUID", "no id available - FragmentMyPolls");
        pollBeanApi = DatabaseEndpoint.instantiateConnection();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_polls, container, false);

            if (Settings.AD_MOB_TEST_ENVIRONMENT)
            {
                //Ad Mob test version
                AdView mAdView = (AdView) rootView.findViewById(R.id.adView_my_polls);
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("2D18A580DC26C325F086D6FB9D84F765").build();
                mAdView.loadAd(adRequest);
            }
            else
            {
                //Ad Mob productive version
                AdView mAdView = (AdView) rootView.findViewById(R.id.adView_my_polls);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_my_polls);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_create_poll);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_create_poll);
        swipeRefreshLayout.setOnRefreshListener(this);

        FloatingActionButton fabAdd = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        new LoadContentTask(false).execute();

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50 && resultCode == Activity.RESULT_OK)
        {
            new LoadContentTask(false).execute();
            limitCounter++;
            limitCreationOfPolls();
        }
    }


    /**
     * onClick Listener for FloatingActionButton
     */
    @Override
    public void onClick(View v) {
        //Creation of Polls is limited to 2 per hour
        if (limitCounter < Settings.POLL_CREATION_LIMIT) {
            Context context = v.getContext();
            Intent intent = new Intent(context, PollCreateActivity.class);
            startActivityForResult(intent, 50);
        }
        if (limitCounter == Settings.POLL_CREATION_LIMIT){
            long elapsedTime = 3600000 - TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - limitStartTime);
            String elapsedTimeString = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(elapsedTime),
                    TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % TimeUnit.MINUTES.toSeconds(1));
            Snackbar.make(recyclerView, getString(R.string.createMorePolls) + elapsedTimeString, Snackbar.LENGTH_LONG).show();
        }
    }


    /**
     * Refresh Listener for SwipeLayout
     */
    @Override
    public void onRefresh() {

        try {
            new LoadContentTask(true).execute();
        }
        catch (Exception e) {
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertTask("FragmentMyPolls - swipeRefresh", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
            e.printStackTrace();
        }
        finally {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    /**
     * Limits the number of polls that can be created within 1 hour.
     */
    public void limitCreationOfPolls(){
        if (!limitTaskStarted){
            limitTaskStarted = true;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    limitCounter = 0;
                    limitTaskStarted = false;
                }
            };
            Timer timer = new Timer();
            //3600000 ms == 1 hour
            timer.schedule(timerTask, 3600000);
            limitStartTime = System.nanoTime();
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
     * Requests all own created Polls from the remote Database.
     */
    private class LoadContentTask extends AsyncTask<Void, Void, List<Poll>> {

        boolean isSwipe;
        LoadContentTask(boolean isSwipe){
            this.isSwipe = isSwipe;
        }

        @Override
        protected List<Poll> doInBackground(Void... params) {

            List<Poll> pollList = new ArrayList<>();
            ModelTransformer transformer = new ModelTransformer();

            try {
                List<PollBean> pollBeanList = pollBeanApi.getMyPollBeans(uuid).execute().getItems();

                if (pollBeanList != null)
                    for (PollBean pollBean: pollBeanList) {
                        pollList.add(transformer.transformPollBeanToPoll(pollBean));
                    }

            }
            catch (Exception e) {
                DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                endpoint.insertTask("FragmentMyPolls - Async", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
                e.printStackTrace();
            }

            return pollList;
        }

        @Override
        protected void onPostExecute(List<Poll> pollList) {
            super.onPostExecute(pollList);

            if (FragmentMyPolls.this.isVisible()){
                progressBar.setVisibility(View.GONE);
                mAdapter = new MyPollAdapter(pollList, getContext());
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