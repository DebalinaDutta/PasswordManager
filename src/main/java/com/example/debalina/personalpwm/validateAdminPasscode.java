package com.example.debalina.personalpwm;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class validateAdminPasscode extends Activity {

    String member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable window activity transition
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        // set an ENTER transition
        getWindow().setEnterTransition(new Explode());
        getWindow().setReturnTransition(new Explode());

        //       final View gv = (GridView) findViewById(R.id.gridview);

        setContentView(R.layout.activity_validate_passcode);

        //receive intent - 'member' and cache it
        member = getIntent().getStringExtra("result");

        /*
        //Setup text bar
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("     Hi " + member + ", " + "enter passcode");
        tv.setTextColor(Color.BLACK);
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_validate_passcode, menu);
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

    public void gotoListpage (View v) {
        EditText et = (EditText) findViewById(R.id.editText);
        int passcode = Integer.parseInt(et.getText().toString());
        String passcodeS = String.valueOf(passcode);

        validatePassword vp = new validatePassword();
        //validate numeric passcode with six digits length; use type 1
        if (vp.validatePWD(passcodeS, 1)) {

            if (passcode == 241407) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", member);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            } else {
                Toast.makeText(validateAdminPasscode.this, "Invalid Passcode", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(validateAdminPasscode.this, "Passcode must be of 6 digits", Toast.LENGTH_SHORT).show();
        }


    }

    public void goBack (View v) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",member);
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }

    public int getViewPassword(String member) {

        DBHandler dbhandler = new DBHandler(validateAdminPasscode.this, null, null, 1);
        AccountProfile acProf = dbhandler.getAccountProfile(member);

        String viewpassword = acProf.getviewPassword();
        Integer pcode = Integer.parseInt(viewpassword);
        return pcode;
    }

}
