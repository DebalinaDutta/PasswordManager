package com.example.debalina.personalpwm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Debalina on 1/14/2016.
 */
public class SqliteToExcel {

    public sqlite2ExcelResponse downloadData(SQLiteDatabase db, Cursor cursor, String member) {

        String fileName = member + ".xls";
        Boolean success;
        String mesg;
        File directory;

        //check external storage state
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            directory = writeExcel(db, cursor, member, fileName);
             success = true;
            mesg = "Success";

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            success = false;
            mesg = "External storage Read Only";
            directory = null;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            success = false;
            mesg = "External storage not available";
            directory = null;
        }
        sqlite2ExcelResponse createXLresponse = new sqlite2ExcelResponse(success, mesg, fileName, directory);
        return createXLresponse;
    }

    public File writeExcel(SQLiteDatabase db, Cursor cursor, String member, String fileName) {
        //Saving file in external storage

        File directory;
        File file;
        File sdCard = Environment.getExternalStorageDirectory();
        directory = new File(sdCard.getAbsolutePath() + "/" + member + ".pwd");

        //Saving file in internal storage
        //  directory = new File(getFilesDir().getAbsolutePath() + "/" + member + ".pwd");

        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        //file path
        file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("MyMemorables", 0);

            try {
                sheet.addCell(new Label(0, 0, "Seq No.")); // column and row
                sheet.addCell(new Label(1, 0, "Partition"));
                sheet.addCell(new Label(2, 0, "Item Name"));
                sheet.addCell(new Label(3, 0, "UserID"));
                sheet.addCell(new Label(4, 0, "Password"));
                sheet.addCell(new Label(5, 0, "Secondary Sequence"));
                sheet.addCell(new Label(6, 0, "Parent ID"));
                sheet.addCell(new Label(7, 0, "Last PWD Changed"));
                sheet.addCell(new Label(8, 0, "PWD Renews Days"));

                if (cursor.moveToFirst()) {
                    cursor.moveToFirst();
                    String id = String.valueOf(cursor.getInt(cursor.getColumnIndex("_ID")));
                    String partition = cursor.getString(cursor.getColumnIndex("NAME"));
                    String itemName = cursor.getString(cursor.getColumnIndex("SUBJECT"));
                    String userID = cursor.getString(cursor.getColumnIndex("USERID"));
                    String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
                    String id1 = String.valueOf(cursor.getInt(cursor.getColumnIndex("_ID1")));
                    String parent = String.valueOf(cursor.getInt(cursor.getColumnIndex("PARENT")));
                    String effdate = cursor.getString(cursor.getColumnIndex("EFFDATE"));
                    String rDays = cursor.getString(cursor.getColumnIndex("DAYS"));

                    int i = cursor.getPosition() + 1;

                    sheet.addCell(new Label(0, i, id));
                    sheet.addCell(new Label(1, i, partition));
                    sheet.addCell(new Label(2, i, itemName));
                    sheet.addCell(new Label(3, i, userID));
                    sheet.addCell(new Label(4, i, password));
                    sheet.addCell(new Label(5, i, id1));
                    sheet.addCell(new Label(6, i, parent));
                    sheet.addCell(new Label(7, i, effdate));
                    sheet.addCell(new Label(8, i, rDays));

                    while (cursor.moveToNext()) {
                        id = String.valueOf(cursor.getInt(cursor.getColumnIndex("_ID")));
                        partition = cursor.getString(cursor.getColumnIndex("NAME"));
                        itemName = cursor.getString(cursor.getColumnIndex("SUBJECT"));
                        userID = cursor.getString(cursor.getColumnIndex("USERID"));
                        password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
                        id1 = String.valueOf(cursor.getInt(cursor.getColumnIndex("_ID1")));
                        parent = String.valueOf(cursor.getInt(cursor.getColumnIndex("PARENT")));
                        effdate = cursor.getString(cursor.getColumnIndex("EFFDATE"));
                        rDays = cursor.getString(cursor.getColumnIndex("DAYS"));

                        i = cursor.getPosition() + 1;

                        sheet.addCell(new Label(0, i, id));
                        sheet.addCell(new Label(1, i, partition));
                        sheet.addCell(new Label(2, i, itemName));
                        sheet.addCell(new Label(3, i, userID));
                        sheet.addCell(new Label(4, i, password));
                        sheet.addCell(new Label(5, i, id1));
                        sheet.addCell(new Label(6, i, parent));
                        sheet.addCell(new Label(7, i, effdate));
                        sheet.addCell(new Label(8, i, rDays));
                    }
                    cursor.close();
                }
                db.close();

                //closing cursor
                cursor.close();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directory;
    }
}
