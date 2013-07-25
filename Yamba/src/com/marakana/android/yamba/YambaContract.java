package com.marakana.android.yamba;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class YambaContract {
    private YambaContract() { }

    public static final String AUTHORITY
         = "com.marakana.android.yamba";

    public static final Uri BASE_URI = new Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT)
        .authority(AUTHORITY)
        .build();

    public static final class Timeline {
        private Timeline() { }

        public static final String TABLE = "timeline";

        public static final Uri URI
            = BASE_URI.buildUpon().appendPath(TABLE).build();

        public static final class Column {
            private Column() { }

            public static final String ID = BaseColumns._ID;
            public static final String TIMESTAMP = "time";
            public static final String USER = "user";
            public static final String STATUS = "message";
        }
    }
}