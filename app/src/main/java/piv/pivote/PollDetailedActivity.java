package piv.pivote;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.util.ArrayList;

import adapter.AnswersAdapter;
import database.DatabaseEndpoint;
import database.DatabaseLogEndpoint;
import database.FilterOptions;
import database.SQLiteAccess;
import model.Answer;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.model.AnswerBean;
import model.pollBeanApi.model.PollBean;


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
        setTitle(getResources().getString(R.string.activity_detailed_poll));
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        poll = (Poll) getIntent().getSerializableExtra("Poll");
        //Set TextViews according to the selected poll
        try {
            TextView tv = (TextView) findViewById(R.id.question_detailed);
            tv.setText(poll.getQuestion());
            tv = (TextView) findViewById(R.id.overallVotes_detailed);
            tv.setText(String.valueOf(poll.getOverallVotes()));

            tv = (TextView) findViewById(R.id.category_detailed);
            String[] cat = getResources().getStringArray(R.array.categories);
            int catPos = FilterOptions.categories.indexOf(poll.getCategory());
            tv.setText(cat[catPos]);

            tv = (TextView) findViewById(R.id.language_detailed);
            String[] lang = getResources().getStringArray(R.array.languages);
            int langPos = FilterOptions.languages.indexOf(poll.getLanguage());
            tv.setText(lang[langPos]);

            tv = (TextView) findViewById(R.id.lastVote_detailed);
            tv.setText(df.format(poll.getLastVoted()));
            tv = (TextView) findViewById(R.id.createdBy_detailed);
            tv.setText(poll.getCreatedBy());
            tv = (TextView) findViewById(R.id.creationDate_detailed);
            tv.setText(df.format(poll.getCreationDate()));
        }
        catch (Exception e){
            DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
            endpoint.insertLogTask("PollDetailedActivity - Views", e.getMessage());
            e.printStackTrace();
        }


        mAdapter = new AnswersAdapter(poll, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_answers_detailed);

        int scrollPosition = 0;
        if (mLayoutManager != null){
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        else {
            mLayoutManager = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
        mRecyclerView.setAdapter(mAdapter);


        Button button = (Button) findViewById(R.id.button_vote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Answer a = ((AnswersAdapter) mAdapter).getChosenAnswer();

                    //Save Poll in local SQLite Database
                    SQLiteAccess dbAccess = new SQLiteAccess(getApplicationContext());
                    dbAccess.insertPoll(poll, a.getAnswerText());
                    dbAccess.close();

                    ModelTransformer modelTransformer = new ModelTransformer();
                    PollBean pollBean = modelTransformer.transformFromPollToPollBean(poll);

                    //ToDo: Check if sharding is needed and possible?
                    DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
                    databaseEndpoint.updatePollTask(pollBean, a.getAnswerText());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("snackbarDetailed", a.getAnswerText());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } catch (Exception e) {
                    DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                    endpoint.insertLogTask("PollDetailedActivity - button_vote", e.getMessage());
                    Toast.makeText(getApplicationContext(), getString(R.string.voteSubmitError), Toast.LENGTH_SHORT).show();
                }

            }
        });


        // if already voted enable locking on the answerList and mark the question red as well as the answer checked
        SQLiteAccess dbAccess = new SQLiteAccess(getApplicationContext());
        Pair<Boolean, String> pair = dbAccess.findPoll(poll);
        boolean alreadyVoted = pair.first;
        String selectedAnswer = pair.second;
        dbAccess.close();

        if (alreadyVoted){
            button.setEnabled(false);

            for (Answer answer: poll.getAnswers()) {
                if (answer.getAnswerText().equals(selectedAnswer))
                {
                    answer.setSelected(true);
                }

            }
            mAdapter = new AnswersAdapter(poll, true);
            mRecyclerView.setAdapter(mAdapter);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
