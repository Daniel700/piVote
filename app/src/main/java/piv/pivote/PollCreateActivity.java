package piv.pivote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.CreateAnswersAdapter;

/**
 * Created by Daniel on 07.08.2015.
 */
public class PollCreateActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;

    private String language;
    private String category;
    private int numberOfAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_create);

        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.activity_create_poll));
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //find Views
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_answers_cp);
        TextView question = (TextView) findViewById(R.id.editText_question_cp);
        Spinner spinnerLanguage = (Spinner) findViewById(R.id.spinner_language_cp);
        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinner_category_cp);
        Spinner spinnerNumberOfAnswers = (Spinner) findViewById(R.id.spinner_numberAnswers_cp);
        TextView name = (TextView) findViewById(R.id.editText_name_cp);
        //ToDo: Conduct validation in EditText fields (floating labels)

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerNumberOfAnswers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numberOfAnswers = Integer.valueOf(parent.getSelectedItem().toString());

                ArrayList<Integer> arrayList = new ArrayList<>();
                for (int i = 1; i <= numberOfAnswers; i++) {
                    arrayList.add(i);
                }

                    //1.
                    mAdapter = new CreateAnswersAdapter(arrayList);
                    mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 2.
        // use a linear layout manager
        int scrollPosition = 0;
        if (mLayoutManager != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        } else {
            mLayoutManager = new LinearLayoutManager(PollCreateActivity.this);
        }
        // 3.
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);


        Button button = (Button) findViewById(R.id.button_create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo: Create Poll Object in DB and redirect to MyPolls
                //ToDo: Access EditTexts of RecyclerView to get the answers
                //ToDo: Make creation of only 3 Polls per hour possible
                Toast.makeText(getApplicationContext(), "Object created", Toast.LENGTH_SHORT).show();
            }
        });

        //Set height of recycler view according to the number of items
        /*
        int viewHeight = 75 * poll.getAnswers().size();
        mRecyclerView.getLayoutParams().height = viewHeight;
        Log.e("size", String.valueOf(viewHeight));
        */



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), LauncherActivity.class);
        intent.putExtra("from", this.getTitle().toString());

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
