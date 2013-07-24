package com.marakana.android.yamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;


public class YambaApplication extends Application
    implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG = "APP";

    private YambaClient client;
    private String userKey;
    private String pwdKey;
    private String uriKey;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Yamba started!");

        userKey = getString(R.string.prefsKeyUser);
        pwdKey = getString(R.string.prefsKeyPass);
        uriKey = getString(R.string.prefsKeyURI);

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public synchronized void onSharedPreferenceChanged(SharedPreferences p, String s) {
        Log.d(TAG, "prefs changed");
        client = null;
    }

    public synchronized YambaClient getClient() {
        if (null == client) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            String usr = prefs.getString(userKey, "");
            String pwd = prefs.getString(pwdKey, "");
            String uri = prefs.getString(uriKey, "");
            Log.d(TAG, "new client: " + usr + "," + pwd  + "@" + uri);

            client = new YambaClient(usr, pwd, uri);
        }

        return client;
    }
}
