package model;


import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;

import model.pollBeanApi.model.AnswerBean;
import model.pollBeanApi.model.PollBean;


/**
 * Created by Daniel on 09.08.2015.
 */
public class TestData {
    private static TestData ourInstance = new TestData();

    public static TestData getInstance() {
        return ourInstance;
    }

    public ArrayList<PollBean> questionList;
    public ArrayList<PollBean> myPollsList;
    public ArrayList<PollBean> recentlyVotedList;

    private TestData() {

        questionList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            PollBean p = new PollBean();
            p.setQuestion("What's your favorite hero of the marvel comics?");
            p.setOverallVotes(2754);
            p.setCreatedBy("Anonymous");
            p.setCategory("Movies");
            p.setLanguage("English");


            Date date = new Date();
            DateTime dateTime = new DateTime(date);
            p.setLastVoted(dateTime);
            p.setCreationDate(dateTime);

            ArrayList<AnswerBean> answers = new ArrayList<>();
            AnswerBean answer = new AnswerBean();
            answer.setAnswerText("Iron Man");
            answer.setAnswerVotes(643);
            answer.setPercentage(0.0);
            answer.setSelected(false);
            answers.add(answer);

            answer = new AnswerBean();
            answer.setAnswerText("Hulk");
            answer.setAnswerVotes(444);
            answer.setPercentage(0.0);
            answer.setSelected(false);
            answers.add(answer);

            p.setAnswerBeans(answers);

            questionList.add(p);
        }

/*
        myPollsList = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            Poll p = new Poll();
            p.setQuestion("Should Society spend more money on science?");
            p.setOverallVotes(1314);
            p.setCreatedBy("Anonymous");
            p.setCategory("Society");
            p.setLanguage("English");

            Date date = new Date();
            p.setLastVoted(date);
            p.setCreationDate(date);

            ArrayList<Answer> answers = new ArrayList<>();
            answers.add(new Answer("Yes", 714));
            answers.add(new Answer("No", 600));

            p.setAnswers(answers);

            myPollsList.add(p);
        }


        recentlyVotedList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            Poll p = new Poll();
            p.setQuestion("Which character of the Simpsons do you like the most?");
            p.setOverallVotes(2034);
            p.setCreatedBy("Anonymous");
            p.setCategory("Movies");
            p.setLanguage("English");

            Date date = new Date();
            p.setLastVoted(date);
            p.setCreationDate(date);

            ArrayList<Answer> answers = new ArrayList<>();
            answers.add(new Answer("Homer", 914));
            answers.add(new Answer("Marge", 210));
            answers.add(new Answer("Bart", 566));
            answers.add(new Answer("Lisa", 344));

            p.setAnswers(answers);

            recentlyVotedList.add(p);
        }

*/
    }
}
