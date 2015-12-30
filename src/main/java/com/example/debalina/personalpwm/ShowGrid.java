package com.example.debalina.personalpwm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SyncRequest;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class ShowGrid extends Activity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    String member;
    MemberCache memcas;
    int ACTIVITY_REQUEST_CODE = 1;
//    ArrayList<String> list = new ArrayList<String>();
//    ListView listV;
//    listAdapter dueAdapter;

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

        setContentView(R.layout.activity_show_grid);

        //receive intent - 'member' and cache it
        member = getIntent().getStringExtra("member");
        memcas = new MemberCache();
        if (member != null) {
            memcas.setmember(member);
        }
        //        Set the ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        //set up listview
        createListView(member);

        //set up gridview
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (position == 0) {
                    Toast.makeText(ShowGrid.this, "Taking to Add screen", Toast.LENGTH_SHORT).show();
                    View iv = gridview.getChildAt(position);
                    iv.setTransitionName("test");
                    final Intent intent = new Intent(ShowGrid.this, AddActivity.class);
                    ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(ShowGrid.this, iv, "test");
                    if (member == null) {
                        member = memcas.getmember();
                    }
                    intent.putExtra("member", member);
                    startActivity(intent, option.toBundle());
                } else {
                    if (position == 1) {
                        Toast.makeText(ShowGrid.this, "Taking to View screen", Toast.LENGTH_SHORT).show();
                        View iv = gridview.getChildAt(position);
                        iv.setTransitionName("test");
                        final Intent intent = new Intent(ShowGrid.this, ViewActivity.class);
                        //                       ActivityOptions  option = ActivityOptions.makeSceneTransitionAnimation(ShowGrid.this);
                        ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(ShowGrid.this, iv, "test");
                        if (member == null) {
                            member = memcas.getmember();
                        }
                        intent.putExtra("member", member);
                        startActivity(intent, option.toBundle());
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_grid, menu);
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

    public void createListView(String member) {

        DBHandler dbhandler = new DBHandler(this, null, null, 1);

        ArrayList<String> item_array = dbhandler.fetchForPWDexp(member);

        if (item_array == null) {
            Toast.makeText(getApplicationContext(), "No data to show", Toast.LENGTH_LONG).show();
        } else {
            BuildDueArray(item_array);
        }

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

    public void BuildDueArray(ArrayList<String> item_array) {

        final ArrayList<String> list = new ArrayList<String>();
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
                        due = "n";
                    } else {
                        due = "p";
                    }
                    String dueItem = subject + ":" + String.valueOf(DaysLeft) + ":" + due;
                    list.add(dueItem);

                }
            }
        }
            listAdapter dueAdapter =
                    new listAdapter(ShowGrid.this, R.layout.activity_grid_list_line_item, list);
            // Set The Adapter
            ListView listV = (ListView) findViewById(R.id.list1);
            listV.setAdapter(dueAdapter);

            // register onClickListener to handle click events on each item
            listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                // argument position gives the index of item which is clicked
                public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                    String due_Item = list.get(position);
                    StringTokenizer tokens = new StringTokenizer(due_Item, ":");
                    String subject = tokens.nextToken();

                    DBHandler dbhandler = new DBHandler(ShowGrid.this, null, null, 1);
                    ArrayList<ContentData> listItem = dbhandler.findcredsWithSub(member, subject);
                    ContentData credObject = listItem.get(0);
                    Intent intent = new Intent(ShowGrid.this, DetailView.class);

                    intent.putExtra("Credentials", credObject);
                    startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
//                startActivityForResult(intent, ACTIVITY_REQUEST_CODE);

                }
            });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String member = data.getStringExtra("result");
                createListView(member);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    public void refreshProcess (View view) {

        //set up listview
        createListView(member);
    }

    }


