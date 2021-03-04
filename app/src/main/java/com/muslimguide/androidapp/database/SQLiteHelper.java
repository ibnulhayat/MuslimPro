package com.muslimguide.androidapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context,String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public  void queryData(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String mDate,String zikirName,String timeCount,String timeLimit){
        SQLiteDatabase database=getWritableDatabase();
        String sql="INSERT INTO ZIKIR VALUES (NULL, ?,?,?,?)";


        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,mDate);
        statement.bindString(2,zikirName);
        statement.bindString(3,timeCount);
        statement.bindString(4,timeLimit);

        statement.executeInsert();
    }
    public void insertMdata(String mDate){
        SQLiteDatabase database=getWritableDatabase();
        String sql="INSERT INTO TBLDATE VALUES (NULL, ?)";


        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,mDate);

        statement.executeInsert();
    }
    public void updateData(String mDate, String zikirName,String timeCount,String timeLimit) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE ZIKIR SET zikirName = ?, timeCount = ?, timeLimit = ? WHERE mDate = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, mDate);
        statement.bindString(2, zikirName);
        statement.bindString(3, timeCount);
        statement.bindString(4, timeLimit);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        Log.d("Path",""+database);
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
