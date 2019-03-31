package com.devlearn.sohel.auction.PushNotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.devlearn.sohel.auction.BoughtItem.BoughtItemsActivity;
import com.devlearn.sohel.auction.MainBuySellActivity;
import com.devlearn.sohel.auction.R;

public class NotificationHelper {

    public static void displayNotification(Context context, String title, String body){

        Intent intent = new Intent(context,BoughtItemsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT //it will cancel pending intent that are already created
        );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,MainBuySellActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_email)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(1,mBuilder.build());
    }
}
