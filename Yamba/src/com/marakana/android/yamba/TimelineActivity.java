
package com.marakana.android.yamba;

import android.os.Bundle;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TimelineActivity extends ListActivity implements LoaderCallbacks<Cursor> {
    private static final String TAG = "TIMELINE";

    public static final String PARAM_DETAILS = "TimelineActivity.DETAILS";

    private static final int TIMELINE_LOADER = 666;

    private static final String[] PROJ = {
        YambaContract.Timeline.Column.ID,
        YambaContract.Timeline.Column.TIMESTAMP,
        YambaContract.Timeline.Column.USER,
        YambaContract.Timeline.Column.STATUS,
    };

    private static final String[] FROM = new String[PROJ.length - 1];
    static { System.arraycopy(PROJ, 1, FROM, 0, FROM.length); }

    private static final int[] TO = {
        R.id.timeline_time,
        R.id.timeline_user,
        R.id.timeline_status,
    };

    static class TimelineBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor c, int idx) {
            if (R.id.timeline_time != view.getId()) { return false; }

            CharSequence s = "long ago";
            long t = c.getLong(idx);
            if (0 < t) { s = DateUtils.getRelativeTimeSpanString(t); }
            ((TextView) view).setText(s);
            return true;
        }
    }

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

            case R.id.menu_status:
                Intent i = new Intent(this, StatusActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "create loader");
        return new CursorLoader(
                this,
                YambaContract.Timeline.URI,
                PROJ,
                null,
                null,
                YambaContract.Timeline.Column.TIMESTAMP + " DESC" );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> l, Cursor c) {
        Log.d(TAG, "load finished: " + c.getCount());
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> c) {
        Log.d(TAG, "load reset");
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.timeline_row,
                null,
                FROM,
                TO,
                0);

        adapter.setViewBinder(new TimelineBinder());
        setListAdapter(adapter);

        getLoaderManager().initLoader(TIMELINE_LOADER, null, this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int p, long id) {
        Intent i = new Intent(this, TimelineDetailActivity.class);
        Cursor c = (Cursor) l.getItemAtPosition(p);
        i.putExtra(
                PARAM_DETAILS,
                c.getString(c.getColumnIndex(YambaContract.Timeline.Column.STATUS)));

        startActivity(i);
    }
}
