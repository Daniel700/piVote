package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

import adapter.QuestionListAdapter;
import database.DatabaseEndpoint;
import database.DatabaseLogEndpoint;
import database.SQLiteAccess;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.PollBeanApi;
import model.pollBeanApi.model.PollBean;
import piv.pivote.R;
import utils.Settings;

/**
 * Created by Daniel on 12.08.2015.
 */
public class FragmentFavorites extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private PollBeanApi pollBeanApi;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pollBeanApi = DatabaseEndpoint.instantiateConnection();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

            if (Settings.AD_MOB_TEST_ENVIRONMENT)
            {
                //Ad Mob test version
                AdView mAdView = (AdView) rootView.findViewById(R.id.adView_favorite_polls);
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("2D18A580DC26C325F086D6FB9D84F765").build();
                mAdView.loadAd(adRequest);
            }
            else
            {
                //Ad Mob productive version
                AdView mAdView = (AdView) rootView.findViewById(R.id.adView_favorite_polls);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_favorites);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_favorite_polls);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_favorite_polls);
        swipeRefreshLayout.setOnRefreshListener(this);

        Button button = (Button) rootView.findViewById(R.id.button_delete_favs);
        button.setOnClickListener(this);

        new LoadContentTask(false).execute();

        return rootView;
    }

    /**
     *  onClick Listener to delete polls
     */
    @Override
    public void onClick(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
        alertDialogBuilder.setTitle(getString(R.string.dialogDeleteTitle));
        alertDialogBuilder.setMessage(getString(R.string.dialogDeleteMessage));
        alertDialogBuilder.setPositiveButton(getString(R.string.dialogButtonPositive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SQLiteAccess dbAccess = new SQLiteAccess(getActivity().getApplicationContext());
                dbAccess.deleteAllFavoritePolls();
                dbAccess.close();

                new LoadContentTask(false).execute();

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
     * Refresh Listener for SwipeLayout
     */
    @Override
    public void onRefresh() {
        try {
            new LoadContentTask(true).execute();
        }
        catch (Exception e){
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertTask("FragmentFavorites - swipeRefresh", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
            e.printStackTrace();
        }
        finally {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    /**
     * Requests all Polls that are marked as favorite(in local DB) from the remote Database.
     */
    private class LoadContentTask extends AsyncTask<Void, Void, List<Poll>> {

        boolean isSwipe;
        LoadContentTask(boolean isSwipe){
            this.isSwipe = isSwipe;
        }

        @Override
        protected List<Poll> doInBackground(Void... params) {

            SQLiteAccess dbAccess = new SQLiteAccess(getActivity().getApplicationContext());
            List<Long> list = dbAccess.getAllFavoritePolls();
            dbAccess.close();

            List<PollBean> pollBeanList = null;
            List<Poll> pollList = new ArrayList<>();
            ModelTransformer transformer = new ModelTransformer();

            if (list.size() > 0)
            {
                try {
                    pollBeanList = pollBeanApi.getBatchPollBeans(list).execute().getItems();
                }
                catch (Exception e){
                    DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                    endpoint.insertTask("GetBatchPollTask - Async", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
                    e.printStackTrace();
                }

                if (pollBeanList != null)
                    for (PollBean pollBean: pollBeanList) {
                        pollList.add(transformer.transformPollBeanToPoll(pollBean));
                    }
            }

            return pollList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isSwipe)
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Poll> pollList) {
            super.onPostExecute(pollList);

            if (rootView.hasFocus()){
                progressBar.setVisibility(View.GONE);
                mAdapter = new QuestionListAdapter(pollList, getContext());
                recyclerView.setAdapter(mAdapter);
            }
        }

    }


}
