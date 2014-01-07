
package com.kevinstudio.speakout;

import com.kevinstudio.speakout.provider.SpeakOut;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewQuestionActivity extends Activity {
    // field
    private Cursor cursor;

    // layout view
    TextView tv_id;

    EditText et_content;

    EditText et_commonLevel;

    EditText et_favor;

    TextView tv_createdDate;

    TextView tv_practiseCount;

    TextView tv_lastPractiseDate;

    TextView tv_wrongCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_question_item);

        initVariable();

        initLayout();
    }

    private void initLayout() {
        // id
        tv_id = (TextView) findViewById(R.id.question_item_activity_id);
        String id = cursor.getString(cursor.getColumnIndex(SpeakOut.QuestionItem._ID));
        tv_id.setText(id);

        // content
        et_content = (EditText) findViewById(R.id.question_item_activity_content);
        String content = cursor.getString(cursor.getColumnIndex(SpeakOut.QuestionItem.CONTENT));
        et_content.setText(content);

        // common level
        et_commonLevel = (EditText) findViewById(R.id.question_item_activity_common_level);
        int commonLevel = cursor.getInt(cursor.getColumnIndex(SpeakOut.QuestionItem.COMMON_LEVEL));
        et_commonLevel.setText(String.valueOf(commonLevel));

        // favor
        et_favor = (EditText) findViewById(R.id.question_item_activity_favor);
        int favor = cursor.getInt(cursor.getColumnIndex(SpeakOut.QuestionItem.FAVOR));
        et_favor.setText(String.valueOf(favor));

        // created date
        tv_createdDate = (TextView) findViewById(R.id.question_item_activity_created_date);
        long createDate = cursor.getLong(cursor.getColumnIndex(SpeakOut.QuestionItem.CREATED_DATE));
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy年MM月dd日 HH时mm分ss秒");
        tv_createdDate.setText(sdf.format(new Date(createDate)));

        // practise count
        tv_practiseCount = (TextView) findViewById(R.id.question_item_activity_practise_count);
        int practiseCount = cursor.getInt(cursor
                .getColumnIndex(SpeakOut.QuestionItem.PRACTISE_COUNT));
        tv_practiseCount.setText(String.valueOf(practiseCount));

        // last practise date
        tv_lastPractiseDate = (TextView) findViewById(R.id.question_item_activity_last_practise_date);
        long lastPractiseDate = cursor.getLong(cursor
                .getColumnIndex(SpeakOut.QuestionItem.LAST_PRACTISE_DATE));
        tv_lastPractiseDate.setText(sdf.format(new Date(lastPractiseDate)));

        // wrong count
        tv_wrongCount = (TextView) findViewById(R.id.question_item_activity_wrong_count);
        int wrongCount = cursor.getInt(cursor.getColumnIndex(SpeakOut.QuestionItem.WRONG_COUNT));
        tv_wrongCount.setText(String.valueOf(wrongCount));
        
        // action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initVariable() {
        cursor = MainActivity.getGlobalCursor();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
