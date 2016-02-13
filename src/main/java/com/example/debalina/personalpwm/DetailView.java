package com.example.debalina.personalpwm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DetailView extends Activity {

    String member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable window content transition
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setAllowEnterTransitionOverlap(true);

// set an exit transition
        getWindow().setEnterTransition(new Fade());
        getWindow().setReturnTransition(new Fade());

        setContentView(R.layout.activity_detail_view);

        ContentData content = getIntent().getParcelableExtra("Credentials");

        String name = content.getname();
        String subject = content.getsubject();
        String userid = content.getuserid();
        String password = content.getpassword();

        member = name;

        TextView tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText(subject);
        EditText et2 = (EditText) findViewById(R.id.editText2);
        et2.setText(userid);
        EditText et3 = (EditText) findViewById(R.id.editText3);
        et3.setText(password);

        //        Set the ActionBar
 /*       ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        actionBar.setTitle(Html.fromHtml("<font color='#ff0000'>Password Locker</font>"));
*/
        //Setup text bar
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("     Change Credentials for " + member);
        tv.setTextColor(Color.BLACK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_view, menu);
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

    public void deleteProcess(View view) {

        //Show alert dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
  //
                    EditText et = (EditText) findViewById(R.id.editText1);
                        String subject = et.getText().toString();
                        DBHandler dbHandler = new DBHandler(DetailView.this, null, null, 1);
                        Boolean result = dbHandler.deleteCreds(member, subject);
                        if (result) {
                            Toast.makeText(getApplicationContext(), "Successfully deleted ", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Delete failed ", Toast.LENGTH_LONG).show();
                        }
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",member);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.alerticon)
                .show();
    }

    public void updateProcess(View view) {

        //Show alert dialog
        new AlertDialog.Builder(this)
                .setTitle("Update entry")
                .setMessage("Are you sure you want to update this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //
                        EditText et1 = (EditText) findViewById(R.id.editText1);
                        String subject = et1.getText().toString();
                        EditText et2 = (EditText) findViewById(R.id.editText2);
                        String userid = et2.getText().toString();
                        EditText et3 = (EditText) findViewById(R.id.editText3);
                        String pwd = et3.getText().toString();

                        DBHandler dbHandler = new DBHandler(DetailView.this, null, null, 1);

                        long result = dbHandler.updateCreds(member, subject, userid, pwd);
                        if (result==999999) {
                            Toast.makeText(getApplicationContext(), "Data exists", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Successfully updated ", Toast.LENGTH_LONG).show();
                        }
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",member);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.alerticon)
                .show();
    }
}
