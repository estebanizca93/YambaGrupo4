package com.example.esteban.yambagrupo4;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class RefreshService extends IntentService {
    static final String TAG = "RefreshService";
    static final int DELAY = 30000; // medio minuto
    private boolean runFlag = false;
    DbHelper dbHelper;
    SQLiteDatabase db;

    public RefreshService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
        dbHelper = new DbHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.runFlag = true;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String accesstoken = prefs.getString("accesstoken", "");
        String accesstokensecret = prefs.getString("accesstokensecret", "");

        Log.d(TAG, "onStarted");

        while (runFlag) {
            Log.d(TAG, "Updater running");
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey("9b7zS9MZiwLEkyKvsbt4LXQhK")
                        .setOAuthConsumerSecret("b2T3kEWGZUZA8VdF8uIzG9Wl2WCGCCUjdJIhWSpP7iUYCmhVCH")
                        .setOAuthAccessToken(accesstoken)
                        .setOAuthAccessTokenSecret(accesstokensecret);
                TwitterFactory factory = new TwitterFactory(builder.build());
                Twitter twitter = factory.getInstance();
                try {
                    List<Status> timeline = twitter.getHomeTimeline();

                    // Iteramos sobre todos los componentes de timeline
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    for (Status status : timeline) {
                    // Insertar en la base de datos!
                        values.clear();
                        values.put(StatusContract.Column.ID, status.getId());
                                values.put(StatusContract.Column.USER,
                                        status.getUser().getName());
                        values.put(StatusContract.Column.MESSAGE, status.getText());
                                values.put(StatusContract.Column.CREATED_AT,
                                        status.getCreatedAt().getTime());
                        db.insertWithOnConflict(StatusContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);
                    }
                    // Cerrar la base de datos
                    db.close();
                } catch (TwitterException e) {
                    Log.e(TAG, "Failed to fetch the timeline", e);
                }

                Log.d(TAG, "Updater ran");
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                runFlag = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
