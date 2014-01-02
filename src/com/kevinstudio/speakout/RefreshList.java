package com.kevinstudio.speakout;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

public class RefreshList extends AsyncTask<ContentResolver, Void, Cursor> {
    String[] columns = new String[] {
            SpeakOut.Notes._ID, SpeakOut.Notes.TITLE, SpeakOut.Notes.NOTE
    };
    Uri myUri = SpeakOut.Notes.CONTENT_URI;
    @Override
    protected Cursor doInBackground(ContentResolver... params) {
        // TODO Auto-generated method stub
        Cursor newCursor = params[0].query(myUri, columns, null, null, null);
        return newCursor;
    }

}
