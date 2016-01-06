package com.example.debalina.personalpwm;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class tableView extends Activity {

    String member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enable window content transition
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setAllowEnterTransitionOverlap(true);

// set an exit transition
        getWindow().setExitTransition(new Fade());
        getWindow().setReenterTransition(new Fade(

        ));

        setContentView(R.layout.activity_table_view);

        member = getIntent().getExtras().getString("member");

        //        Set the ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        actionBar.setTitle(Html.fromHtml("<font color='#ff0000'>Password Locker</font>"));

        //Setup text bar
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("    Hi " + member + ", " + "History data");
        tv.setTextColor(Color.BLACK);

        ImageView image = (ImageView) findViewById(R.id.histicon);
        image.setImageResource(R.drawable.historyimages);

        //set recyclerview
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //get table data
        DBHandler dbhandler = new DBHandler(this, null, null, 1);
        ArrayList<TableData> list = dbhandler.fetchTableData(member);

        if (list == null) {
            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
        }else {
            // specify an adapter
            TableDataAdapter mAdapter = new TableDataAdapter(list);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table_view, menu);
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

    public class TableDataAdapter extends RecyclerView.Adapter<TableDataAdapter.ViewHolder> {

        ArrayList<TableData> mDisplayedValues;

        public TableDataAdapter(ArrayList<TableData> mDisplayedValues){
            this.mDisplayedValues = mDisplayedValues;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv1;
            TextView tv2;
            TextView tv3;
            TextView tv4;
            TextView tv5;
            TextView tv6;
            TextView tv7;
            TextView tv8;
            TextView tv9;

            public ViewHolder(View v) {
                super(v);
                tv1 = (TextView) v.findViewById(R.id.textView001);
                tv2 = (TextView) v.findViewById(R.id.textView002);
                tv3 = (TextView) v.findViewById(R.id.textView003);
                tv4 = (TextView) v.findViewById(R.id.textView004);
                tv5 = (TextView) v.findViewById(R.id.textView005);
                tv6 = (TextView) v.findViewById(R.id.textView006);
                tv7 = (TextView) v.findViewById(R.id.textView007);
                tv8 = (TextView) v.findViewById(R.id.textView008);
                tv9 = (TextView) v.findViewById(R.id.textView009);
            }
        }

            @Override
            // Create new views (invoked by the layout manager)
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                // create a new view
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_view_item, parent, false);
                // set the view's size, margins, paddings and layout parameters
                ViewHolder vh = new ViewHolder(v);
                return vh;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {

                final TableData tabdata = mDisplayedValues.get(position);

                holder.tv1.setText(String.valueOf(tabdata.getID()));
                holder.tv2.setText(tabdata.getname());
                holder.tv3.setText(tabdata.getsubject());
                holder.tv4.setText(tabdata.getuserid());
                holder.tv5.setText(tabdata.getpassword());
                holder.tv6.setText(String.valueOf(tabdata.getID1()));
                holder.tv7.setText(String.valueOf(tabdata.getparent()));
                holder.tv8.setText(tabdata.geteffdate());
                holder.tv9.setText(String.valueOf(tabdata.getdays()));
            }

            // Return the size of your dataset (invoked by the layout manager)
            @Override
            public int getItemCount() {
                return mDisplayedValues.size();
            }

        }
    }


