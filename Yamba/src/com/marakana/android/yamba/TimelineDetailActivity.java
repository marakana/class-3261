
package com.marakana.android.yamba;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class TimelineDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timeline_detail);

        Intent i = getIntent();
        i.getStringExtra(TimelineActivity.PARAM_DETAILS);

        ((TextView) findViewById(R.id.timeline_details)).setText(
                i.getStringExtra(TimelineActivity.PARAM_DETAILS));
    }
}
