package com.school.shopbudd.ui.shoppinglist.reminder;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import com.school.shopbudd.R;
import com.school.shopbudd.main.MainActivity;
import com.school.shopbudd.main.SettingConsts;
import com.school.shopbudd.ui.product.ProductActivity;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ReminderScheduler extends IntentService {
    public static final String MESSAGE_TEXT = "com.shoppinglist.notificationservicetext";

    public ReminderScheduler() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String messageText = intent.getStringExtra(MESSAGE_TEXT);
        String listId = intent.getStringExtra(MainActivity.LIST_ID_KEY);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(getApplicationContext(), listId);

        String appName = getResources().getString(R.string.app_name);

        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(SettingConsts.NOTIFICATIONS_ENABLED, true)) {

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(appName)
                    .setContentText(messageText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageText))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .build();

            manager.notify(Integer.parseInt(listId), notification);
        }
    }

    private PendingIntent getPendingIntent(Context context, String listId) {
        Intent pendingIntent = new Intent(context, ProductActivity.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        pendingIntent.putExtra(MainActivity.LIST_ID_KEY, listId);
        TaskStackBuilder stackBuilder = TaskStackBuilder
                .create(context)
                .addParentStack(ProductActivity.class)
                .addNextIntent(pendingIntent);
        return stackBuilder.getPendingIntent(Integer.parseInt(listId), PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
