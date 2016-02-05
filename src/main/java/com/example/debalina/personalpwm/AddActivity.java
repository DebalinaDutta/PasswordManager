package com.example.debalina.personalpwm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddActivity extends Activity {

    String member;
    String flag = "n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable window content transition
//          getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        // set an exit transition
        //       getWindow().setEnterTransition(new Explode());

//        getWindow().setAllowEnterTransitionOverlap(true);

        setContentView(R.layout.activity_add);

        //receive intent
        member = getIntent().getStringExtra("member");

        //        Set the ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        actionBar.setTitle(Html.fromHtml("<font color='#ff0000'>Password Locker</font>"));

        //Setup text bar
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("        Add Credential for " + member);
        tv.setTextColor(Color.BLACK);

        ImageView image = (ImageView) findViewById(R.id.icon1);
        image.setImageResource(R.drawable.add);

        CheckBox ch1 = (CheckBox) findViewById(R.id.checkBox1);
        ch1.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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

        if (id == android.R.id.home) {
            Intent intent = new Intent(this, ShowGrid.class);
            intent.putExtra("member", member);
            startActivity(intent);
/*            AddActivity activity = new AddActivity();
            activity.finishAfterTransition();*/
        }

        return super.onOptionsItemSelected(item);
    }

    public void AddProc(View view) {
        EditText et1 = (EditText) findViewById(R.id.editText1);
        String subject = et1.getText().toString();

        EditText et2 = (EditText) findViewById(R.id.editText2);
        String userid = et2.getText().toString();

        EditText et3 = (EditText) findViewById(R.id.editText3);
        String password = et3.getText().toString();

        EditText et4 = (EditText) findViewById(R.id.editText4);
        String renewDaysS = et4.getText().toString();
        Integer renewDays = Integer.parseInt(renewDaysS);

        Boolean ifNotAllinputgiven = false;
        ifNotAllinputgiven = CheckForAllInput(subject, userid, password);

        if (ifNotAllinputgiven) {
            Toast.makeText(getApplicationContext(), "Enter all required input", Toast.LENGTH_SHORT).show();
        } else {

            ContentData content = new ContentData(member, subject, userid, password);

            DBHandler dbhandler = new DBHandler(this, null, null, 1);

            long parent = dbhandler.addcreds(content);

            if (parent == 999999) {
                Toast.makeText(getApplicationContext(), "Already exists, do you want to update", Toast.LENGTH_SHORT).show();
                et1.requestFocus();
            } else {
                //initialize scren fields
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("0");

                //insert RENEW table

                //get current date
                String effDate = getCurrentDate();

                int renewDays_num = 0;
                if (renewDays == 0) {
                    renewDays_num = 888888;
                }else{
                    renewDays_num = renewDays;
                }

                dbhandler.addRenew(parent, effDate, renewDays_num);

                Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                et1.requestFocus();
            }
        }
    }

    public void UpdateProc(View view) {
        //Show alert dialog
        new AlertDialog.Builder(this)
                .setTitle("Update entry")
                .setMessage("Are you sure you want to update this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with update
                                //
                                EditText et1 = (EditText) findViewById(R.id.editText1);
                                String subject = et1.getText().toString();
                                EditText et2 = (EditText) findViewById(R.id.editText2);
                                String userid = et2.getText().toString();
                                EditText et3 = (EditText) findViewById(R.id.editText3);
                                String pwd = et3.getText().toString();
                                EditText et4 = (EditText) findViewById(R.id.editText4);
                                String renewDaysS = et4.getText().toString();
                                int renewDays = Integer.parseInt(renewDaysS);

                                Boolean retainRenewDays = checkIfChecked();

                                DBHandler dbHandler;
                                Boolean ifNotAllinputgiven = false;
                                ifNotAllinputgiven = CheckForAllInput(subject, userid, pwd);

                                if (ifNotAllinputgiven) {
                                    Toast.makeText(getApplicationContext(), "Enter all required input", Toast.LENGTH_SHORT).show();
                                } else {
                                    dbHandler = new DBHandler(AddActivity.this, null, null, 1);

                                    long result = dbHandler.updateCreds(member, subject, userid, pwd);
                                    if (result == 999999) {
                                        Toast.makeText(getApplicationContext(), "USer id & Password exist", Toast.LENGTH_LONG).show();
                                    } else {
                                        String effDate = getCurrentDate();
                                        int parent = dbHandler.getParent(member, subject, userid, pwd);

                                        int effect_renewDays = dbHandler.getRenewDays(parent, effDate);
                                        if (!retainRenewDays) {
                                            if (renewDays == 0) {
                                                effect_renewDays = 888888;
                                            }else{
                                                effect_renewDays = renewDays;
                                            }
                                        }
                                        dbHandler.addRenew(parent, effDate, effect_renewDays);
                                        Toast.makeText(getApplicationContext(), "Successfully updated ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                    )


                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }

                    )
                            .

                    setIcon(R.drawable.alerticon)

                    .

                    show();


    }

    public Boolean CheckForAllInput(String subject, String userid, String password) {
        Character emptySubject = 'y';
        Character emptyUserid = 'y';
        Character emptyPassword = 'y';

        if (subject != null && !subject.isEmpty() && !subject.trim().isEmpty()) {
            emptySubject = 'n';
        }
        if (userid != null && !userid.isEmpty() && !userid.trim().isEmpty()) {
            emptyUserid = 'n';
        }
        if (password != null && !password.isEmpty() && !password.trim().isEmpty()) {
            emptyPassword = 'n';
        }
        if ((emptySubject == 'y') || (emptyUserid == 'y') || (emptyPassword == 'y')) {

            return true;
        }
        return false;
    }

    public String getCurrentDate() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String effDate = df.format(c.getTime());
        return effDate;
    }

    public boolean checkIfChecked() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked())
                    flag = "y";
                else
                    flag = "n";
            }
        });
        if (flag.equals("y")) {
            return true;
        }else{
            return false;
        }

    }
}