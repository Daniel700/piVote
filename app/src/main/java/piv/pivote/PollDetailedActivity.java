package piv.pivote;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.AnswersAdapter;

/**
 * Created by Daniel on 02.08.2015.
 */
public class PollDetailedActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_detailed);

        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Detailed Poll View");
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        // Poll p = (Poll) getIntent().getSerializableExtra("Poll");
        //Toast.makeText(getApplicationContext(), p.getQuestion(), Toast.LENGTH_LONG).show();

        Poll poll = new Poll();
        poll.setQuestion("Wer sollte der nächste Bundeskanzler werden?");
        poll.setLanguage("Deutsch");
        poll.setCategory("Politik");
        poll.setOverallVotes(2141);
        poll.setCreatedBy("Dani");

        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Merkel");
        answers.add("Gabriel");
        answers.add("Seehofer");
        answers.add("Roth");
        poll.setAnswers(answers);

        ArrayList<Integer> answerVotes = new ArrayList<>();
        answerVotes.add(566);
        answerVotes.add(234);
        answerVotes.add(444);
        answerVotes.add(32);
        poll.setAnswerVotes(answerVotes);



        //1.
        mAdapter = new AnswersAdapter(poll);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_answers);

        // 2.
        // use a linear layout manager
        int scrollPosition = 0;
        if (mLayoutManager != null){
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        else {
            mLayoutManager = new LinearLayoutManager(this);
        }

        // 3.
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
