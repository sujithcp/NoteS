package sujith.cp.notes;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by root on 7/21/15.
 */
public class Note
{

    //String title,note;
   public static final String  KEY_ROWIDAUTO="_id";
    public static final String  KEY_ROWID="number";
    public static final String  KEY_NAME="title";
    public static final String  KEY_DATE="date";
    public static final String  KEY_DATA="data";
    public static final String  DATABASE_NAME="notes";
    public static final int  DATABASE_VERSION=1;
    public static final String  DATABASE_TABLE="Notes1";
    private final Context context;
    public Note(Context context)
    {
        this.context=context;
    }


    private DbHelper Help;

    private SQLiteDatabase ourDB;



    public class DbHelper extends SQLiteOpenHelper
    {
        public DbHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+DATABASE_TABLE+" ("+KEY_ROWIDAUTO+" INTEGER PRIMARY KEY AUTOINCREMENT , "+KEY_ROWID+" INTEGER, "+KEY_NAME+" TEXT NOT NULL, "+
                    KEY_DATA+" TEXT NOT NULL, "+KEY_DATE+" TEXT NOT NULL);"
            );

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);
        }


    }

    public DbHelper open() throws SQLException
    {
        Help = new DbHelper(context);
        ourDB = Help.getWritableDatabase();
        return  Help;
    }

    public void close()
    {
        Help.close();
    }

    public void createEntry(NoteData D)
    {
        ContentValues cv = new ContentValues();
        int l = getProfilesCount();
        cv.put(KEY_ROWID, l + 1);
        cv.put(KEY_NAME,D.title);
        cv.put(KEY_DATA, D.data);
        cv.put(KEY_DATE,D.date);
        ourDB.insert(DATABASE_TABLE, null, cv);
    }
    public void editEntry(NoteData noteObj) {
        noteObj.title=DatabaseUtils.sqlEscapeString(noteObj.title);
        noteObj.data=DatabaseUtils.sqlEscapeString(noteObj.data);
        //String title= DatabaseUtils.sqlEscapeString(noteObj.data);
        //Log.v("Edit",""+noteObj.date+" "+noteObj.title+" "+noteObj.data);
        String editQuery = "UPDATE "+DATABASE_TABLE
                +" SET "+KEY_NAME+"="+noteObj.title+" ,"+KEY_DATA+"="+noteObj.data+""
                +" WHERE "+KEY_DATE+"='"+noteObj.date+"';";
        ourDB.execSQL(editQuery);
        //Log.v("sql",""+editQuery);
    }

    /*public void createEntry(String name,String dat,int i)
    {
        String[] col = ic_new String[]{KEY_ROWIDAUTO,KEY_ROWID,KEY_NAME,KEY_DATA};
        Cursor c = ourDB.query(DATABASE_TABLE, col, null, null, null, null, null);
        c.moveToPosition(i);
        int iDat=c.getColumnIndex(KEY_DATA);
        int iName=c.getColumnIndex(KEY_NAME);
        ourDB.execSQL("UPDATE " + DATABASE_TABLE + " SET " + KEY_NAME + "='" + name + "', " + KEY_DATA + "='" + dat + "' WHERE " + KEY_NAME + "='" + c.getString(iName) + "';");
    }
*/
    public DbValues getData()
    {
        //ArrayList<String> AL = ic_new ArrayList<String>();
        String[] col = new String[]{KEY_ROWIDAUTO,KEY_ROWID,KEY_NAME,KEY_DATA,KEY_DATE};
        Cursor c = ourDB.query(DATABASE_TABLE, col, null, null, null, null, null);
        int iRow=c.getColumnIndex(KEY_ROWID);
        int iName=c.getColumnIndex(KEY_NAME);
        int iData = c.getColumnIndex(KEY_DATA);
        int iDate=c.getColumnIndex(KEY_DATE);
        int i=0;
        DbValues D = new DbValues();
        for(c.moveToLast();!c.isBeforeFirst();c.moveToPrevious(),i++)
        {
            D.AL.add(i,""+c.getString(iName));
            D.Dates.add(i,c.getString(iDate));
        }

        return D;
    }

    public String getData(String date)
    {
        //String[] col = ic_new String[]{KEY_ROWIDAUTO,KEY_ROWID,KEY_NAME,KEY_DATA,KEY_DATE};
        //Cursor c = ourDB.query(DATABASE_TABLE, col, null, null, null, null, null);
        //c.moveToPosition(i);
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE+" WHERE "+KEY_DATE+" ='"+date+"';";
        Cursor cursor = ourDB.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int iDat=cursor.getColumnIndex(KEY_DATA);
        return cursor.getString(iDat);
    }
    public void delete(String date)
    {

        /*ourDB.execSQL("DELETE FROM "+DATABASE_TABLE+" WHERE "+KEY_DATE+"='"+date+"';");
        String[] col = ic_new String[]{KEY_ROWIDAUTO,KEY_ROWID,KEY_NAME,KEY_DATA};
        Cursor c = ourDB.query(DATABASE_TABLE, col, null, null, null, null, null);
        */
        Log.v("",""+date);
        String deleteQuery = "DELETE FROM "+DATABASE_TABLE+" WHERE "+KEY_DATE+"='"+date+"';";
        ourDB.execSQL(deleteQuery);
        /*
        int iRow=c.getColumnIndex(KEY_ROWID);
        int i=0;
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext())
        {
            String name=c.getString(c.getColumnIndex(KEY_NAME));
            ourDB.execSQL("UPDATE " + DATABASE_TABLE + " SET " + KEY_ROWID + "='" + i + "' WHERE " + KEY_NAME + "='" + name + "';");
        }
        int id2=Integer.parseInt(c.getString(iRow));
        ourDB.execSQL("UPDATE "+DATABASE_TABLE+" SET "+KEY_ROWID+" = "+KEY_ROWID+"-1 WHERE "+KEY_ROWID+">"+id2);
        */

    }
    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE;

        Cursor cursor = ourDB.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public void lock(String date)
    {
        //TODO

    }
    public void unLock(String date)
    {
        //TODO
    }
    public class DbValues
    {
        ArrayList<String> AL;
        ArrayList<String> Dates;
        DbValues()
        {
            AL=new ArrayList<String>();
            Dates=new ArrayList<String>();
        }
    }
}