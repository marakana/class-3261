
package com.marakana.android.yamba;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusActivity extends Activity {
    private static final String TAG = "STATUS";

    static class Poster extends AsyncTask<String, Void, Integer> {
        private Context ctxt;

        public Poster(Context ctxt) { this.ctxt = ctxt; }

        @Override
        protected Integer doInBackground(String... params) {

            try {
                new YambaClient(
                        "student",
                        "password",
                        "http://yamba.marakana.com/api")
                    .postStatus(params[0]);
                return Integer.valueOf(R.string.statusSucceeded);
            }
            catch (YambaClientException e) {
                Log.e(TAG, "Post failed: ", e);
            }
            return Integer.valueOf(R.string.statusFailed);
        }

        @Override
        protected void onPostExecute(Integer result) {
            done(result.intValue());
        }

        @Override
        protected void onCancelled() { done(R.string.statusFailed); }

        private void done(int msg) {
            poster = null;
            Toast.makeText(ctxt, msg, Toast.LENGTH_LONG) .show();
        }
    }

    static Poster poster;

    private TextView count;
    private EditText status;

    private int okColor;
    private int warnColor;
    private int errColor;

    private int maxStatusLen;
    private int warnMax;
    private int errMax;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        Resources rez = getResources();

        okColor = rez.getColor(R.color.status_ok);
        warnColor = rez.getColor(R.color.status_warn);
        errColor = rez.getColor(R.color.status_err);

        maxStatusLen = rez.getInteger(R.integer.status_max_len);
        warnMax = rez.getInteger(R.integer.warn_max);
        errMax = rez.getInteger(R.integer.err_max);

        setContentView(R.layout.activity_status);

        findViewById(R.id.status_submit).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { post(); }
                } );

        count = (TextView) findViewById(R.id.status_count);
        status = (EditText) findViewById(R.id.status_status);
        status.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable str) { updateCount(); }

                    @Override
                    public void beforeTextChanged(CharSequence str, int s, int c, int a) { }

                    @Override
                    public void onTextChanged(CharSequence str, int s, int c, int a) { }
                } );
    }

    void updateCount() {
        int n = maxStatusLen - status.getText().length();

        int c;
        if (n > warnMax) { c = okColor; }
        else if (n > errMax) { c = warnColor; }
        else { c = errColor; }

        count.setText(String.valueOf(n));
        count.setTextColor(c);
    }

    void post() {
        String msg = status.getText().toString();
        if (TextUtils.isEmpty(msg)) { return; }

        if (null != poster) { return; }

        poster = new Poster(getApplicationContext());
        poster.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, msg);

        status.setText("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }
}
