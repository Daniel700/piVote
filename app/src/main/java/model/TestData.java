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
            answers.add(answer);

            answer = new AnswerBean();
            answer.setAnswerText("Hulk");
            answer.setAnswerVotes(444);
            answers.add(answer);

            p.setAnswerBeans(answers);

            questionList.add(p);
        }


        myPollsList = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            PollBean p = new PollBean();
            p.setQuestion("Should Society spend more money on science?");
            p.setOverallVotes(1314);
            p.setCreatedBy("Anonymous");
            p.setCategory("Society");
            p.setLanguage("English");


            Date date = new Date();
            DateTime dateTime = new DateTime(date);
            p.setLastVoted(dateTime);
            p.setCreationDate(dateTime);


            AnswerBean answerBean = new AnswerBean();
            answerBean.setAnswerText("Yes");
            answerBean.setAnswerVotes(714);

            AnswerBean answerBean1 = new AnswerBean();
            answerBean1.setAnswerText("No");
            answerBean1.setAnswerVotes(600);

            ArrayList<AnswerBean> arrayList = new ArrayList<>();
            arrayList.add(answerBean);
            arrayList.add(answerBean1);


            p.setAnswerBeans(arrayList);

            myPollsList.add(p);
        }


        recentlyVotedList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            PollBean p = new PollBean();
            p.setQuestion("Which character of the Simpsons do you like the most?");
            p.setOverallVotes(2034);
            p.setCreatedBy("Anonymous");
            p.setCategory("Movies");
            p.setLanguage("English");

            Date date = new Date();
            DateTime dateTime = new DateTime(date);
            p.setLastVoted(dateTime);
            p.setCreationDate(dateTime);

            ArrayList<AnswerBean> answers = new ArrayList<>();

            AnswerBean answerBean = new AnswerBean();
            answerBean.setAnswerText("Homer");
            answerBean.setAnswerVotes(914);

            AnswerBean answerBean1 = new AnswerBean();
            answerBean1.setAnswerText("Marge");
            answerBean1.setAnswerVotes(210);

            AnswerBean answerBean2 = new AnswerBean();
            answerBean2.setAnswerText("Bart");
            answerBean2.setAnswerVotes(566);

            AnswerBean answerBean3 = new AnswerBean();
            answerBean3.setAnswerText("Lisa");
            answerBean3.setAnswerVotes(344);


            answers.add(answerBean);
            answers.add(answerBean1);
            answers.add(answerBean2);
            answers.add(answerBean3);

            p.setAnswerBeans(answers);

            recentlyVotedList.add(p);
        }


    }
}
