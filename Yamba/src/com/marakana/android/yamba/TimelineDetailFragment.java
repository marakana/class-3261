package com.marakana.android.yamba;

import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TimelineDetailFragment extends Fragment {

    public static Fragment newInstance(Bundle args) {
        TimelineDetailFragment frag = new TimelineDetailFragment();
        frag.setArguments(args);
        return frag;
    }


    private View details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle state) {
        details = inflater.inflate(R.layout.timeline_detail_fragment, root, false);

        if (null == state) { state = getArguments(); }
        if (null != state) {
            setDetails(
                    state.getLong(YambaContract.Timeline.Column.TIMESTAMP),
                    state.getString(YambaContract.Timeline.Column.USER),
                    state.getString(YambaContract.Timeline.Column.STATUS));
        }

        return details;
    }

    public void setDetails(long ts, String user, String status) {
        ((TextView) details.findViewById(R.id.timeline_detail_timestamp))
            .setText(DateUtils.getRelativeTimeSpanString(ts));
        ((TextView) details.findViewById(R.id.timeline_detail_user)).setText(user);
        ((TextView) details.findViewById(R.id.timeline_detail_status)).setText(status);
    }
}
