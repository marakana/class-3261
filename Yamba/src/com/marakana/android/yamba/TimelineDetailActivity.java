
package com.marakana.android.yamba;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class TimelineDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timeline_detail);

        Intent i = getIntent();

        long t = i.getLongExtra(YambaContract.Timeline.Column.TIMESTAMP, 0L);
        ((TextView) findViewById(R.id.timeline_detail_timestamp)).setText(
                DateUtils.getRelativeTimeSpanString(t));
        ((TextView) findViewById(R.id.timeline_detail_user)).setText(
                i.getStringExtra(YambaContract.Timeline.Column.USER));
        ((TextView) findViewById(R.id.timeline_detail_status)).setText(
                i.getStringExtra(YambaContract.Timeline.Column.STATUS));
    }
}
