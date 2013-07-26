package com.marakana.android.yamba;

import android.content.Intent;
import android.os.Bundle;


public class TimelineDetailActivity extends YambaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_detail_activity);

        TimelineDetailFragment frag = (TimelineDetailFragment) getFragmentManager()
            .findFragmentByTag(getString(R.string.timeline_detail_fragment));

        Intent i = getIntent();
        frag.setDetails(
                i.getLongExtra(YambaContract.Timeline.Column.TIMESTAMP, 0L),
                i.getStringExtra(YambaContract.Timeline.Column.USER),
                i.getStringExtra(YambaContract.Timeline.Column.STATUS));
    }
}
