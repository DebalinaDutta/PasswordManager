package com.example.debalina.personalpwm;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class ViewActivity extends Activity {

    MyAdapter ArrAdapt;
    ArrayList<ContentData> list;
    ListView listView;
    String member;
    File file;
    int ACTIVITY_REQUEST_CODE;
    Button OpenExcelBtn;

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

        setContentView(R.layout.activity_view);

        member = getIntent().getExtras().getString("member");

        //        Set the ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        actionBar.setTitle(Html.fromHtml("<font color='#ff0000'>Password Locker</font>"));

        //Setup text bar
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("        List Credentials for " + member);
        tv.setTextColor(Color.BLACK);

        ImageView image = (ImageView) findViewById(R.id.icon1);
        image.setImageResource(R.drawable.view);

        OpenExcelBtn = (Button) findViewById(R.id.button3);
        OpenExcelBtn.setVisibility(View.GONE);

        createList(member);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
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

    // Adapter Class
    public class MyAdapter extends ArrayAdapter implements Filterable {

        Context context;
        private ArrayList<ContentData> mOriginalValues; // Original Values
        private ArrayList<ContentData> mDisplayedValues;    // Values to be displayed
        LayoutInflater inflater;

        public MyAdapter(Context context, int resource, ArrayList<ContentData> list) {
            super(context, resource, list);
            this.context = context;
            this.mOriginalValues = list;
            this.mDisplayedValues = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDisplayedValues.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /*private view holder class*/
        private class ViewHolder {
            TextView item;
            LinearLayout llContainer;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_line_item, null);

                if (position % 2 == 0) {
                    convertView.setBackgroundResource(R.drawable.list_view_item_color);
                } else {
                    convertView.setBackgroundResource(R.drawable.list_view_item_alternatecolor);
                }

                holder = new ViewHolder();
                holder.llContainer = (LinearLayout) convertView.findViewById(R.id.llContainer);
                holder.item = (TextView) convertView.findViewById(R.id.lineitem);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.item.setText(mDisplayedValues.get(position).getsubject());

            holder.llContainer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    final ContentData selectItem = mDisplayedValues.get(position);
//                    Toast.makeText(getApplicationContext(), "Item Selected : " + selectItem, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ViewActivity.this, DetailView.class);
//                    View tv = listView.getChildAt(position);
//                    tv.setTransitionName("NAME");
//                    ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(ViewActivity.this, tv, "NAME");

//                    ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(ViewActivity.this);
                    intent.putExtra("Credentials", selectItem);
                    startActivityForResult(intent, 1);
                }
            });

            return convertView;
        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    mDisplayedValues = (ArrayList<ContentData>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    android.widget.Filter.FilterResults results = new android.widget.Filter.FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<ContentData> FilteredArrList = new ArrayList<ContentData>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<ContentData>(mDisplayedValues); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String data = mOriginalValues.get(i).getsubject();
                            if (data.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new ContentData(mOriginalValues.get(i).getname(),
                                        mOriginalValues.get(i).getsubject(),
                                        mOriginalValues.get(i).getuserid(),
                                        mOriginalValues.get(i).getpassword()));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }

    public void createList(String member) {
        DBHandler dbhandler = new DBHandler(this, null, null, 1);
        list = dbhandler.findcreds(member);

        if (list == null) {
            Toast.makeText(getApplicationContext(), "No data to show..", Toast.LENGTH_SHORT).show();
        } else {
            listView = (ListView) findViewById(R.id.list);
            ArrAdapt = new MyAdapter(ViewActivity.this, R.layout.activity_line_item, list);
            EditText et1 = (EditText) findViewById(R.id.editText1);

            // Set The Adapter
            listView.setAdapter(ArrAdapt);

            et1.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    ViewActivity.this.ArrAdapt.getFilter().filter(cs);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });

/*                    // register onClickListener to handle click events on each item
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        // argument position gives the index of item which is clicked
                        public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                            final ContentData selectItem = list.get(position);
                            Toast.makeText(getApplicationContext(), "Item Selected : " + selectItem, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ViewActivity.this, DetailView.class);
//                    View tv = listView.getChildAt(position);
//                    tv.setTransitionName("NAME");
//                    ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(ViewActivity.this, tv, "NAME");

                            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(ViewActivity.this);
                            intent.putExtra("Credentials", selectItem);
                            startActivity(intent, option.toBundle());
                        }
                    });  */
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String member = data.getStringExtra("result");
                createList(member);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == 2) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "No data received", Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_OK) {
                receiveExcelData(data);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void sendtoExcel(View view) {

        //Build the cursor
        DBHandler dbhandler = new DBHandler(this, null, null, 1);

        ExcelDBCursor eXdBcRSR = dbhandler.fetchDataforDownload(member);
        final SQLiteDatabase db = eXdBcRSR.getdatabase();
        final Cursor cursor = eXdBcRSR.getcursor();

        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "No data to download", Toast.LENGTH_LONG).show();
        } else {
            //Show alert dialog
            new AlertDialog.Builder(this)
                    .setTitle("Save in Dropbox")
                    .setMessage("Do you want to store the file in Dropbox?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Save to Dropbox
                            OpenExcelBtn.setVisibility(View.VISIBLE);

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SqliteToExcel sql2Excel = new SqliteToExcel();
                            sqlite2ExcelResponse resp = sql2Excel.downloadData(db, cursor, member);
                            if (resp.getSuccess() == true) {
                                file = new File(resp.getdirectory(), resp.getfilename());
                                String mesg = "Data downloaded as " + resp.getfilename() + " in the path: " + resp.getdirectory();
                                Toast.makeText(getApplicationContext(), mesg, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), resp.getmesg(), Toast.LENGTH_LONG).show();
                            }

                            OpenExcelBtn.setVisibility(View.VISIBLE);
                        }
                    })
                    .setIcon(R.drawable.alerticon)
                    .show();
        }

    }

    public void openExcel(View view) {

        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ViewActivity.this, "No Application Available to View Excel", Toast.LENGTH_LONG).show();
        }
    }

    public void loadfromExcel(View view) {

        Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        fileintent.setType("gagt/sdf");
        try {
            startActivityForResult(fileintent, 2);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ViewActivity.this, "No Application Available to Read Excel", Toast.LENGTH_LONG).show();
        }

    }

    public void receiveExcelData(Intent data) {

        String FilePath = data.getData().getPath();
        Context context = getApplicationContext();

        ExcelToSqlite excel2Ssqlite = new ExcelToSqlite();
        Excel2SqliteResponse resp = excel2Ssqlite.downloadFromExceltoSqlite(member, FilePath, context);

        if (resp.getSuccess() == true) {
            if (resp.mesg == "nothing") {
                Toast.makeText(ViewActivity.this, "Blank excel found; data load failed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ViewActivity.this, resp.mesg, Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(ViewActivity.this, resp.mesg, Toast.LENGTH_LONG).show();
        }

    }
}