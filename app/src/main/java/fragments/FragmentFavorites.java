package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import model.pollBeanApi.model.PollBean;
import piv.pivote.R;
import utils.Settings;

/**
 * Created by Daniel on 12.08.2015.
 */
public class FragmentFavorites extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_favorite_polls);

        mAdapter = new QuestionListAdapter(getCurrentPollList(), getActivity().getApplicationContext());
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        int scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        recyclerView.scrollToPosition(scrollPosition);

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_favorite_polls);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    mAdapter = new QuestionListAdapter(getCurrentPollList(), getActivity().getApplicationContext());
                    recyclerView.setAdapter(mAdapter);
                }
                catch (Exception e){
                    DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                    endpoint.insertTask("FragmentFavorites - swipeRefresh", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
                    e.printStackTrace();
                }
                finally {
                    refreshLayout.setRefreshing(false);
                }
            }
        });

        Button button = (Button) rootView.findViewById(R.id.button_delete_favs);
        button.setOnClickListener(new View.OnClickListener() {
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

                        mAdapter = new QuestionListAdapter(getCurrentPollList(), getActivity().getApplicationContext());
                        recyclerView.setAdapter(mAdapter);

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


        return rootView;
    }

    /**
     * Requests all Polls that are marked as favorite(in local DB) from the remote Database.
     * @return Poll list for the Adapter
     */
    public List<Poll> getCurrentPollList(){

        SQLiteAccess dbAccess = new SQLiteAccess(getActivity().getApplicationContext());
        List<Long> list = dbAccess.getAllFavoritePolls();
        dbAccess.close();

        List<Poll> pollList = new ArrayList<>();
        if (list.size() > 0)
        {
            DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
            List<PollBean> pollBeanList = databaseEndpoint.getBatchPollTask(list);
            ModelTransformer transformer = new ModelTransformer();

            if (pollBeanList != null)
                for (PollBean pollBean: pollBeanList) {
                    pollList.add(transformer.transformPollBeanToPoll(pollBean));
                }
        }

        return pollList;
    }

}
