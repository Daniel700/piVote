package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.util.Pair;
import android.util.Log;

import model.Poll;

/**
 * Created by Daniel on 22.08.2015.
 */
public class SQLiteAccess extends SQLiteOpenHelper {

    // Database Name
    public static String DATABASE_NAME = "internalDB";

    // Current version of database
    private static final int DATABASE_VERSION = 1;

    // Name of table
    private static final String TABLE_POLLS = "votedPolls";

    // All Columns of the table
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_POLLID = "PollId";
    private static final String COLUMN_ANSWERTEXT = "AnswerText";
    private static final String COLUMN_VOTEDATE = "VoteDate";


    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_POLLS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_POLLID + " INTEGER NOT NULL, " +
            COLUMN_ANSWERTEXT + " TEXT NOT NULL, " +
            COLUMN_VOTEDATE + " TEXT NOT NULL);";


    public SQLiteAccess(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("SQLITE", "Table created");
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLLS);
        onCreate(db);
    }


    public void insertPoll(Poll poll, String answerText){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_POLLID, poll.getId());
        values.put(COLUMN_ANSWERTEXT, answerText);
        values.put(COLUMN_VOTEDATE, poll.getLastVoted().toString());

        if (findPoll(poll).first){
            db.update(TABLE_POLLS, values, COLUMN_POLLID + " = " + poll.getId(), null);
            return;
        }

        db.insert(TABLE_POLLS, null, values);
        db.close();
    }



    public Pair<Boolean, String> findPoll(Poll poll){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_POLLS + " WHERE " + COLUMN_POLLID + " = " + poll.getId();

        String[] columns = {COLUMN_ID, COLUMN_POLLID, COLUMN_ANSWERTEXT, COLUMN_VOTEDATE};

        //ToDo: fix QUERY
       // Cursor c = db.rawQuery(selectQuery, null);
        Cursor c = db.query(TABLE_POLLS, columns, COLUMN_POLLID + " = " + poll.getId(), null, null, null, null, null);
        if (c!= null && c.getCount() > 0){
            Log.e("AnswerSQL", c.getString(2));
            Pair<Boolean, String> tmp = Pair.create(true, "Hulk");
            c.close();
            return tmp;

        }

        return Pair.create(false, " ");
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
        Log.e("CursorSize", String.valueOf(cursor.getCount()));
        while (!cursor.isAfterLast()) {
            Log.e("PRINT POLLS", COLUMN_ID + "  " + cursor.getString(0) + "     " +
                                 COLUMN_POLLID + "  " + cursor.getString(1) + "     " +
                                 COLUMN_ANSWERTEXT + "  " + cursor.getString(2) + "     " +
                                 COLUMN_VOTEDATE + "  " + cursor.getString(3));
            cursor.moveToNext();
        }

        cursor.close();
    }


}
