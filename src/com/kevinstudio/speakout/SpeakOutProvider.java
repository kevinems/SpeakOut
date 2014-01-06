package com.kevinstudio.speakout;

import com.kevinstudio.speakout.SpeakOut.QuestionItem;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SpeakOutProvider extends ContentProvider {
    private static final String TAG = "NotePadProvider";
    
    // database name
    private static final String DATABASE_NAME = "speak_out_library.db";
    private static final int DATABASE_VERSION = 2;
    
    // table name
    private static final String NOTES_TABLE_NAME = "question_items";
    private static HashMap<String, String> sQuestionItemsProjectionMap;
    private static final int QUESTION = 1;
    private static final int QUESTION_ID = 2;
    private static final UriMatcher sUriMatcher;
    private DatabaseHelper mOpenHelper;
    
    // create table SQL 
    private static final String CREATE_TABLE = "CREATE TABLE "
                                                + NOTES_TABLE_NAME
                                                + " (" + QuestionItem._ID
                                                + " INTEGER PRIMARY KEY,"
                                                + QuestionItem.CONTENT
                                                + " TEXT,"
                                                + QuestionItem.CREATEDDATE
                                                + " TEXT" 
                                                + ");";

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(SpeakOut.AUTHORITY, "question", QUESTION);
        sUriMatcher.addURI(SpeakOut.AUTHORITY, "question/#", QUESTION_ID);
        
        sQuestionItemsProjectionMap = new HashMap<String, String>();
        sQuestionItemsProjectionMap.put(QuestionItem._ID, QuestionItem._ID);
        sQuestionItemsProjectionMap.put(QuestionItem.CONTENT, QuestionItem.CONTENT);
        sQuestionItemsProjectionMap.put(QuestionItem.CREATEDDATE, QuestionItem.CREATEDDATE);
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
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case QUESTION:
                count = db.delete(NOTES_TABLE_NAME, selection, selectionArgs);
                break;
                
            case QUESTION_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.delete(NOTES_TABLE_NAME,
                        QuestionItem._ID + "=" + noteId
                                + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),
                        selectionArgs);
                break;                    

            default:
                throw new IllegalArgumentException("Unknow URI" + uri);
//                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        switch (sUriMatcher.match(uri)) {
            case QUESTION:
                return QuestionItem.CONTENT_TYPE;
            case QUESTION_ID:
                return QuestionItem.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unkonw URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initalValues) {
        // TODO Auto-generated method stub
        if (sUriMatcher.match(uri) != QUESTION) {
            throw new IllegalArgumentException("Unkonw URI" + uri);
        }
        
        ContentValues values;
        if (initalValues != null) {
            values = new ContentValues(initalValues);
        } else {
            values = new ContentValues();
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy年MM月dd日 HH时mm分ss秒");
        String now = sdf.format(new Date());  
        if (values.containsKey(SpeakOut.QuestionItem.CREATEDDATE) == false) {
            values.put(SpeakOut.QuestionItem.CREATEDDATE, now);
        }      
        
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(NOTES_TABLE_NAME, QuestionItem.CONTENT, values);
        if (rowId > 0) {
            Log.i(TAG, "rowId = " + rowId);
            Uri noteUri = ContentUris.withAppendedId(SpeakOut.QuestionItem.CONTENT_URI, rowId);
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
            case QUESTION:
                qb.setTables(NOTES_TABLE_NAME);
                qb.setProjectionMap(sQuestionItemsProjectionMap);
                break;
                
            case QUESTION_ID:
                qb.setTables(NOTES_TABLE_NAME);
                qb.setProjectionMap(sQuestionItemsProjectionMap);
                qb.appendWhere(QuestionItem._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unkonw URI" + uri);
                
        }
        
        //String orderBy
        
        // db operate
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null,
                SpeakOut.QuestionItem.DEFAULT_SORT_ORDER);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case QUESTION:
                count = db.update(NOTES_TABLE_NAME, values, selection, selectionArgs);
                break;
            case QUESTION_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.update(NOTES_TABLE_NAME, values,
                        QuestionItem._ID + "=" + noteId
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
