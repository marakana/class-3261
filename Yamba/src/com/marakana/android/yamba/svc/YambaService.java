package com.marakana.android.yamba.svc;

import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.R;
import com.marakana.android.yamba.YambaApplication;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class YambaService extends IntentService {
    private static final String TAG = "SVC";



    static final int OP_POST_COMPLETE = -1;
    private static final int OP_POST = -2;
    private static final int OP_POLL = -3;

    private static final String PARAM_OP = "YambaService.OP";
    private static final String PARAM_STATUS = "YambaService.STATUS";

    private static final int POLL_REQ = 42;

    private static class Hdlr extends Handler {
        private final  YambaService svc;

        public Hdlr(YambaService svc) { this.svc = svc; }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case OP_POST_COMPLETE:
                    Toast.makeText(svc, msg.arg1, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public static void startPolling(Context ctxt) {
        AlarmManager mgr = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        mgr.setInexactRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis() + 100,
                getPollInterval(ctxt),  // should be a resource!
                getPollingIntent(ctxt));
    }

    public static void post(Context ctxt, String msg) {
        Intent i = new Intent(ctxt, YambaService.class);
        i.putExtra(PARAM_OP, OP_POST);
        i.putExtra(PARAM_STATUS, msg);
        ctxt.startService(i);
    }

    private static PendingIntent getPollingIntent(Context ctxt) {
        Intent i = new Intent(ctxt, YambaService.class);
        i.putExtra(PARAM_OP, OP_POLL);
        return PendingIntent.getService(
                ctxt,
                POLL_REQ,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static synchronized long getPollInterval(Context ctxt) {
        if (0 >= pollInterval) {
            pollInterval
                = 1000 * 60 * ctxt.getResources().getInteger(R.integer.poll_interval);
        }
        return pollInterval;
    }

    // LAZILY INITIALIZED!  Use getPollInterval().
    private static long pollInterval;


    private volatile int maxPolls;
    private volatile Hdlr hdlr;

    public YambaService() { super(TAG); }

    @Override
    public void onCreate() {
        super.onCreate();
        maxPolls = getResources().getInteger(R.integer.poll_max);
        hdlr = new Hdlr(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int op = intent.getIntExtra(PARAM_OP, 0);
        switch(op) {
            case OP_POST:
                doPost(intent);
                break;
            case OP_POLL:
                doPoll();
                break;

                default:
                    throw new IllegalArgumentException("Unrecognized op: " + op);
        }

    }

    private void doPoll() {
        Log.d(TAG, "polling");
        try {
            parseTimeline(
                ((YambaApplication) getApplication()).getClient().getTimeline(maxPolls));
        }
        catch (YambaClientException e) {
            Log.e(TAG, "Poll failed: " + e, e);
        }
    }

    private void doPost(Intent intent) {
        String status = intent.getStringExtra(PARAM_STATUS);
        Log.d(TAG, "posting: " + status);

        int msg = R.string.statusFailed;
        try {
            ((YambaApplication) getApplication()).getClient().postStatus(status);
            msg = R.string.statusSucceeded;
        }
        catch (YambaClientException e) {
            Log.e(TAG, "Post failed: " + e, e);
        }

        Message.obtain(hdlr, OP_POST_COMPLETE, msg, 0).sendToTarget();
    }

    private void parseTimeline(List<Status> timeline) {
        for (Status status: timeline) {
            Log.d(
                    TAG,
                    "status #" + status.getId()
                    + "@" + status.getUser()
                    + "-" + status.getCreatedAt()
                    + ":" + status.getMessage());
        }
    }
}
