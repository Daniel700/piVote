package model;


import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;

import database.DatabaseEndpoint;
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


    public ArrayList<PollBean> backendList;


    private TestData() {

        backendList = new ArrayList<>();

        for (int i = 0; i < 4; i++){
            backendList.add(Poll1());
            backendList.add(Poll2());
            backendList.add(Poll3());
        }


        DatabaseEndpoint databaseEndpoint = new DatabaseEndpoint();
        for (PollBean p: backendList) {
            databaseEndpoint.insertTask(p);
        }


    }




    public PollBean Poll1(){
        Date date = new Date();
        DateTime dateTime = new DateTime(date);

        PollBean poll = new PollBean();
        poll.setQuestion("What's your favorite hero of the marvel comics?");
        poll.setOverallVotes(2754);
        poll.setCreatedBy("Anonymous");
        poll.setCategory("Movies");
        poll.setLanguage("English");
        poll.setLastVoted(dateTime);
        poll.setCreationDate(dateTime);
            AnswerBean answer1 = new AnswerBean();
            answer1.setAnswerText("Iron Man");
            answer1.setAnswerVotes(643);
            AnswerBean answer2 = new AnswerBean();
            answer2.setAnswerText("Hulk");
            answer2.setAnswerVotes(444);
            ArrayList<AnswerBean> answers = new ArrayList<>();
            answers.add(answer1);
            answers.add(answer2);
        poll.setAnswerBeans(answers);

        return poll;
    }



    public PollBean Poll2(){
        Date date = new Date();
        DateTime dateTime = new DateTime(date);

        PollBean poll1 = new PollBean();
        poll1.setQuestion("Should Society spend more money on science?");
        poll1.setOverallVotes(1314);
        poll1.setCreatedBy("Anonymous");
        poll1.setCategory("Society");
        poll1.setLanguage("English");
        poll1.setLastVoted(dateTime);
        poll1.setCreationDate(dateTime);
            AnswerBean answerBean = new AnswerBean();
            answerBean.setAnswerText("Yes");
            answerBean.setAnswerVotes(714);
            AnswerBean answerBean1 = new AnswerBean();
            answerBean1.setAnswerText("No");
            answerBean1.setAnswerVotes(600);
            ArrayList<AnswerBean> arrayList = new ArrayList<>();
            arrayList.add(answerBean);
            arrayList.add(answerBean1);
        poll1.setAnswerBeans(arrayList);

        return poll1;
    }



    public PollBean Poll3(){
        Date date = new Date();
        DateTime dateTime = new DateTime(date);

        PollBean poll = new PollBean();
        poll.setQuestion("Which character of the Simpsons do you like the most?");
        poll.setOverallVotes(2034);
        poll.setCreatedBy("Anonymous");
        poll.setCategory("Movies");
        poll.setLanguage("English");
        poll.setLastVoted(dateTime);
        poll.setCreationDate(dateTime);
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
            ArrayList<AnswerBean> answers = new ArrayList<>();
            answers.add(answerBean);
        answers.add(answerBean1);
            answers.add(answerBean2);
            answers.add(answerBean3);
        poll.setAnswerBeans(answers);

        return poll;
    }

}
