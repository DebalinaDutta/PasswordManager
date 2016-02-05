package com.example.debalina.personalpwm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class eMailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_e_mail);

        String member = getIntent().getStringExtra("recipientAccount");
        SendEmail semail = new SendEmail();

        semail.execute(member);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_e_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SendEmail extends AsyncTask<String, Void, String> {

        // constructor
        public SendEmail() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String member = params[0];

            DBHandler dbhandler = new DBHandler(eMailActivity.this, null, null, 1);
            AccountProfile acProf = dbhandler.getAccountProfile(member);

            String sentFlag = "no";
            String userID = acProf.getuserID();
            String password = acProf.getpassword();
            String viewpassword = acProf.getviewPassword();
            String email = acProf.getemail();

            try {
                GMailSender sender = new GMailSender("suvoapp@gmail.com", "Sherlock@1");
                sender.sendMail("Password Locker credentials for Account - " + member,
                        "User ID: " + userID + ", " + "Password: " + password + ", " + "View password: " + viewpassword,
                        "suvoapp@gmail.com",
                        email);
                sentFlag = "yes";
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);

            }
            return sentFlag;
        }

        @Override
        protected void onPostExecute(String mailSent) {

            Intent returnIntent = new Intent();
            returnIntent.putExtra("returnString", mailSent);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();


        }
    }
}
