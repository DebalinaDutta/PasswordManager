package com.example.debalina.personalpwm;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.debalina.personalpwm.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Debalina on 10/29/2015.
 */
public class listAdapter extends ArrayAdapter<String> {

    Context context;
    String rowitem1;
    public listAdapter(Context context, int resource, ArrayList<String> list) {
        super(context, resource, list);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView item;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String rowitem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_grid_list_line_item, null);

            StringTokenizer tokens = new StringTokenizer(rowitem, ":");
            String subject = tokens.nextToken();
            String days = tokens.nextToken();
            String duemode = tokens.nextToken();

            if(duemode.equals("p"))  {
                 rowitem1 = " " + subject + " - " + days + " days left";
                convertView.setBackgroundResource(R.drawable.list_view_item_color);
            }else {
               int abs_days = (Integer.valueOf(days) < 0) ? -Integer.valueOf(days) : Integer.valueOf(days);
                rowitem1 = " " + subject + " - " + abs_days + " days overdue";
                convertView.setBackgroundResource(R.drawable.overdue);
            }

            holder = new ViewHolder();
            holder.item = (TextView) convertView.findViewById(R.id.lineitem);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.item.setText(rowitem1);

        return convertView;
    }
}
