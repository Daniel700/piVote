package fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import adapter.QuestionListAdapter;
import database.DatabaseEndpoint;
import database.DatabaseLogEndpoint;
import database.SQLiteAccess;
import model.ModelTransformer;
import model.Poll;
import model.logBeanApi.model.LogBean;
import model.pollBeanApi.model.PollBean;
import piv.pivote.R;

/**
 * Created by Daniel on 28.07.2015.
 */
public class FragmentQuestionList extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private QuestionListAdapter mAdapter;
    private int languagePosition = 0;
    private int categoryPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new QuestionListAdapter(getCurrentPollList(languagePosition, categoryPosition), getActivity().getApplicationContext());
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle(getString(R.string.checkRefreshTitle));

                alertDialogBuilder.setMessage(getString(R.string.checkRefreshMessage));
                alertDialogBuilder.setPositiveButton(getString(R.string.dialogButtonPositive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAdapter = new QuestionListAdapter(getCurrentPollList(languagePosition, categoryPosition), getActivity().getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);
                        Snackbar.make(rootView, getString(R.string.refresh1) + " " + String.valueOf(mAdapter.getItemCount()) + " " + getString(R.string.refresh2), Snackbar.LENGTH_LONG).show();

                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.setNegativeButton(getString(R.string.dialogButtonNegative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_question_list);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
                    List<PollBean> beans1 = databaseEndpoint.getBatchPollTask(mAdapter.getIdList().subList(0, 100));
                    List<PollBean> beans2 = databaseEndpoint.getBatchPollTask(mAdapter.getIdList().subList(100, mAdapter.getIdList().size()));

                    List<PollBean> beans = new ArrayList<PollBean>();
                    beans.addAll(beans1);
                    beans.addAll(beans2);

                    ModelTransformer transformer = new ModelTransformer();
                    List<Poll> pollList = new ArrayList<>();

                    if (beans.size() > 0)
                        for (PollBean pollBean : beans) {
                            pollList.add(transformer.transformPollBeanToPoll(pollBean));
                        }

                    mAdapter = new QuestionListAdapter(pollList, getActivity().getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);
                } catch (Exception e) {
                    DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                    endpoint.insertTask("FragmentQuestionList - swipeRefresh", e.getMessage());
                    e.printStackTrace();
                } finally {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        //Ad Mob productive version
        /*
        AdView mAdView = (AdView) findViewById(R.id.adView_question_list);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */

        //Ad Mob test version
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView_question_list);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("2D18A580DC26C325F086D6FB9D84F765").build();
        mAdView.loadAd(adRequest);

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
            languagePosition = data.getIntExtra("language", 0);
            categoryPosition = data.getIntExtra("category", 0);
            mAdapter = new QuestionListAdapter(getCurrentPollList(languagePosition, categoryPosition), getActivity().getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
            Snackbar.make(mRecyclerView, getString(R.string.refresh1) + " " + String.valueOf(mAdapter.getItemCount()) + " " + getString(R.string.refresh2), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Requests 200 random Polls from the remote Database.
     * @return Poll list for the Adapter
     */
    public List<Poll> getCurrentPollList(int languagePosition, int categoryPosition){
        DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
        List<PollBean> pollBeanList = databaseEndpoint.getRandomPollsTask(languagePosition, categoryPosition);

        ModelTransformer transformer = new ModelTransformer();
        List<Poll> pollList = new ArrayList<>();

        if (pollBeanList != null)
        for (PollBean pollBean: pollBeanList) {
            pollList.add(transformer.transformPollBeanToPoll(pollBean));
        }

        return pollList;
    }

}