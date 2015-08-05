package tabs;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import adapter.CreatePollAdapter;
import adapter.QuestionListAdapter;
import piv.pivote.Poll;
import piv.pivote.R;

/**
 * Created by Daniel on 28.07.2015.
 */
public class TabFragmentCreatePoll extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Poll> pollList = new ArrayList<>();
        // 1.
        for (int i = 0; i < 4; i++){
            Poll p = new Poll();
            p.setQuestion("Should Society spend more money on science?");
            p.setOverallVotes(1314);
            p.setCreatedBy("Anonymous");
            p.setCategory("Society");
            p.setLanguage("English");
            p.setLastVoted(new Date());
            pollList.add(p);
        }
        mAdapter = new CreatePollAdapter(pollList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment_create_poll, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_create_poll);

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


        FloatingActionButton fabAdd = (FloatingActionButton) rootView.findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), v.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }
}