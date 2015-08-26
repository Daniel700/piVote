package fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import adapter.TopPollsAdapter;
import database.DatabaseEndpoint;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.model.PollBean;
import piv.pivote.R;
import utils.ToolsUpdateView;

/**
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
                mAdapter = new TopPollsAdapter(getCurrentPollList(),getActivity().getApplicationContext());
                recyclerView.setAdapter(mAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);

        updateView();

        return rootView;
    }


    public void updateView(){
        // refreshing the Adapter for REFRESH_NUMBER times
        if (refreshCounter != 0 && refreshCounter < ToolsUpdateView.REFRESH_NUMBER)
        {
            mAdapter = new TopPollsAdapter(getCurrentPollList(), getActivity().getApplicationContext());
            recyclerView.setAdapter(mAdapter);
            refreshCounter++;
        }
        // if allowed Number of refreshes is reached start a timer to reset this value
        else if (refreshCounter == ToolsUpdateView.REFRESH_NUMBER){
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
                timer.schedule(timerTask, ToolsUpdateView.DURATION);
                startTime = System.nanoTime();
            }
            else {
                long elapsedTime = ToolsUpdateView.DURATION - TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
                String elapsedTimeString = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(elapsedTime),
                        TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % TimeUnit.MINUTES.toSeconds(1));
                Snackbar.make(getActivity().getCurrentFocus(), "Elapsed Time " + elapsedTimeString, Snackbar.LENGTH_LONG).show();
            }
        }
        // increment the counter because of the initial request in onCreate() / (do nothing)
        if (refreshCounter == 0)
        {
            refreshCounter++;
        }
    }



    public List<Poll> getCurrentPollList(){
        DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
        List<PollBean> pollBeanList = databaseEndpoint.getTop100PollsTask();

        ModelTransformer transformer = new ModelTransformer();
        List<Poll> pollList = new ArrayList<>();

        for (PollBean pollBean: pollBeanList) {
            pollList.add(transformer.transformPollBeanToPoll(pollBean));
        }

        return pollList;
    }

}
