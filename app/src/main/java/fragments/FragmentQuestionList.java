package fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.QuestionListAdapter;
import database.DatabaseEndpoint;
import model.ModelTransformer;
import model.Poll;
import model.TestData;
import model.pollBeanApi.model.PollBean;
import piv.pivote.DialogFilter;
import piv.pivote.R;

/**
 * Created by Daniel on 28.07.2015.
 */
public class FragmentQuestionList extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ToDo: Request only Polls from Database on which the user hasn't yet voted / Alternatively color them grey in the Question-List
        mAdapter = new QuestionListAdapter(getCurrentPollList(), getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        final View rootView = inflater.inflate(R.layout.fragment_question_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_question_list);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        mRecyclerView.scrollToPosition(scrollPosition);

        final FloatingActionButton fabRefresh = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), v.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                    fabRefresh.setVisibility(View.INVISIBLE);
                }
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    fabRefresh.setVisibility(View.VISIBLE);
                }
            }
        });


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_launcher, menu);

        MenuItem menuItem = menu.findItem(R.id.action_filter);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                DialogFragment dialogFilter = new DialogFilter();
                dialogFilter.setTargetFragment(FragmentQuestionList.this, 1);
                dialogFilter.show(getFragmentManager(), "dFilter");

                DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawers();

                return true;
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            Toast.makeText(getActivity(), data.getStringExtra("language"), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), data.getStringExtra("category"), Toast.LENGTH_LONG).show();

            //ToDo: Filter SQL Queries here and refresh the adapter
        }
    }


    public List<Poll> getCurrentPollList(){
        DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
        List<PollBean> pollBeanList = TestData.getInstance().backendList;

        ModelTransformer transformer = new ModelTransformer();
        List<Poll> pollList = new ArrayList<>();

        for (PollBean pollBean: pollBeanList) {
            pollList.add(transformer.transformPollBeanToPoll(pollBean));
        }

        return pollList;
    }

}