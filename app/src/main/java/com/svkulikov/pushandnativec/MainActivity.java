package com.svkulikov.pushandnativec;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    private final List<MessageData> messageList = new ArrayList<>();
    private final Calendar calendar = Calendar.getInstance();


    private Disposable disposableList;

    private final String TAG = "MainActivity";

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private PendingIntent resultPendingIntent;
    private Intent resultIntent;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDao appDao = App.getInstance().getAppDatabase().getDaoDatabase();

        // Example of a call to a native method
        // TextView tv = findViewById(R.id.sample_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        /*
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        */
        // [END handle_data_extras]

        /*
        findViewById(R.id.subscribeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Subscribing to weather topic");
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("Push and Native C++")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = getString(R.string.msg_subscribed);
                                if (!task.isSuccessful()) {
                                    msg = getString(R.string.msg_subscribe_failed);
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END subscribe_topics]
                // FirebaseMessaging.
            }
        });
        */


        RecyclerView rvMessageList = findViewById(R.id.rvMessageList);
        MessageAdapter messageAdapter = new MessageAdapter(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                RecyclerView.VERTICAL);

        rvMessageList.setLayoutManager(new LinearLayoutManager(this));
        rvMessageList.setAdapter(messageAdapter);
        rvMessageList.setClickable(true);
        rvMessageList.setLongClickable(true);
        rvMessageList.setOnCreateContextMenuListener(this); // необходимо для контекстного меню для RecycledView
        rvMessageList.addItemDecoration(dividerItemDecoration);

        disposableList = appDao.rx_loadMessageList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                            messageAdapter.setList(messages);
                            messageAdapter.notifyDataSetChanged();
                        },
                        err -> {
                            Log.e(TAG, err.getLocalizedMessage());
                        },
                        () -> Log.d(TAG, "com")
                );

        showToken();
        // setNotification();

        //tv.setText(stringFromJNI("654321"));
    }

    private void showToken() {
        FirebaseMessaging.getInstance().getToken()
              .addOnCompleteListener(new OnCompleteListener<String>() {
                  @Override
                  public void onComplete(@NonNull Task<String> task) {
                      if (!task.isSuccessful()) {
                          Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                          return;
                      }

                      // Get new FCM registration token
                      String token = task.getResult();

                      // Log and toast
                      String msg = getString(R.string.msg_token_fmt, token);

                      ((EditText)findViewById(R.id.etToken)).setText(token);

                      Log.d(TAG, msg);
                      Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                  }
              });
    }


    // https://coderlessons.com/articles/mobilnaia-razrabotka-articles/kak-nachat-rabotu-s-push-uvedomleniiami-na-android

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    /*
    private void createPendingIntent() {
        // Create PendingIntent
        resultIntent = new Intent(this, MainActivity.class);
        resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        resultPendingIntent = PendingIntent.getBroadcast(this, 0, resultIntent, 0);
    }

     */

    @Override
    protected void onDestroy() {
        disposableList.dispose();
        super.onDestroy();
    }
}