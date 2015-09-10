package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 15.08.2015.
 *
 * Poll class is needed for handling the data internally in the app.
 * Remote PollBeans cannot be Serializable (this is needed for passing the data between activities)
 * and the Date Format is automatically transformed into Google DateTime. In addition there are
 * only the standard constructors available.
 * Therefore we need our own model class directly in the app.
 */
//ToDo: Maybe introduce closingDate for Polls? also needed in PollBean
public class Poll implements Serializable {

    private Long id;
    private String uuid;
    private String question;
    private ArrayList<Answer> answers;
    private int overallVotes;
    private String language;
    private String category;
    private String createdBy;
    private Date creationDate;
    private Date lastVoted;
    private boolean alreadyVoted;
    private boolean isFavorite;


    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isAlreadyVoted() {
        return alreadyVoted;
    }

    public void setAlreadyVoted(boolean alreadyVoted) {
        this.alreadyVoted = alreadyVoted;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getLastVoted() {
        return lastVoted;
    }

    public void setLastVoted(Date lastVoted) {
        this.lastVoted = lastVoted;
    }

    public int getOverallVotes() {
        return overallVotes;
    }

    public void setOverallVotes(int overallVotes) {
        this.overallVotes = overallVotes;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}