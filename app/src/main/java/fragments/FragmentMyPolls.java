package fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adapter.MyPollAdapter;
import database.DatabaseEndpoint;
import piv.pivote.PollCreateActivity;
import piv.pivote.R;

/**
 * Created by Daniel on 28.07.2015.
 */
public class FragmentMyPolls extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private String uuid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("InstallSettings", Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString("UUID", "no id available");
        DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
        mAdapter = new MyPollAdapter(databaseEndpoint.getMyPollsTask(uuid));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("onCreateView - MyPolls", "Fragments saved in Manager: " + String.valueOf(getFragmentManager().getFragments().size()));

        final View rootView = inflater.inflate(R.layout.fragment_my_polls, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_create_poll);

        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_create_poll);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
                mAdapter = new MyPollAdapter(databaseEndpoint.getMyPollsTask(uuid));
                recyclerView.setAdapter(mAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        FloatingActionButton fabAdd = (FloatingActionButton) rootView.findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PollCreateActivity.class);
                startActivityForResult(intent, 50);
            }
        });


        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 50 && resultCode == Activity.RESULT_OK)
        {
            /*
            // Reload current fragment (starts onCreateView)
            Fragment frg = null;
            frg = getFragmentManager().findFragmentByTag(FragmentMyPolls.class.getName());
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
            */

            DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
            mAdapter = new MyPollAdapter(databaseEndpoint.getMyPollsTask(uuid));
            recyclerView.setAdapter(mAdapter);
        }

    }

}