//package com.nagash.appwebbrowser.controller;
//
//import android.app.IntentService;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.app.NotificationManagerCompat;
//import android.support.v4.util.DebugUtils;
//
//import com.nagash.appwebbrowser.R;
//
//public class NotificationUtils {
//    public static final int NOTIFICATION_ID = 1;
//
//    public static final String ACTION_1 = "action_1";
//
//    public static void displayNotification(Context context) {
//
//        Intent action1Intent = new Intent(context, NotificationActionService.class).setAction(ACTION_1);
//
//        PendingIntent action1PendingIntent = PendingIntent.getService(context, 0,
//                action1Intent, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.ic_up_down_arrows_white_24dp)
//                        .setContentTitle("Sample Notification")
//                        .setContentText("Notification text goes here")
//                        .addAction(new NotificationCompat.Action(R.drawable.ic_up_down_arrows_white_24dp,
//                                "Action 1", action1PendingIntent));
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
//    }
//
//    public static class NotificationActionService extends IntentService {
//        public NotificationActionService() {
//            super(NotificationActionService.class.getSimpleName());
//        }
//
//        @Override
//        protected void onHandleIntent(Intent intent) {
//            String action = intent.getAction();
//            if (ACTION_1.equals(action)) {
//                // TODO: handle action 1.
//                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
//            }
//        }
//}