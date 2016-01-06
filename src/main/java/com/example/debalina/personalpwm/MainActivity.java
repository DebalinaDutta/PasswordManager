package com.example.debalina.personalpwm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.sql.StatementEvent;


public class MainActivity extends Activity {

    ArrayList<String> list;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setAllowEnterTransitionOverlap(true);

// set an exit transition
        getWindow().setExitTransition(new Explode());
        getWindow().setReenterTransition(new Explode());

        setContentView(R.layout.activity_main);

        //        Set the ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        actionBar.setTitle(Html.fromHtml("<font color='#ff0000'>Password Locker</font>"));

        // Spinner population logic
        list = new ArrayList<String>();
        list.add("Debalina");
        list.add("Suvojit");

        Spinner spinner = (Spinner) findViewById(R.id.spin1);
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp1);

        raiseAlarm(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void Signin(View view) {

        // get member
        Spinner spinner = (Spinner) findViewById(R.id.spin1);
        String member = spinner.getSelectedItem().toString();

        //       get  ID
        EditText et1 = (EditText) findViewById(R.id.editText1);
        String userID = et1.getText().toString();
        // get pwd
        EditText et2 = (EditText) findViewById(R.id.editText2);
        String pwd = et2.getText().toString();

        String u = "admin";
        String p = "admin";

        if ((userID.equals(u)) && (pwd.equals(p))) {
            et1.setText("");
            et2.setText("");
            final Intent intent = new Intent(this, ShowGrid.class);
            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(this);
            intent.putExtra("member", member);
            startActivity(intent, option.toBundle());
        } else {
            Toast.makeText(getApplicationContext(), "invalid..", Toast.LENGTH_SHORT).show();
            et1.requestFocus();
        }
    }

    public ArrayList<String> checkifDue(ArrayList<String> list) {

        ArrayList<String> messagelist = new ArrayList<String>();

        for (int idx = 0; idx < list.size(); idx++) {
            String member = list.get(idx);
            DBHandler dbhandler = new DBHandler(MainActivity.this, null, null, 1);
            ArrayList<String> item_array = dbhandler.fetchForPWDexp(member);

            if (item_array != null) {
                for (int index = 0; index < item_array.size(); index++) {
                    String item = item_array.get(index);
                    StringTokenizer tokens = new StringTokenizer(item, ":");
                    String subject = tokens.nextToken();
                    String effdate = tokens.nextToken();
                    String days = tokens.nextToken();
                    if (Integer.valueOf(days) > 0) {
                        //get current date
                        String currdate = getCurrentDate();

                        //change to Julian
                        Gred2JulDate g2jdate = new Gred2JulDate();
                        int effdateJul = g2jdate.convertToJulian(effdate);
                        String effdateJ = String.valueOf(effdateJul);
                        int currdateJul = g2jdate.convertToJulian(currdate);
                        String currdateJ = String.valueOf(currdateJul);

                        int daydiff = calculateDateDiff(effdateJ, currdateJ);

                        int renewDays = Integer.valueOf(days);
                        int DaysLeft = renewDays - daydiff;
                        String due;
                        if (DaysLeft < 10) {
                            if (DaysLeft < 0) {
                                due = "overdue";
                            } else {
                                due = "due";
                            }
                            String message = member + " has " + "password change " + due;
                            messagelist.add(message);
                            break;
                        }
                    }
                }
            }
        }
        return messagelist;
    }

    public String getCurrentDate() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String effDate = df.format(c.getTime());
        return effDate;
    }

    public int calculateDateDiff(String effdateJ, String currdateJ) {
        int daydiff;
        if (Integer.valueOf(effdateJ.substring(0, 3)) < Integer.valueOf(currdateJ.substring(0, 3))) {
            Boolean leapyear = (Integer.valueOf(effdateJ.substring(0, 3)) % 4 == 0);
            if (leapyear) {
                daydiff = (366 - Integer.valueOf(effdateJ.substring(3, 6))) + Integer.valueOf(currdateJ.substring(3, 6));
            }else{
                daydiff = (365 - Integer.valueOf(effdateJ.substring(3, 6))) + Integer.valueOf(currdateJ.substring(3, 6));

            }
        } else {
            daydiff = Integer.valueOf(currdateJ.substring(3, 6)) - Integer.valueOf(effdateJ.substring(3, 6));
        }
        return daydiff;
    }

    public void raiseAlarm(ArrayList<String> memList) {

        ArrayList<String> messageList = checkifDue(memList);

        Calendar calendar = Calendar.getInstance();

/*        calendar.set(Calendar.MONTH, 12);
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.DAY_OF_MONTH, 19);
*/
        calendar.set(Calendar.HOUR, 2);
        calendar.set(Calendar.MINUTE, 33);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.AM);

        Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
        myIntent.putStringArrayListExtra("members", messageList);

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, myIntent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1000), pendingIntent);

    }
}