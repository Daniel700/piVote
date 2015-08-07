package piv.pivote;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.AnswersAdapter;

/**
 * Created by Daniel on 02.08.2015.
 */
public class PollDetailedActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private Poll poll;


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


        poll = (Poll) getIntent().getSerializableExtra("Poll");


/*
        poll = new Poll();
        poll.setQuestion("Wer sollte der nächste Bundeskanzler werden?");
        poll.setLanguage("Deutsch");
        poll.setCategory("Politik");
        poll.setOverallVotes(1276);
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
*/

        //Set TextViews according to the selected poll
        try {
            TextView tv = (TextView) findViewById(R.id.question_detailed);
            tv.setText(poll.getQuestion());
            tv = (TextView) findViewById(R.id.overallVotes_detailed);
            tv.setText(String.valueOf(poll.getOverallVotes()));
            tv = (TextView) findViewById(R.id.category_detailed);
            tv.setText(poll.getCategory());
            tv = (TextView) findViewById(R.id.language_detailed);
            tv.setText(poll.getLanguage());
            tv = (TextView) findViewById(R.id.lastVote_detailed);
            tv.setText("04.08.2015");
            tv = (TextView) findViewById(R.id.createdBy_detailed);
            tv.setText(poll.getCreatedBy());
            tv = (TextView) findViewById(R.id.creationDate_detailed);
            tv.setText("30.07.2015");
        }
        catch (Exception e){
            e.printStackTrace();
        }



        //1.
        mAdapter = new AnswersAdapter(poll);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_answers);

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

        Button button = (Button) findViewById(R.id.button_vote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), String.valueOf(v.getId()), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        int viewHeight = 75 * poll.getAnswers().size();
        mRecyclerView.getLayoutParams().height = viewHeight;
        Log.e("size", String.valueOf(viewHeight));
        */


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
