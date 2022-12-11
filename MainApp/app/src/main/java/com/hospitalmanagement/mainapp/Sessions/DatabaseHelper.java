package com.hospitalmanagement.mainapp.Sessions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "hospital.db";
    public static final String TABLENAME = "family";
    public static final String IDS = "id";
    public static final String MEMBERDETAIL = "member";
    public static final String COUPLEDETAIL = "couple";
    public static final String PREGNANTDETAIL = "pregnant";
    public static final String CHILDRENDETAIL = "children";
    SQLiteDatabase database = this.getWritableDatabase();

    Context cont;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
        cont = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLENAME + " (id integer, member text, couple text, pregnant " +
                "text, children text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists family");
        onCreate(db);
    }

    public void deleteAllData() {
        database.execSQL("DELETE FROM " + TABLENAME);
        database.execSQL("VACUUM");
    }

    public int getMemberCount() {
        Cursor cursor = database.rawQuery("select * from family", null);
        return cursor.getCount();
    }

    public String getMemberDetails() {
        Cursor cursor = database.rawQuery("select * from family where id = 100", null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public boolean saveMemberDetails(String member) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IDS, 100);
        contentValues.put(MEMBERDETAIL, member);
        Cursor cursor = database.rawQuery("select * from family where id = 100", null);
        if (cursor.getCount() > 0) {
            database.update(TABLENAME, contentValues, "id = ? ",
                    new String[] {Integer.toString(100)});
            return true;
        }
        database.insert(TABLENAME, null, contentValues);
        return true;
    }

    public boolean savePregnantDetails(String pregnant) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IDS, 100);
        contentValues.put(PREGNANTDETAIL, pregnant);
        Cursor cursor = database.rawQuery("select * from family where id = 100", null);
        if (cursor.getCount() > 0) {
            database.update(TABLENAME, contentValues, "id = ?",
                    new String[] {Integer.toString(100)});
            return true;
        }
        database.insert(TABLENAME, null, contentValues);
        return true;
    }

    public boolean saveChildDetails(String pregnant) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IDS, 100);
        contentValues.put(CHILDRENDETAIL, pregnant);
        Cursor cursor = database.rawQuery("select * from family where id = 100", null);
        if (cursor.getCount() > 0) {
            database.update(TABLENAME, contentValues, "id = ?",
                    new String[] {Integer.toString(100)});
            return true;
        }
        database.insert(TABLENAME, null, contentValues);
        return true;
    }

    public String getChildDetails() {
        Cursor cursor = database.rawQuery("select * from family where id = 100", null);
        cursor.moveToFirst();
        return cursor.getString(4);
    }

    public int getChildCount() {
        Cursor cursor = database.rawQuery("select * from family", null);
        return cursor.getCount();
    }

    public String getCoupleDetails() {
        Cursor cursor = database.rawQuery("select * from family where id = 100", null);
        cursor.moveToFirst();
        return cursor.getString(2);
    }

    public String getPregnantDetails() {
        Cursor cursor = database.rawQuery("select * from family where id = 100", null);
        cursor.moveToFirst();
        return cursor.getString(3);
    }

    public int getPregnantCount() {
        Cursor cursor = database.rawQuery("select * from family", null);
        return cursor.getCount();
    }

    public int getCoupleCount() {
        Cursor cursor = database.rawQuery("select * from family", null);
        return cursor.getCount();
    }

    public boolean saveCoupleDetails(String couple) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IDS, 100);
        contentValues.put(COUPLEDETAIL, couple);
        Cursor cursor = database.rawQuery("select * from family where id = 100", null);
        if (cursor.getCount() > 0) {
            database.update(TABLENAME, contentValues, "id = ? ",
                    new String[] {Integer.toString(100)});
            return true;
        }
        database.insert(TABLENAME, null, contentValues);
        return true;
    }
}
