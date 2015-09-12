package piv.pivote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;

import adapter.DetailedPollAnswersAdapter;
import database.DatabaseEndpoint;
import database.DatabaseLogEndpoint;
import database.FilterOptions;
import database.SQLiteAccess;
import model.Answer;
import model.ModelTransformer;
import model.Poll;
import model.pollBeanApi.model.PollBean;


/**
 * Activity for showing a poll with detailed information.
 * Will be called after a list item is clicked.
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
            endpoint.insertTask("PollDetailedActivity - Views", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
            e.printStackTrace();
        }

        //Set Adapter
        mAdapter = new DetailedPollAnswersAdapter(poll, false);
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

        //saves the selected vote in local and remote DB
        Button button = (Button) findViewById(R.id.button_vote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Answer a = ((DetailedPollAnswersAdapter) mAdapter).getChosenAnswer();

                    //Save Poll in local SQLite Database
                    SQLiteAccess dbAccess = new SQLiteAccess(getApplicationContext());
                    dbAccess.insertPoll(poll, a.getAnswerText());
                    //dbAccess.printAllPolls();
                    dbAccess.close();

                    ModelTransformer modelTransformer = new ModelTransformer();
                    PollBean pollBean = modelTransformer.transformFromPollToPollBean(poll);

                    DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
                    databaseEndpoint.updatePollTask(pollBean, a.getAnswerText());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("snackbarDetailed", a.getAnswerText());
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } catch (Exception e) {
                    DatabaseLogEndpoint endpoint = new DatabaseLogEndpoint();
                    endpoint.insertTask("PollDetailedActivity - button_vote", "1st Msg: " + e.getMessage() + "\n 2nd Msg: " + e.toString());
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
            mAdapter = new DetailedPollAnswersAdapter(poll, true);
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
