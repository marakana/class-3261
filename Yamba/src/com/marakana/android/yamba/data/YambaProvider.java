package com.marakana.android.yamba.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class YambaProvider extends ContentProvider {
    private YambaDBHelper db;

    @Override
    public boolean onCreate() {
        db = new YambaDBHelper(getContext());
        return null != db;
    }

    @Override
    public String getType(Uri arg0) {
        throw new UnsupportedOperationException("getType not supported");
    }

    @Override
    public Cursor query(Uri uri, String[] proj, String sel, String[] selArgs, String sort) {
        return getDb()
            .query(YambaDBHelper.TABLE, proj, sel, selArgs, null, null, sort);
    }

    @Override
    public int bulkInsert(Uri arg0, ContentValues[] arg1) {
        throw new UnsupportedOperationException("bulkInsert not supported");
    }

    @Override
    public Uri insert(Uri uri, ContentValues vals) {
        long id = getDb().insert(YambaDBHelper.TABLE, null, vals);
        return (0 <= id) ? null : uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        throw new UnsupportedOperationException("delete not supported");
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException("update not supported");
    }

    private SQLiteDatabase getDb() { return db.getWritableDatabase(); }
}