package com.marakana.android.yamba;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.marakana.android.yamba.svc.YambaService;


public class StatusActivity extends Activity {
    private static final String TAG = "STATUS";

    private TextView count;
    private EditText status;

    private int okColor;
    private int warnColor;
    private int errColor;

    private int maxStatusLen;
    private int warnMax;
    private int errMax;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yamba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_prefs:
                startActivity(new Intent(this, YambaPreferences.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

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
        Log.d(TAG, "posting: " + msg);

        if (TextUtils.isEmpty(msg)) { return; }

        YambaService.post(this, msg);

        status.setText("");
    }
}
