package com.example.debalina.personalpwm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AdminActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable window content transition
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setAllowEnterTransitionOverlap(true);

        // set an ENTER transition
/*        getWindow().setEnterTransition(new Explode());
        getWindow().setReturnTransition(new Explode());  */

        getWindow().setEnterTransition(new Fade());
        getWindow().setReturnTransition(new Fade());

        setContentView(R.layout.activity_admin);

        final EditText psw = (EditText) findViewById(R.id.textView3);

        final TextView str = (TextView) findViewById(R.id.tv);
        str.setVisibility(View.INVISIBLE);

        final ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        psw.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                String pss = psw.getText().toString();
                if (pss.length() != 0) {
                    PasswordStrength ps = new PasswordStrength();
                    String strength = ps.CalPasswordStrength(pss);

                    if (strength.equals("invalid")) {
                        str.setVisibility(View.VISIBLE);
                        str.setText("Invalid");
                        pb.setVisibility(View.VISIBLE);
                        pb.setProgress(50);
                    }else if (strength.equals("valid")){
                        str.setVisibility(View.VISIBLE);
                        str.setText("Valid");
                        pb.setVisibility(View.VISIBLE);
                        pb.setProgress(100);
                    } else {
                        pb.setVisibility(View.INVISIBLE);
                        str.setVisibility(View.INVISIBLE);
                    }

                }else{
                    pb.setVisibility(View.INVISIBLE);
                    str.setVisibility(View.INVISIBLE);
                }

            }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
        int after) {
            // TODO Auto-generated method stub

        }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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

    public void cancelSave (View view) {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void saveProcess (View view) {

        Boolean error_flag = false;
        String member = "";
        String userID = "";
        String password = "";
        String Vpassword = "";
        String email = "";

        EditText et1 = (EditText) findViewById(R.id.textView1);
        member = et1.getText().toString();
        if ((member.equals(null)) || (member.isEmpty()) || (member.trim().isEmpty())) {
            et1.requestFocus();
            error_flag = true;
            Toast.makeText(getApplicationContext(), "Enter profile", Toast.LENGTH_LONG).show();
        }
        if (error_flag == false) {
            EditText et2 = (EditText) findViewById(R.id.textView2);
            userID = et2.getText().toString();
            if ((userID.equals(null)) || (userID.isEmpty()) || (userID.trim().isEmpty())) {
                Toast.makeText(getApplicationContext(), "Enter User ID", Toast.LENGTH_LONG).show();
                et2.requestFocus();
                error_flag = true;
            }
        }
        if (error_flag == false) {
            EditText et3 = (EditText) findViewById(R.id.textView3);
            password = et3.getText().toString();
            if ((password.equals(null)) || (password.isEmpty()) || (password.trim().isEmpty())) {
                Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_LONG).show();
                et3.requestFocus();
                error_flag = true;
            } else {
                validatePassword vp = new validatePassword();
                //validate passcode with six digits length, Uppercase & lowercase letter, number and special char; use type 2
                if (!vp.validatePWD(password, 2)) {
                    Toast.makeText(getApplicationContext(), "Password must be 6 character long with atleast one " +
                            "alphabet in upper case, one in lower case, one number and one special character",
                            Toast.LENGTH_LONG).show();
                    error_flag = true;
                    et3.requestFocus();

                }
            }

        }
        if (error_flag == false) {
            EditText et4 = (EditText) findViewById(R.id.textView4);
            Vpassword = et4.getText().toString();
            if ((Vpassword.equals(null)) || (Vpassword.isEmpty()) || (Vpassword.trim().isEmpty())) {
                Toast.makeText(getApplicationContext(), "Enter view password", Toast.LENGTH_LONG).show();
                et4.requestFocus();
                error_flag = true;
            }else {
                validatePassword vp = new validatePassword();
                //validate numeric passcode with six digits length; use type 1
                if (!vp.validatePWD(Vpassword, 1)) {
                    Toast.makeText(getApplicationContext(), "View password invalid; must be 6 digits", Toast.LENGTH_LONG).show();
                    error_flag = true;
                    et4.requestFocus();
                }

                }
            }


        if (error_flag == false) {
            EditText et5 = (EditText) findViewById(R.id.textView5);
            email = et5.getText().toString();
            email = email.trim();
//            email.replaceFirst("\\s+$", "");
            if ((email.equals(null)) || (email.isEmpty()) || (email.trim().isEmpty())) {
                Toast.makeText(getApplicationContext(), "Enter email ID", Toast.LENGTH_LONG).show();
                et5.requestFocus();
                error_flag = true;
            }else {
                if (!emailIsValid(email)) {
                    Toast.makeText(getApplicationContext(), "Invalid email Id", Toast.LENGTH_LONG).show();
                    error_flag = true;
                    et5.requestFocus();
                }

            }
        }

//save data in database via AccountProfile object
        if (error_flag == false) {

            AccountProfile accountProfile = new AccountProfile(member, userID, password, Vpassword, email);

            DBHandler dbhandler = new DBHandler(this, null, null, 1);
            String operation_flag = dbhandler.saveAdmin(accountProfile);

            if (operation_flag == "update") {
                Toast.makeText(getApplicationContext(), "Existing profile updated", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "New profile created", Toast.LENGTH_LONG).show();
            }
        }

/*        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish(); */

        }

    private Boolean emailIsValid(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public void deleteProfile (View view) {

        //Show alert dialog
        final String member;
 //       final Boolean error_flag = false;

        EditText et1 = (EditText) findViewById(R.id.textView1);
        member = et1.getText().toString();
        if ((member.equals(null)) || (member.isEmpty()) || (member.trim().isEmpty())) {
            et1.requestFocus();
//            error_flag = true;
            Toast.makeText(getApplicationContext(), "Enter profile", Toast.LENGTH_LONG).show();

        } else {

            new AlertDialog.Builder(this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            //
                            //delete selected profile
//                            if (error_flag == false) {

                                DBHandler dbhandler = new DBHandler(AdminActivity.this, null, null, 1);
                                Boolean delete_result = dbhandler.deleteProf(member);

                                if (delete_result == true) {
                                    Toast.makeText(getApplicationContext(), "Profile deleted", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Profile does not exist", Toast.LENGTH_LONG).show();
                                }
//                            }
                        }
                    })

                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.alerticon)
                    .show();

/*        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish(); */
        }
    }

}
