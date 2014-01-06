
package com.kevinstudio.speakout;

import android.net.Uri;
import android.provider.BaseColumns;

public class SpeakOut {
    // uri
    public static final String AUTHORITY = "com.kevinstudio.provider.SpeakOut";

    private SpeakOut() {
    }

    // define essential column
    public static final class QuestionItem implements BaseColumns {
        private QuestionItem() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/question");

        // new MIME type, multiple
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.question";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.question";

        public static final String DEFAULT_SORT_ORDER = "created DESC";

        // column

        public static final String CONTENT = "content";

        public static final String CREATEDDATE = "created";
    }
}
