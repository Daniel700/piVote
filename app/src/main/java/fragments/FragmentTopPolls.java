package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
import model.pollBeanApi.model.PollBean;
import piv.pivote.R;
import utils.Settings;

/**
 * This class provides the view for the Top100 View
 * Created by Daniel on 11.08.2015.
 */
public class FragmentTopPolls extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;

    private static int refreshCounter = 0;
    private static boolean taskStarted = false;
    private static long startTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new TopPollsAdapter(getCurrentPollList(), getActivity().getApplicationContext());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_top_polls, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_top_polls);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_top_polls);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    mAdapter = new TopPollsAdapter(getCurrentPollList(),getActivity().getApplicationContext());
                    recyclerView.setAdapter(mAdapter);
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
        });


        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);

        updateView();


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


        return rootView;
    }

    /**
     * This method will refresh the Poll list for REFRESH_NUMBER times automatically for convenience
     */
    public void updateView(){
        // refreshing the Adapter for REFRESH_NUMBER times
        if (refreshCounter != 0 && refreshCounter < Settings.REFRESH_NUMBER)
        {
            mAdapter = new TopPollsAdapter(getCurrentPollList(), getActivity().getApplicationContext());
            recyclerView.setAdapter(mAdapter);
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
     * @return Poll list for the Adapter
     */
    public List<Poll> getCurrentPollList(){
        DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
        List<PollBean> pollBeanList = databaseEndpoint.getTop100PollsTask();

        ModelTransformer transformer = new ModelTransformer();
        List<Poll> pollList = new ArrayList<>();

        if (pollBeanList != null)
        for (PollBean pollBean: pollBeanList) {
            pollList.add(transformer.transformPollBeanToPoll(pollBean));
        }

        return pollList;
    }

}
