package fragments;



import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.QuestionListAdapter;
import database.DatabaseEndpoint;
import database.SQLiteAccess;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.model.PollBean;
import piv.pivote.R;
import model.TestData;

/**
 * Created by Daniel on 30.07.2015.
 */
public class FragmentRecentlyVoted extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mAdapter = new QuestionListAdapter(getCurrentPollList(), getActivity().getApplicationContext());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recently_voted, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_recently_voted);

        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_recently_voted);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    mAdapter = new QuestionListAdapter(getCurrentPollList(), getActivity().getApplicationContext());
                    recyclerView.setAdapter(mAdapter);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        return rootView;
    }


    public List<Poll> getCurrentPollList(){

        SQLiteAccess dbAccess = new SQLiteAccess(getActivity().getApplicationContext());
        List<Long> list = dbAccess.getRecentPolls();
        dbAccess.printAllPolls();
        dbAccess.close();

        List<PollBean> pollBeanList = new ArrayList<>();
        DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();

        //ToDo: Check if BatchRequest is of advantage?
        //Request every Poll that is in SQLite DB and remote DB
        for (Long value: list) {
            pollBeanList.add(databaseEndpoint.getPollTask(value));
        }

        ModelTransformer transformer = new ModelTransformer();
        List<Poll> pollList = new ArrayList<>();

        for (PollBean pollBean: pollBeanList) {
            pollList.add(transformer.transformPollBeanToPoll(pollBean));
        }

        return pollList;
    }


}
