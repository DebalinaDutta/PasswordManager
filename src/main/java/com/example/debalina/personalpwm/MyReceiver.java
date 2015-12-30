package com.example.debalina.personalpwm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

/**
 * Created by Debalina on 12/18/2015.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> messageArr = intent.getStringArrayListExtra("members");

        if (messageArr != null) {
            notifyDueMessage(messageArr, context);
        }
    }

    public void notifyDueMessage(ArrayList<String> list, Context context) {

        String message = createNotification(list);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("Password change notification")
                        .setContentText(message);

        //expand notification
        expandNotification(mBuilder, list);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    public String createNotification(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < list.size(); index++) {
            String item = list.get(index);

            sb.append(item + "\n");
        }
        String message = sb.toString();
        return message;
    }

    public void expandNotification(NotificationCompat.Builder mBuilder, ArrayList<String> list) {

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
//        String[] events = new String[6];
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Notification details:");

// Moves events into the expanded layout
        for (int i=0; i < list.size(); i++) {

            inboxStyle.addLine(list.get(i));
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);


    }
}