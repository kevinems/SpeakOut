
package com.kevinstudio.speakout;

import android.net.Uri;
import android.provider.BaseColumns;

public class SpeakOut {
    // uri
    public static final String AUTHORITY = "com.kevinstudio.provider.SpeakOut";

    private SpeakOut() {
    }

    // define essential column
    public static final class Notes implements BaseColumns {
        private Notes() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

        // new MIME type, multiple
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";

        public static final String DEFAULT_SORT_ORDER = "title DESC";

        // column
        public static final String TITLE = "title";

        public static final String NOTE = "note";

        public static final String CREATEDDATE = "created";

        public static final String MODIFIEDDATE = "modified";

    }
}
