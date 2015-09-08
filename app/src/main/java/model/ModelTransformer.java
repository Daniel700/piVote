package model;



import com.google.api.client.util.DateTime;


import java.util.ArrayList;
import java.util.Date;

import model.pollBeanApi.model.AnswerBean;
import model.pollBeanApi.model.PollBean;

/**
 * Helper class that can transform a given PollBean to a Poll.
 * This is needed because of the other data model within the application.
 * Created by Daniel on 15.08.2015.
 */
public class ModelTransformer {



    public Poll transformPollBeanToPoll(PollBean pollBean){

        Poll poll = new Poll();
        poll.setQuestion(pollBean.getQuestion());
        poll.setCategory(pollBean.getCategory());
        poll.setOverallVotes(pollBean.getOverallVotes());
        poll.setLanguage(pollBean.getLanguage());
        poll.setCreatedBy(pollBean.getCreatedBy());
        poll.setUuid(pollBean.getUuid());
        poll.setId(pollBean.getId());

        //Set Answers in Poll
        ArrayList<Answer> answers = new ArrayList<Answer>();
        for (AnswerBean answerBean: pollBean.getAnswerBeans()) {
            answers.add(new Answer(answerBean.getAnswerText(), answerBean.getAnswerVotes(), false, 0));
        }
        poll.setAnswers(answers);


        //Transform the Dates
        DateTime dateTime = pollBean.getCreationDate();
        Date date = new Date(dateTime.getValue());
        poll.setCreationDate(date);

        dateTime = pollBean.getLastVoted();
        date = new Date(dateTime.getValue());
        poll.setLastVoted(date);

        return poll;
    }


    public PollBean transformFromPollToPollBean(Poll poll){

        PollBean pollBean = new PollBean();
        pollBean.setId(poll.getId());
        pollBean.setUuid(poll.getUuid());
        pollBean.setQuestion(poll.getQuestion());
        pollBean.setCategory(poll.getCategory());
        pollBean.setLanguage(poll.getLanguage());
        pollBean.setOverallVotes(poll.getOverallVotes());
        pollBean.setCreatedBy(poll.getCreatedBy());


        //Answers
        ArrayList<AnswerBean> answers = new ArrayList<>();
        for (Answer answer: poll.getAnswers()) {

            AnswerBean answerBean = new AnswerBean();
            answerBean.setAnswerText(answer.getAnswerText());
            answerBean.setAnswerVotes(answer.getAnswerVotes());
            answers.add(answerBean);
        }
        pollBean.setAnswerBeans(answers);


        //CreationDate
        DateTime dateTime = new DateTime(poll.getCreationDate());
        pollBean.setCreationDate(dateTime);

        //LastVoted
        DateTime dateTime1 = new DateTime(poll.getLastVoted());
        pollBean.setLastVoted(dateTime1);


        return pollBean;
    }


}
