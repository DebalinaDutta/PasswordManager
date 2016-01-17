package com.example.debalina.personalpwm; /**
 * Created by Debalina on 7/13/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CredentialDB.db";
    private static final String TABLE_CREDENTIALS = "CREDENTIALS";
    private static final String TABLE_RENEWAL = "RENEWAL";

    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_ID1 = "_ID1";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SUBJECT = "SUBJECT";
    public static final String COLUMN_USERID = "USERID";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_PARENT = "PARENT";
    public static final String COLUMN_EFFDATE = "EFFDATE";
    public static final String COLUMN_DAYS = "DAYS";

    public DBHandler(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE " +
                TABLE_CREDENTIALS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
                + " TEXT, " + COLUMN_SUBJECT + " TEXT, " + COLUMN_USERID + " TEXT, " + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_CREDENTIALS_TABLE);

        String CREATE_RENEWAL_TABLE = "CREATE TABLE " +
                TABLE_RENEWAL + "(" + COLUMN_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PARENT
                + " INTEGER, " + COLUMN_EFFDATE + " TEXT, " + COLUMN_DAYS + " INTEGER" + ","
                + " FOREIGN KEY(" + COLUMN_ID1 + ")" + " REFERENCES " + TABLE_CREDENTIALS + "(" + COLUMN_ID
                + ")" + " ON DELETE CASCADE " + ")";

        db.execSQL(CREATE_RENEWAL_TABLE);
    }

    //Open database with FK on
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENEWAL);
        onCreate(db);
    }
    //insert
    public long addcreds (ContentData content) {

        SQLiteDatabase db = this.getWritableDatabase();

        // check if exists
        Boolean ifExists = false;
        String name = content.getname();
        String subject = content.getsubject();
        long result;

        ifExists = checkIfExists(db, name, subject);
        if (ifExists) {
            result = 999999;
            return result;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, content.getname());
            values.put(COLUMN_SUBJECT, content.getsubject());
            values.put(COLUMN_USERID, content.getuserid());
            values.put(COLUMN_PASSWORD, content.getpassword());

            long parentID = db.insert(TABLE_CREDENTIALS, null, values);

            db.close();
            result = parentID;
            return result;
        }
    }

    //query
    public ArrayList<ContentData> findcreds(String member) {

        String query;

//        query = "Select * FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " LIKE %" + member + "%" ;
        query = "Select * FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " =  \"" + member + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<ContentData> list = new ArrayList<ContentData>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            ContentData content = new ContentData();
            content.setname(cursor.getString(cursor.getColumnIndex("NAME")));
            content.setsubject(cursor.getString(cursor.getColumnIndex("SUBJECT")));
            content.setuserid(cursor.getString(cursor.getColumnIndex("USERID")));
            content.setpassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));

            list.add(content);

            while(cursor.moveToNext()){
                content = new ContentData();
                content.setname(cursor.getString(cursor.getColumnIndex("NAME")));
                content.setsubject(cursor.getString(cursor.getColumnIndex("SUBJECT")));
                content.setuserid(cursor.getString(cursor.getColumnIndex("USERID")));
                content.setpassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));

                list.add(content);
            }
            cursor.close();
        } else {
            list = null;
        }
        db.close();

        return (list);
    }

    public Boolean checkIfExists (SQLiteDatabase db, String name, String subject) {

        String Query = "Select * from " + TABLE_CREDENTIALS + " where " + COLUMN_NAME + " =  \"" + name + "\"" +
                " AND " + COLUMN_SUBJECT + " =  \"" + subject + "\"";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }

    }

    public Boolean checkIfExistsforUpdate (SQLiteDatabase db, String name, String subject, String userid, String pwd) {

        String Query = "Select * from " + TABLE_CREDENTIALS + " where " + COLUMN_NAME + " =  \"" + name + "\"" +
                " AND " + COLUMN_SUBJECT + " =  \"" + subject + "\"" + " AND " + COLUMN_USERID + " =  \"" + userid + "\""
                + " AND " + COLUMN_PASSWORD + " =  \"" + pwd + "\"";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }

    }

    //delete
    public boolean deleteCreds(String name, String subject) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " =  \"" + name + "\"" +
                " AND " + COLUMN_SUBJECT + " =  \"" + subject + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ContentData content = new ContentData();

        if (cursor.moveToFirst()) {
            content.setname(cursor.getString(cursor.getColumnIndex("NAME")));
            content.setsubject(cursor.getString(cursor.getColumnIndex("SUBJECT")));
            db.delete(TABLE_CREDENTIALS, COLUMN_NAME + " = ? AND " + COLUMN_SUBJECT + " = ?" ,
                    new String[]{String.valueOf(content.getname()),String.valueOf(content.getsubject())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    //update
    public long updateCreds(String name, String subject, String userid, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        // check if exists
        Boolean ifExistsforNoUpdate = false;
        long result;

        ifExistsforNoUpdate = checkIfExistsforUpdate(db, name, subject, userid, password);
        if (ifExistsforNoUpdate) {
            result = 999999;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERID, userid);
            values.put(COLUMN_PASSWORD, password);

            long parentID = db.update(TABLE_CREDENTIALS, values, COLUMN_NAME + " = ? AND " + COLUMN_SUBJECT + " = ?",
                    new String[]{String.valueOf(name), String.valueOf(subject)});
            db.close();
            result = parentID;
        }
            return result;
    }

    //insert
    public void addRenew (long parent, String effDate, int renewDays) {

        SQLiteDatabase db1 = this.getWritableDatabase();

//            long effDateMilli = Convert2Millidate(effDate);
            ContentValues values1 = new ContentValues();
            values1.put(COLUMN_PARENT, parent);
            values1.put(COLUMN_EFFDATE, effDate);
            values1.put(COLUMN_DAYS, renewDays);

            db1.insert(TABLE_RENEWAL, null, values1);

            db1.close();

    }

    //query with member only
    public ArrayList<String> fetchForPWDexp(String member) {

        String query;

//        query = "Select * FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " =  \"" + member + "\"";
          query = "SELECT A." + COLUMN_ID + "," +
                " A." + COLUMN_NAME + "," +
                " A." + COLUMN_SUBJECT + "," +
                " A." + COLUMN_USERID + "," +
                " A." + COLUMN_PASSWORD + "," +
                " B." + COLUMN_ID1 + "," +
                " B." + COLUMN_PARENT + "," +
                " B." + COLUMN_EFFDATE + "," +
                " B." + COLUMN_DAYS + " FROM "
                + TABLE_CREDENTIALS + " A, " + TABLE_RENEWAL + " B " + " WHERE A." +
                COLUMN_NAME + " =  \"" + member + "\"" + " AND A." + COLUMN_ID + " = B." + COLUMN_PARENT +
                " AND B." + COLUMN_ID1 + " = (SELECT MAX(B." + COLUMN_ID1 + ") FROM " + TABLE_RENEWAL  +
                " B WHERE A." + COLUMN_ID + " = B." + COLUMN_PARENT + ")";

          SQLiteDatabase db = this.getWritableDatabase();

            ArrayList<String> itemlist = new ArrayList<String>();
          Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("_ID"));
            String partition = cursor.getString(cursor.getColumnIndex("NAME"));
            String item1 = cursor.getString(cursor.getColumnIndex("SUBJECT"));
            String userID  = cursor.getString(cursor.getColumnIndex("USERID"));
            String password  = cursor.getString(cursor.getColumnIndex("PASSWORD"));
            int id1 = cursor.getInt(cursor.getColumnIndex("_ID1"));
            int parent = cursor.getInt(cursor.getColumnIndex("PARENT"));
            String item2 = cursor.getString(cursor.getColumnIndex("EFFDATE"));
//            int item2_int = cursor.getInt(cursor.getColumnIndex("EFFDATE"));
//            String item2 = ConvertMilli2String(item2_int);
            String item3 = cursor.getString(cursor.getColumnIndex("DAYS"));
            String item = item1 + ":" + item2 + ":" + item3;

            itemlist.add(item);

            while(cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("_ID"));
                partition = cursor.getString(cursor.getColumnIndex("NAME"));
                item1 = cursor.getString(cursor.getColumnIndex("SUBJECT"));
                userID  = cursor.getString(cursor.getColumnIndex("USERID"));
                password  = cursor.getString(cursor.getColumnIndex("PASSWORD"));
                id1 = cursor.getInt(cursor.getColumnIndex("_ID1"));
                parent = cursor.getInt(cursor.getColumnIndex("PARENT"));
                item2 = cursor.getString(cursor.getColumnIndex("EFFDATE"));
//                item2_int = cursor.getInt(cursor.getColumnIndex("EFFDATE"));
//                item2 = ConvertMilli2String(item2_int);
                item3 = cursor.getString(cursor.getColumnIndex("DAYS"));
                item = item1 + ":" + item2 + ":" + item3;
                itemlist.add(item);
            }
            cursor.close();
        } else {
            itemlist = null;
        }
        db.close();

        return (itemlist);
    }

    //query with name and subject
    public ArrayList<ContentData> findcredsWithSub(String member, String subject) {

        String query;

//        query = "Select * FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " LIKE %" + member + "%" ;
        query = "Select * FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " =  \"" + member + "\""
                + " AND " + COLUMN_SUBJECT + " =  \"" + subject + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<ContentData> list = new ArrayList<ContentData>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            ContentData content = new ContentData();
            content.setname(cursor.getString(cursor.getColumnIndex("NAME")));
            content.setsubject(cursor.getString(cursor.getColumnIndex("SUBJECT")));
            content.setuserid(cursor.getString(cursor.getColumnIndex("USERID")));
            content.setpassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));

            list.add(content);

            while(cursor.moveToNext()){
                content = new ContentData();
                content.setname(cursor.getString(cursor.getColumnIndex("NAME")));
                content.setsubject(cursor.getString(cursor.getColumnIndex("SUBJECT")));
                content.setuserid(cursor.getString(cursor.getColumnIndex("USERID")));
                content.setpassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));

                list.add(content);
            }
            cursor.close();
        } else {
            list = null;
        }
        db.close();

        return (list);
    }

    private long Convert2Millidate(String effDate) {
        long MilliDate = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(effDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            MilliDate = date.getTime();
            return MilliDate;
        }
    public String ConvertMilli2String(int milliSeconds) {
        String effDate = "";
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        effDate = formatter.format(calendar.getTime());

        return effDate;
    }

    public int getParent(String member, String subject, String userID, String pwd) {
        int parent = 0;
        String query;
        query = "Select " + COLUMN_ID + " FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " =  \"" + member + "\""
                + " AND " + COLUMN_SUBJECT + " =  \"" + subject + "\""
                + " AND " + COLUMN_USERID + " =  \"" + userID + "\""
                + " AND " + COLUMN_SUBJECT + " =  \"" + pwd + "\"" ;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            parent = cursor.getInt(cursor.getColumnIndex("_ID"));
        }
        return parent;
    }

    public int getRenewDays(long parent, String effDate) {
        int id1 = 0;
        int renewDays = 0;
        String query;
        query = "Select " + COLUMN_ID1 + ", " + COLUMN_DAYS + " FROM " + TABLE_RENEWAL + " WHERE "
                + COLUMN_PARENT + " =  \"" + parent + "\""
                + " AND " + COLUMN_EFFDATE + " =  \"" + effDate + "\""
                + " ORDER BY " + COLUMN_ID1 + " DESC" ;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            id1 = cursor.getInt(cursor.getColumnIndex("_ID1"));
            renewDays = cursor.getInt(cursor.getColumnIndex("DAYS"));
        }

        return renewDays;
    }

    public ExcelDBCursor fetchDataforDownload(String member) {

            String query1;
        ExcelDBCursor eXdBCrsr = new ExcelDBCursor();

//        query = "Select * FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " =  \"" + member + "\"";
            query1 = "SELECT A." + COLUMN_ID + "," +
                    " A." + COLUMN_NAME + "," +
                    " A." + COLUMN_SUBJECT + "," +
                    " A." + COLUMN_USERID + "," +
                    " A." + COLUMN_PASSWORD + "," +
                    " B." + COLUMN_ID1 + "," +
                    " B." + COLUMN_PARENT + "," +
                    " B." + COLUMN_EFFDATE + "," +
                    " B." + COLUMN_DAYS + " FROM "
                    + TABLE_CREDENTIALS + " A, " + TABLE_RENEWAL + " B " + " WHERE A." +
                    COLUMN_NAME + " =  \"" + member + "\"" + " AND A." + COLUMN_ID + " = B." + COLUMN_PARENT +
                    " AND B." + COLUMN_ID1 + " = (SELECT MAX(B." + COLUMN_ID1 + ") FROM " + TABLE_RENEWAL  +
                    " B WHERE A." + COLUMN_ID + " = B." + COLUMN_PARENT + ")" +
                    " ORDER BY " + COLUMN_ID + ", " + COLUMN_ID1;

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor1 = db.rawQuery(query1, null);

            eXdBCrsr.setdatabase(db);
            eXdBCrsr.setcursor(cursor1);

            return eXdBCrsr;
    }

    public Boolean ExceltoSqlite(String member, XSSFSheet sheet) {

        Boolean status = false;
        SQLiteDatabase db1 = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_CREDENTIALS + " WHERE " +
                COLUMN_NAME + " =  \"" + member + "\"";

        db1.execSQL(query);

        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();

        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();

            row.getCell(0, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(1, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(2, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(3, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(4, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(5, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(6, Row.CREATE_NULL_AS_BLANK).setCellType(Cell.CELL_TYPE_STRING);

            contentValues.put(COLUMN_NAME, row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(COLUMN_SUBJECT, row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(COLUMN_USERID, row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(COLUMN_PASSWORD, row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());

            db1.insert(TABLE_CREDENTIALS, null, contentValues);

            contentValues1.put(COLUMN_PARENT, row.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues1.put(COLUMN_EFFDATE, row.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue());

            if (row.getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue().equals(0)) {
                contentValues1.put(COLUMN_DAYS, 888888);
            }else{
                contentValues1.put(COLUMN_DAYS, row.getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
            }

            db1.insert(TABLE_RENEWAL, null, contentValues1);

        }
        db1.close();
        status = true;
        return status;
    }

    //query with member only
    public ArrayList<TableData> fetchTableData(String member) {

        String query;

//        query = "Select * FROM " + TABLE_CREDENTIALS + " WHERE " + COLUMN_NAME + " =  \"" + member + "\"";
        query = "SELECT A." + COLUMN_ID + "," +
                " A." + COLUMN_NAME + "," +
                " A." + COLUMN_SUBJECT + "," +
                " A." + COLUMN_USERID + "," +
                " A." + COLUMN_PASSWORD + "," +
                " B." + COLUMN_ID1 + "," +
                " B." + COLUMN_PARENT + "," +
                " B." + COLUMN_EFFDATE + "," +
                " B." + COLUMN_DAYS + " FROM "
                + TABLE_CREDENTIALS + " A, " + TABLE_RENEWAL + " B " + " WHERE A." +
                COLUMN_NAME + " =  \"" + member + "\"" + " AND A." + COLUMN_ID + " = B." + COLUMN_PARENT
                + " ORDER BY " + "A." + COLUMN_ID + ","
                + " A." + COLUMN_NAME + ","
                + " A." + COLUMN_SUBJECT + ","
                + " A." + COLUMN_USERID + ","
                + " A." + COLUMN_PASSWORD + ","
                + " B." + COLUMN_ID1 + ","
                + " B." + COLUMN_PARENT + ","
                + " B." + COLUMN_EFFDATE + ","
                + " B." + COLUMN_DAYS
        ;


        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<TableData> tabledata = new ArrayList<TableData>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("_ID"));
            String name = cursor.getString(cursor.getColumnIndex("NAME"));
            String subject = cursor.getString(cursor.getColumnIndex("SUBJECT"));
            String userID  = cursor.getString(cursor.getColumnIndex("USERID"));
            String password  = cursor.getString(cursor.getColumnIndex("PASSWORD"));
            int id1 = cursor.getInt(cursor.getColumnIndex("_ID1"));
            int parent = cursor.getInt(cursor.getColumnIndex("PARENT"));
            String effdate = cursor.getString(cursor.getColumnIndex("EFFDATE"));
            int days = cursor.getInt(cursor.getColumnIndex("DAYS"));

            TableData td = new TableData(id, name, subject, userID, password, id1, parent,
                    effdate, days);

            tabledata.add(td);

            while(cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("_ID"));
                name = cursor.getString(cursor.getColumnIndex("NAME"));
                subject = cursor.getString(cursor.getColumnIndex("SUBJECT"));
                userID = cursor.getString(cursor.getColumnIndex("USERID"));
                password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
                id1 = cursor.getInt(cursor.getColumnIndex("_ID1"));
                parent = cursor.getInt(cursor.getColumnIndex("PARENT"));
                effdate = cursor.getString(cursor.getColumnIndex("EFFDATE"));
                days = cursor.getInt(cursor.getColumnIndex("DAYS"));

                td = new TableData(id, name, subject, userID, password, id1, parent,
                        effdate, days);

                tabledata.add(td);
            }
            cursor.close();
        } else {
            tabledata = null;
        }
        db.close();

        return tabledata;
    }
    }