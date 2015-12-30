package com.example.debalina.personalpwm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Debalina on 8/30/2015.
 */
public class ExcelDBCursor {

    private SQLiteDatabase _database;
    private Cursor _cursor;

    public ExcelDBCursor() {

    }

    public ExcelDBCursor(SQLiteDatabase database, Cursor cursor) {

        this._database = database;
        this._cursor = cursor;
    }

    public void setdatabase(SQLiteDatabase database) {

        this._database = database;
    }
    public void setcursor(Cursor cursor) {

        this._cursor = cursor;
    }

    public SQLiteDatabase getdatabase() {

        return this._database;
    }

    public Cursor getcursor() {

        return this._cursor;
    }
}
