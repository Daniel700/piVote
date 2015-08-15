package model;

/**
 * Created by Daniel on 07.08.2015.
 */
public class AnswerBean {

    private String answerText;
    private int answerVotes;
    private double percentage;
    private boolean selected;

//Todo: percentage and selected needed?

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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
