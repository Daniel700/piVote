package piv.pivote;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.Date;

import database.SQLiteAccess;
import model.Poll;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();



        for (Long i = 1L; i <= 15000L; i++){
            SQLiteAccess dbAccess = new SQLiteAccess(getContext());
            Poll poll = new Poll();
            poll.setId(i);
            Date date = new Date();
            poll.setCreationDate(date);
            poll.setLastVoted(date);
            dbAccess.insertPoll(poll, "answer" + i.toString());
        }

        //dbAccess.printAllPolls();
        //dbAccess.close();

    }
}