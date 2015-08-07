package fragments;



import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.QuestionListAdapter;
import piv.pivote.DialogFilter;
import piv.pivote.Poll;
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

        ArrayList<Poll> pollList = new ArrayList<>();
        // 1.
        for (int i = 0; i < 25; i++){
            Poll p = new Poll();
            p.setQuestion("What's your favorite hero of the marvel comics?");
            p.setOverallVotes(2754);
            p.setCreatedBy("Anonymous");
            p.setCategory("Movies");
            p.setLanguage("English");

            ArrayList<String> answers = new ArrayList<>();
            answers.add("Iron Man");
            answers.add("Hulk");
            answers.add("Captain America");
            answers.add("Thor");
            answers.add("Ant-Man");
            p.setAnswers(answers);

            ArrayList<Integer> answerVotes = new ArrayList<>();
            answerVotes.add(1021);
            answerVotes.add(404);
            answerVotes.add(210);
            answerVotes.add(766);
            answerVotes.add(353);
            p.setAnswerVotes(answerVotes);

            pollList.add(p);
        }
        mAdapter = new QuestionListAdapter(pollList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_question_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_question_list);

        // 2.
        // use a linear layout manager
        int scrollPosition = 0;
        if (mLayoutManager != null){
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }

        // 3.
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setAdapter(mAdapter);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // mRecyclerView.setHasFixedSize(true);

        final FloatingActionButton fabFilter = (FloatingActionButton) rootView.findViewById(R.id.fab_filter);
        final FloatingActionButton fabRefresh = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh);


        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), v.toString(), Toast.LENGTH_SHORT).show();
                DialogFragment dialogFilter = new DialogFilter();
                dialogFilter.show(getFragmentManager(), "dFilter");
            }
        });


        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), v.toString(),Toast.LENGTH_SHORT).show();
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                    fabFilter.setVisibility(View.INVISIBLE);
                    fabRefresh.setVisibility(View.INVISIBLE);
                }
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    fabFilter.setVisibility(View.VISIBLE);
                    fabRefresh.setVisibility(View.VISIBLE);
                }
            }
        });


        return rootView;
    }

}