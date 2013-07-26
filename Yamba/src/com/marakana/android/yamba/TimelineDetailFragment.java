package com.marakana.android.yamba;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TimelineDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle state) {

        View v = inflater.inflate(R.layout.timeline_detail_fragment, root, false);

        Intent i = getActivity().getIntent();

        long t = i.getLongExtra(YambaContract.Timeline.Column.TIMESTAMP, 0L);
        ((TextView) v.findViewById(R.id.timeline_detail_timestamp)).setText(
                DateUtils.getRelativeTimeSpanString(t));
        ((TextView) v.findViewById(R.id.timeline_detail_user)).setText(
                i.getStringExtra(YambaContract.Timeline.Column.USER));
        ((TextView) v.findViewById(R.id.timeline_detail_status)).setText(
                i.getStringExtra(YambaContract.Timeline.Column.STATUS));

        return v;
    }
}
