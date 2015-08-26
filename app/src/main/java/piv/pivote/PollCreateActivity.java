package piv.pivote;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;

import adapter.CreateAnswersAdapter;
import database.DatabaseEndpoint;
import layoutManager.DividerItemDecoration;
import layoutManager.MyLinearLayoutManager;
import model.TestData;
import model.pollBeanApi.model.AnswerBean;
import model.pollBeanApi.model.PollBean;

/**
 * Created by Daniel on 07.08.2015.
 */
public class PollCreateActivity extends AppCompatActivity {

    //private RecyclerView.LayoutManager mLayoutManager;
    private MyLinearLayoutManager mLayoutManager;
    private CreateAnswersAdapter mAdapter;
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
        final EditText question = (EditText) findViewById(R.id.editText_question_cp);
        final Spinner spinnerLanguage = (Spinner) findViewById(R.id.spinner_language_cp);
        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinner_category_cp);
        Spinner spinnerNumberOfAnswers = (Spinner) findViewById(R.id.spinner_numberAnswers_cp);
        final EditText name = (EditText) findViewById(R.id.editText_name_cp);

        // use a linear layout manager
        int scrollPosition = 0;
        if (mLayoutManager != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        } else {
            mLayoutManager = new MyLinearLayoutManager(this, MyLinearLayoutManager.VERTICAL, false);
            //mLayoutManager = new LinearLayoutManager(PollCreateActivity.this);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        mRecyclerView.scrollToPosition(scrollPosition);


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

                        mAdapter = new CreateAnswersAdapter(arrayList);
                        mRecyclerView.setAdapter(mAdapter);
                        // Update RecyclerView Height (only with standard layoutManager)
                        // mRecyclerView.getLayoutParams().height = 100 * numberOfAnswers;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




        Button button = (Button) findViewById(R.id.button_create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //################# Conduct validation of all input fields ########################
                CreateAnswersAdapter createAnswersAdapter = ((CreateAnswersAdapter) mAdapter);
                ArrayList<String> answers = createAnswersAdapter.getAnswers();

                String[] resLanguages = getResources().getStringArray(R.array.languages);
                String[] resCategories = getResources().getStringArray(R.array.categories);

                //Check if User has entered all attributes for the corresponding Poll
                if (question.getText().toString().trim().isEmpty())
                {
                    question.setError(getString(R.string.createQuestionError));
                }
                else if (language.equals(resLanguages[0])){
                    Snackbar.make(findViewById(R.id.spinner_language_cp), getString(R.string.createLanguageError), Snackbar.LENGTH_LONG).show();
                }
                else if (category.equals(resCategories[0])){
                    Snackbar.make(findViewById(R.id.spinner_category_cp), getString(R.string.createCategoryError), Snackbar.LENGTH_LONG).show();
                }
                else if (!(answers.size() == createAnswersAdapter.getItemCount()) || numberOfAnswers == 0){
                    Snackbar.make(findViewById(R.id.recycler_view_answers_cp), getString(R.string.createAnswersError), Snackbar.LENGTH_LONG).show();
                }
                else {
                    // Poll has all attributes and can be created
                    if (name.getText().toString().trim().isEmpty())
                        name.setText("Anonymous");


                    SharedPreferences sharedPreferences = getSharedPreferences("InstallSettings", MODE_PRIVATE);
                    String uuid = sharedPreferences.getString("UUID", "no id available");

                    PollBean pollBean = new PollBean();

                    pollBean.setUuid(uuid);
                    pollBean.setQuestion(question.getText().toString().trim());
                    pollBean.setLanguage(language);
                    pollBean.setCategory(category);
                    pollBean.setCreatedBy(name.getText().toString().trim());
                    pollBean.setOverallVotes(0);

                    pollBean.setCreationDate(new DateTime(new Date()));
                    pollBean.setLastVoted(new DateTime(new Date()));

                    ArrayList<AnswerBean> answerBeans = new ArrayList<AnswerBean>();
                    for (String answer: answers) {
                        AnswerBean bean = new AnswerBean();
                        bean.setAnswerText(answer);
                        bean.setAnswerVotes(0);
                        answerBeans.add(bean);
                    }
                    pollBean.setAnswerBeans(answerBeans);

                    DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
                    databaseEndpoint.insertTask(pollBean);

                    setResult(RESULT_OK);
                    finish();
                }

            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
