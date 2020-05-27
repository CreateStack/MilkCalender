package com.monday2105.milkcalender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;

public class SqlHelper {

    public static class SqlOpener extends SQLiteOpenHelper {

        private static final String TAG = "SQLOpener";
        private static final String DATABASE_NAME = "myMilkDb";    // Database Name
        private static final String TABLE_NAME = "MyMilk";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String DATE="Date";     // Column I (Primary Key)
        private static final String AMOUNT = "Amount";    //Column II
        //private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+DATE+" DATE PRIMARY KEY, "+AMOUNT+" FLOAT(10));";

        SqlOpener(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "onUpgrade: called");

        }
    }

    private String TAG = "SqlHelper";
    private SQLiteDatabase dbWrite;
    private SQLiteDatabase dbRead;

    public SqlHelper(Context context){
        SqlOpener sqlOpener = new SqlOpener((context));
        dbWrite = sqlOpener.getWritableDatabase();
        dbRead = sqlOpener.getReadableDatabase();
    }

    public long insertData(String date,float amount){
        ContentValues cv = new ContentValues();
        cv.put(SqlOpener.DATE,date);
        cv.put(SqlOpener.AMOUNT,Float.toString(amount));
        long res = dbWrite.insert(SqlOpener.TABLE_NAME,null,cv);
        Log.i(TAG, "insertData: "+res);
        return res;
    }

    public int updateData(String date,float amount){
        ContentValues cv = new ContentValues();
        cv.put(SqlOpener.AMOUNT,Float.toString(amount));
        String[] whereArgs = {date};
        int res = dbWrite.update(SqlOpener.TABLE_NAME,cv, SqlOpener.DATE +" = ?",whereArgs);
        Log.i(TAG, "updateData: "+res);
        return res;
    }

    public ArrayList<String> readData(String date){
        String query = "SELECT amount FROM "+ SqlOpener.TABLE_NAME +" WHERE Date = '"+date+"';";
        Log.i(TAG, "readData: "+query);
        Cursor cursor = dbRead.rawQuery(query,null);
        ArrayList<String> data = new ArrayList<>();
        int idx = cursor.getColumnIndex("Amount");
        Log.i(TAG, "readData: idx = "+idx);
        if(cursor.moveToFirst()) {
            do {
                Log.i(TAG, "readData: " + cursor.getString(idx));
                data.add(cursor.getString(idx));
            } while (cursor.moveToNext());
            Log.i(TAG, "readData: "+data.get(0));
        }
        else Log.i(TAG, "readData: Fail");
        cursor.close();
        return data;
    }

    public int deleteData(String date){
        int res = dbWrite.delete(SqlOpener.TABLE_NAME, SqlOpener.DATE + "=?",
                new String[]{date});
        Log.i(TAG, "deleteData: "+res);
        return res;
    }

    public ArrayList<String> getMonthData(String month){
        String query = "SELECT amount FROM "+ SqlOpener.TABLE_NAME +" WHERE strftime('%m', "+ SqlOpener.DATE +") = '"+month+"';";
        Log.i(TAG, "readData: "+query);
        Cursor cursor = dbRead.rawQuery(query,null);
        ArrayList<String> data = new ArrayList<>();
        int idx = cursor.getColumnIndex("Amount");
        Log.i(TAG, "readData: idx = "+idx);
        if(cursor.moveToFirst()) {
            do {
                Log.i(TAG, "readData: " + cursor.getString(idx));
                data.add(cursor.getString(idx));
            } while (cursor.moveToNext());
            Log.i(TAG, "readData: "+data.get(0));
        }
        else Log.i(TAG, "readData: Fail");
        cursor.close();
        return data;
    }

}
