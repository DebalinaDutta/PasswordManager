package com.example.debalina.personalpwm;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.widget.Toast;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Debalina on 1/15/2016.
 */
public class ExcelToSqlite {

    public Excel2SqliteResponse downloadFromExceltoSqlite(String member, String FilePath, Context context) {

        Boolean success = false;
        String mesg = "";

        try {
            AssetManager am = context.getAssets();
            InputStream inStream;
            XSSFWorkbook wb = null;
            try {
                inStream = new FileInputStream(FilePath);
                wb = new XSSFWorkbook(inStream);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            XSSFSheet sheet1 = wb.getSheetAt(0);

            if (sheet1 == null) {
                success = true;
                mesg = "nothing";
            } else {
                DBHandler dbhandler = new DBHandler(context, null, null, 1);
                if (dbhandler.ExceltoSqlite(member, sheet1)) {
                    success = true;
                    mesg = "Data loaded to database successfully";
                } else {
                    mesg = "Data loading failed";
                    success = false;
                }

            }

        } catch (Exception ex) {

        }

        Excel2SqliteResponse e2sr = new Excel2SqliteResponse(success, mesg);
        return e2sr;
    }

}
