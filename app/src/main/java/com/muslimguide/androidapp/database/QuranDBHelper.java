package com.muslimguide.androidapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class QuranDBHelper extends SQLiteOpenHelper {

    public QuranDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertSuraName(String suraNum,String arabicName,String engName,String engMeaning,String numOfAyah,String relevationType){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO SURANAME VALUES (NULL, ?,?,?,?,?,?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, suraNum);
        statement.bindString(2, arabicName);
        statement.bindString(3, engName);
        statement.bindString(4, engMeaning);
        statement.bindString(5, numOfAyah);
        statement.bindString(6, relevationType);
        statement.executeInsert();
    }

    public void insertSura(String SURANAME, String number,String arabicAyah,String engAyah,String numberInSurah,String url){
        SQLiteDatabase database=getWritableDatabase();
        String sql="INSERT INTO "+SURANAME+" VALUES (NULL,?,?,?,?,?)";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,number);
        statement.bindString(2,arabicAyah);
        statement.bindString(3,engAyah);
        statement.bindString(4,numberInSurah);
        statement.bindString(5,url);
        statement.executeInsert();
    }

    public Cursor getFullSura(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }


    public Cursor getSuraList(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
