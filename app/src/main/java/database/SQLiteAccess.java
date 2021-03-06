package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.util.Pair;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Poll;

/**
 * This class creates a SQLite Database and the Tables for recently voted Polls and favorite Polls.
 * Also provides the corresponding methods to access these Tables.
 * Created by Daniel on 22.08.2015.
 */
public class SQLiteAccess extends SQLiteOpenHelper {

    // Database Name
    public static String DATABASE_NAME = "internalDB";
    // Current version of database
    private static final int DATABASE_VERSION = 1;

    // Table for recently voted Polls
    private static final String TABLE_POLLS = "votedPolls";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_POLLID = "PollId";
    private static final String COLUMN_ANSWERTEXT = "AnswerText";
    private static final String COLUMN_VOTEDATE = "VoteDate";
    private static final String COLUMN_POLLCREATIONDATE = "PollCreationDate";
    private static final String[] allColumns = {COLUMN_ID, COLUMN_POLLID, COLUMN_ANSWERTEXT, COLUMN_VOTEDATE, COLUMN_POLLCREATIONDATE};

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_POLLS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_POLLID + " INTEGER NOT NULL, " +
            COLUMN_ANSWERTEXT + " TEXT NOT NULL, " +
            COLUMN_VOTEDATE + " TEXT NOT NULL, " +
            COLUMN_POLLCREATIONDATE + " TEXT NOT NULL);";

    // Table for favorite Polls
    private static final String TABLE_FAVORITES = "myFavorites";
    private static final String FAV_COLUMN_ID = "Id";
    private static final String FAV_COLUMN_POLLID = "PollId";

    private static final String CREATE_FAV_TABLE =
            "CREATE TABLE " + TABLE_FAVORITES + "(" +
            FAV_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FAV_COLUMN_POLLID + " INTEGER NOT NULL);";


    public SQLiteAccess(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("SQLITE", "Table created");
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }



    //##############################################################################################
    //                            Methods for favorite Polls Table
    //##############################################################################################


    public void insertFavoritePoll(Poll poll){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAV_COLUMN_POLLID, poll.getId());

        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }


    public void deleteFavoritePoll(Poll poll){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, FAV_COLUMN_POLLID + " = " + poll.getId(), null);
        db.close();
    }


    public void deleteAllFavoritePolls(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, null, null);
        db.close();
    }


    public List<Long> getAllFavoritePolls(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Long> favoriteIDs = new ArrayList<Long>();

        String[] tmp = {FAV_COLUMN_ID, FAV_COLUMN_POLLID};
        Cursor cursor = db.query(TABLE_FAVORITES, tmp, null, null, null, null, FAV_COLUMN_ID + " DESC", null);

        cursor.moveToFirst();
        Log.e("PRINT FAVORITE POLLS", " Cursor Size: " + String.valueOf(cursor.getCount()));
        while (!cursor.isAfterLast()) {
            Log.e("PRINT FAVORITE POLLS", FAV_COLUMN_ID + "  " + cursor.getString(0) + "     " +
                    FAV_COLUMN_POLLID + "  " + cursor.getString(1));
            favoriteIDs.add(cursor.getLong(1));
            cursor.moveToNext();
        }
        cursor.close();

        return favoriteIDs;
    }


    public boolean findFavoritePoll(Poll poll){
        SQLiteDatabase db = this.getReadableDatabase();

         String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES + " WHERE " + FAV_COLUMN_POLLID + " = " + poll.getId();
         Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0){
            c.moveToFirst();
            c.close();
            return true;
        }

        return false;
    }



    //##############################################################################################
    //                            Methods for recently voted Polls Table
    //##############################################################################################


    /**
     * Method is used in PollDetailedActivity when a vote is submitted.
     * In that case the vote has to be saved locally.
     * @param poll = poll on that has been voted
     * @param answerText = selected answer
     */
    public void insertPoll(Poll poll, String answerText){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_POLLID, poll.getId());
        values.put(COLUMN_ANSWERTEXT, answerText);
        values.put(COLUMN_VOTEDATE, poll.getLastVoted().toString());
        values.put(COLUMN_POLLCREATIONDATE, poll.getCreationDate().toString());

        //if poll already in database overwrite this poll
        if (findPoll(poll).first){
            db.update(TABLE_POLLS, values, COLUMN_POLLID + " = " + poll.getId(), null);
            return;
        }

        db.insert(TABLE_POLLS, null, values);
        db.close();
    }


    /**
     * Method is used in Adapter to mark the question as voted and set selected answer
     * @param poll = the poll that has to be checked if existent in SQLite
     * @return true if poll was found and voted, false otherwise
     */
    public Pair<Boolean, String> findPoll(Poll poll){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor curs = db.query(TABLE_POLLS, allColumns, COLUMN_POLLID + " = " + poll.getId(), null, null, null, null, null);

        //Poll is in Database
        if (curs.getCount() > 0){
            curs.moveToFirst();
            //Check if poll in SQLite db is the same as in remote DB via comparison of creation Date
            if (poll.getCreationDate().toString().equals(curs.getString(4)))
            {
                Pair<Boolean, String> tmp = Pair.create(true, curs.getString(2));
                curs.close();
                return tmp;
            }
            //otherwise the poll in SQLite is an outdated version and not used anymore in the remote DB
            //this means the poll in the remote DB had been temporarily deleted and then created a new one with the same id as the old one
            else
            {
                deleteEntry(curs.getInt(0));
            }

        }

        return Pair.create(false, " ");
    }


    public List<Long> getRecentPolls(){
        List<Long> recentIDs = new ArrayList<Long>();
        final String LIMIT = "75";

        SQLiteDatabase db = this.getReadableDatabase();

        String[] tmp = {COLUMN_ID, COLUMN_POLLID};
        Cursor cursor = db.query(TABLE_POLLS, tmp, null, null, null, null, COLUMN_ID + " DESC", LIMIT);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            recentIDs.add(cursor.getLong(1));
            cursor.moveToNext();
        }
        cursor.close();

        return recentIDs;
    }

    public void deleteEntry(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_POLLS, COLUMN_ID + " = " + id, null);
        db.close();
    }

    public void printAllPolls(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_POLLS;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Log.e("PRINT POLLS", " Cursor Size: " + String.valueOf(cursor.getCount()));
        while (!cursor.isAfterLast()) {
            Log.e("PRINT POLLS", COLUMN_ID + "  " + cursor.getString(0) + "     " +
                                 COLUMN_POLLID + "  " + cursor.getString(1) + "     " +
                                 COLUMN_ANSWERTEXT + "  " + cursor.getString(2) + "     " +
                                 COLUMN_VOTEDATE + "  " + cursor.getString(3) + "   " +
                                 COLUMN_POLLCREATIONDATE + " " + cursor.getString(4));
            cursor.moveToNext();
        }

        cursor.close();
    }


}
