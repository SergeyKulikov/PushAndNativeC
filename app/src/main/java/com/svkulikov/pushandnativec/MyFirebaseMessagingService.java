package com.svkulikov.pushandnativec;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String channelId;
    private String channelName;
    private final String TAG = "MyFirebaseMesService";

    private final AppDao appDao = App.getInstance().getAppDatabase().getDaoDatabase();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        channelId  = getString(R.string.default_notification_channel_id);
        channelName = getString(R.string.default_notification_channel_name);

        String toHash = remoteMessage.getNotification().getBody();
        String hashValue = getSHA256(toHash);

        MessageData messageData = new MessageData(toHash, hashValue);

         appDao.rx_saveMessage(messageData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                            Log.d(TAG, "Saved message ("+messageData.getToHash()+"): "+messageData.getHashValue());
                        },
                        err -> {
                            Log.e(TAG, err.getLocalizedMessage());
                        },
                        () -> Log.d(TAG, "com")
                ); //.dispose();

        sendNotification(toHash);

        // Log.d("===>  ", remoteMessage.getNotification().getBody());
    }

    public native String getSHA256(String toHash);

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                 //  .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher))
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}