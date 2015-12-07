package fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.QuestionListAdapter;
import database.DatabaseEndpoint;
import database.DatabaseLogEndpoint;
import database.FilterOptions;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.PollBeanApi;
import model.pollBeanApi.model.PollBean;
import piv.pivote.R;
import utils.Settings;

/**
 * Created by Daniel on 28.07.2015.
 */
public class FragmentQuestionList extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private QuestionListAdapter mAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private PollBeanApi pollBeanApi;
    private int languagePosition = 0;
    private int categoryPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pollBeanApi = DatabaseEndpoint.instantiateConnection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_question_list, container, false);

            if (Settings.AD_MOB_TEST_ENVIRONMENT)
            {
                //Ad Mob test version
                AdView mAdView = (AdView) rootView.findViewById(R.id.adView_question_list);
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("2D18A580DC26C325F086D6FB9D84F765").build();
                mAdView.loadAd(adRequest);
            }
            else {
                //Ad Mob productive version
                AdView mAdView = (AdView) rootView.findViewById(R.id.adView_question_list);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_question_list);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_question_list);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        mRecyclerView.scrollToPosition(scrollPosition);

        new LoadContentTask().execute();

        final FloatingActionButton fabRefresh = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(this);

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

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_question_list);
        swipeRefreshLayout.setOnRefreshListener(this);

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
            new LoadContentTask().execute();
        }
    }


    /**
     * Refresh Listener for Swipe Refresh
     */
    @Override
    public void onRefresh() {
        new ReloadContentTask().execute();
    }

    /**
     * onClick Method for Floating Button
     */
    @Override
    public void onClick(final View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder.setTitle(getString(R.string.checkRefreshTitle));

        alertDialogBuilder.setMessage(getString(R.string.checkRefreshMessage));
        alertDialogBuilder.setPositiveButton(getString(R.string.dialogButtonPositive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new LoadContentTask().execute();
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


    /**
     * Refreshes the current PollList with BatchRequests
     */
    private class ReloadContentTask extends AsyncTask<Void, Void, List<Poll>> {

        @Override
        protected List<Poll> doInBackground(Void... params) {

            ModelTransformer transformer = new ModelTransformer();
            List<Poll> pollList = new ArrayList<>();
            List<PollBean> beans = new ArrayList<PollBean>();

            try {
                    if (mAdapter.getIdList().size() <= 100)
                    {
                        beans = pollBeanApi.getBatchPollBeans(mAdapter.getIdList().subList(0, mAdapter.getIdList().size())).execute().getItems();
                    }
                    else
                    {
                        List<PollBean> beans1 = pollBeanApi.getBatchPollBeans(mAdapter.getIdList().subList(0, 100)).execute().getItems();
                        List<PollBean> beans2 = pollBeanApi.getBatchPollBeans(mAdapter.getIdList().subList(100, mAdapter.getIdList().size())).execute().getItems();
                        beans.addAll(beans1);
                        beans.addAll(beans2);
                    }

                if (beans.size() > 0)
                    for (PollBean pollBean : beans) {
                        pollList.add(transformer.transformPollBeanToPoll(pollBean));
                    }
            } catch (Exception e) {
                DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                endpoint.insertTask("FragmentQuestionList - swipeRefresh", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
            }

            return pollList;
        }

        @Override
        protected void onPostExecute(List<Poll> pollList) {
            super.onPostExecute(pollList);

            if (FragmentQuestionList.this.isVisible()){
                mAdapter = new QuestionListAdapter(pollList, getActivity().getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        }

    }


    /**
     * Requests 200 random Polls from the remote Database.
     */
    private class LoadContentTask extends AsyncTask<Void, Void, List<Poll>> {

        String language;
        String category;

        @Override
        protected List<Poll> doInBackground(Void... params) {
            List<PollBean> beans = null;
            List<Poll> pollList = new ArrayList<>();

            try {
                beans = pollBeanApi.getRandomPollBeans(category, language).execute().getItems();

                ModelTransformer transformer = new ModelTransformer();
                if (beans != null)
                    for (PollBean pollBean: beans) {
                        pollList.add(transformer.transformPollBeanToPoll(pollBean));
                    }
            }
            catch (IOException e) {
                DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                endpoint.insertTask("FragmentQuestionList - Async", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
                e.printStackTrace();
            }

            return pollList;
        }


        @Override
        protected void onPostExecute(List<Poll> pollList) {
            super.onPostExecute(pollList);

            if (FragmentQuestionList.this.isVisible()){
                progressBar.setVisibility(View.GONE);
                mAdapter = new QuestionListAdapter(pollList, getActivity().getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
                Snackbar.make(getActivity().findViewById(R.id.coordinatorLayoutQuestionList), getString(R.string.refresh1) + " " + String.valueOf(mAdapter.getItemCount()) + " " + getString(R.string.refresh2), Snackbar.LENGTH_LONG).show();
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            language = FilterOptions.languages.get(languagePosition);
            category = FilterOptions.categories.get(categoryPosition);
        }
    }


}