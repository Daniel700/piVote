package tabs;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import adapter.CreatePollAdapter;
import adapter.QuestionListAdapter;
import piv.pivote.Poll;
import piv.pivote.R;

/**
 * Created by Daniel on 30.07.2015.
 */
public class TabFragmentRecentlyVoted extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Poll> pollList = new ArrayList<>();
        // 1.
        for (int i = 0; i < 6; i++){
            Poll p = new Poll();
            p.setQuestion("Which character of the Simpsons do you like the most?");
            p.setOverallVotes(456);
            p.setCreatedBy("Anonymous");
            p.setCategory("Movies");
            p.setLanguage("English");
            pollList.add(p);
        }
        mAdapter = new QuestionListAdapter(pollList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_recently_voted, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_recently_voted);

        // 2.
        // use a linear layout manager
        int scrollPosition = 0;
        if (mLayoutManager != null){
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        // 3.
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
        recyclerView.setAdapter(mAdapter);


        return rootView;
    }
}
