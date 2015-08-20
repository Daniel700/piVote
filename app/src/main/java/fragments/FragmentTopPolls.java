package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import adapter.TopPollsAdapter;
import database.DatabaseEndpoint;
import model.TestData;
import piv.pivote.R;

/**
 * Created by Daniel on 11.08.2015.
 */
public class FragmentTopPolls extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
        mAdapter = new TopPollsAdapter(databaseEndpoint.getTop100PollsTask());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_top_polls, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_top_polls);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_top_polls);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
                mAdapter = new TopPollsAdapter(databaseEndpoint.getTop100PollsTask());
                recyclerView.setAdapter(mAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);


        return rootView;
    }

}
