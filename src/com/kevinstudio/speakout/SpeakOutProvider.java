package com.kevinstudio.speakout;

import com.kevinstudio.speakout.SpeakOut.Notes;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.sql.SQLException;
import java.util.HashMap;

public class SpeakOutProvider extends ContentProvider {
    private static final String TAG = "NotePadProvider";
    
    // database name
    private static final String DATABASE_NAME = "speak_out_library.db";
    private static final int DATABASE_VERSION = 2;
    
    // table name
    private static final String NOTES_TABLE_NAME = "items";
    private static HashMap<String, String> sItemsProjectionMap;
    private static final int NOTES = 1;
    private static final int NOTE_ID = 2;
    private static final UriMatcher sUriMatcher;
    private DatabaseHelper mOpenHelper;
    
    // create table SQL 
    private static final String CREATE_TABLE = "CREATE TABLE "
                                                + NOTES_TABLE_NAME
                                                + " (" + Notes._ID
                                                + " INTEGER PRIMARY KEY,"
                                                + Notes.TITLE
                                                + " TEXT,"
                                                + Notes.NOTE
                                                + " BLOB" + ");";

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(SpeakOut.AUTHORITY, "notes", NOTES);
        sUriMatcher.addURI(SpeakOut.AUTHORITY, "notes/#", NOTE_ID);
        
        sItemsProjectionMap = new HashMap<String, String>();
        sItemsProjectionMap.put(Notes._ID, Notes._ID);
        sItemsProjectionMap.put(Notes.TITLE, Notes.TITLE);
        sItemsProjectionMap.put(Notes.NOTE, Notes.NOTE);
//        sNotesProjectionMap.put(Notes.CREATEDDATE, Notes.CREATEDDATE);
//        sNotesProjectionMap.put(Notes.MODIFIEDDATE, Notes.MODIFIEDDATE);
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
        
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                return Notes.CONTENT_TYPE;
            case NOTE_ID:
                return Notes.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unkonw URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initalValues) {
        // TODO Auto-generated method stub
        if (sUriMatcher.match(uri) != NOTES) {
            throw new IllegalArgumentException("Unkonw URI" + uri);
        }
        
        ContentValues values;
        if (initalValues != null) {
            values = new ContentValues(initalValues);
        } else {
            values = new ContentValues();
        }
        
        if(values.containsKey(SpeakOut.Notes.TITLE) == false) {
//            Resources r = Resources.getSystem();
            values.put(SpeakOut.Notes.TITLE, "no title");
        }
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(NOTES_TABLE_NAME, Notes.NOTE, values);
        if (rowId > 0) {
            Log.i(TAG, "rowId = " + rowId);
            Uri noteUri = ContentUris.withAppendedId(SpeakOut.Notes.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        
        try {
            throw new SQLException("Failed to insert row into " + uri);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                qb.setTables(NOTES_TABLE_NAME);
                qb.setProjectionMap(sItemsProjectionMap);
                break;
                
            case NOTE_ID:
                qb.setTables(NOTES_TABLE_NAME);
                qb.setProjectionMap(sItemsProjectionMap);
                qb.appendWhere(Notes._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unkonw URI" + uri);
                
        }
        
        //String orderBy
        
        // db operate
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null,
                SpeakOut.Notes.DEFAULT_SORT_ORDER);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                count = db.update(NOTES_TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTE_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.update(NOTES_TABLE_NAME, values,
                        Notes._ID + "=" + noteId
                                + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unkonw URI" + uri);
//                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
