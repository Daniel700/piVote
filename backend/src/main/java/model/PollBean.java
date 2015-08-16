package model;


import com.googlecode.objectify.annotation.Entity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 30.07.2015.
 */

@Entity
public class PollBean {

    private String questionID;
    private String question;
    private ArrayList<AnswerBean> answerBeans;
    private int overallVotes;
    private String language;
    private String category;
    private String createdBy;
    private Date creationDate;
    private Date lastVoted;



    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<AnswerBean> getAnswerBeans() {
        return answerBeans;
    }

    public void setAnswerBeans(ArrayList<AnswerBean> answerBeans) {
        this.answerBeans = answerBeans;
    }

    public int getOverallVotes() {
        return overallVotes;
    }

    public void setOverallVotes(int overallVotes) {
        this.overallVotes = overallVotes;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastVoted() {
        return lastVoted;
    }

    public void setLastVoted(Date lastVoted) {
        this.lastVoted = lastVoted;
    }
}