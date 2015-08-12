package dataObjects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Daniel on 09.08.2015.
 */
public class TestData {
    private static TestData ourInstance = new TestData();

    public static TestData getInstance() {
        return ourInstance;
    }

    public ArrayList<Poll> questionList;
    public ArrayList<Poll> myPollsList;
    public ArrayList<Poll> recentlyVotedList;

    private TestData() {

        questionList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Poll p = new Poll();
            p.setQuestion("What's your favorite hero of the marvel comics?");
            p.setOverallVotes(2754);
            p.setCreatedBy("Anonymous");
            p.setCategory("Movies");
            p.setLanguage("English");

            Date date = new Date();
            p.setLastVoted(date);
            p.setCreationDate(date);

            ArrayList<Answer> answers = new ArrayList<>();
            answers.add(new Answer("Iron Man", 1021));
            answers.add(new Answer("Hulk", 404));
            answers.add(new Answer("Captain America", 210));
            answers.add(new Answer("Thor", 766));
            answers.add(new Answer("Ant Man", 353));

            p.setAnswers(answers);

            questionList.add(p);
        }


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


    }
}
