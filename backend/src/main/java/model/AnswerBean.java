package model;

import com.googlecode.objectify.annotation.Container;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Subclass;


/**
 * Created by Daniel on 07.08.2015.
 */


public class AnswerBean {

    private String answerText;
    private int answerVotes;


    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getAnswerVotes() {
        return answerVotes;
    }

    public void setAnswerVotes(int answerVotes) {
        this.answerVotes = answerVotes;
    }

}
