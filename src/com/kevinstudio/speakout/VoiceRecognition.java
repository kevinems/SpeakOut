/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevinstudio.speakout;

import com.kevinstudio.speakout.R.drawable;
import com.kevinstudio.speakout.data.ImageTextAdapter;
import com.kevinstudio.speakout.data.Question;
import com.kevinstudio.speakout.provider.SpeakOut;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sample code that invokes the speech recognition intent API.
 */
public class VoiceRecognition extends Activity implements OnClickListener {

    private static final String TAG = "VoiceRecognition";

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    private ListView mList;

    private Button mBtnNext;

    private Button mBtnOnceAgain;
    
    private TextView mTvQuestion;
    
    private TextView mTvAnswer;

    private Handler mHandler;

    private Spinner mSupportedLanguageView;

    private int mCursorId = 0;

    private Question mCurrentQuestion;

    static final String DEFAULT_LANGUAGE = "en-US";

    private String mCurrentLanguge = DEFAULT_LANGUAGE;

    private boolean mLastQuestionAnswer = false;

    private static final int QUESTION_GENERATE_RULE_SEQUENCE = 0;

    private static final int QUESTION_GENERATE_RULE_RANDOM = 1;

    private static final int QUESTION_GENERATE_RULE_WRONG = 2;

    private int mQuestionGenerateRule = QUESTION_GENERATE_RULE_SEQUENCE;

    private Cursor cursor;

    private String[] speakModeStrings;

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.voice_recognition);

        initVariable();

        initLayout();

        // Get display items for later interaction

        // Tell the list view to show one checked/activated item at a time.

        mList = (ListView) findViewById(R.id.voice_recognition_result_list);

        // TextView textView = new TextView(this);
        // textView.setText("headerview test");
        // mList.addHeaderView(textView);

        // mSupportedLanguageView = (Spinner)
        // findViewById(R.id.supported_languages);

        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            mBtnOnceAgain.setEnabled(true);
            mBtnNext.setEnabled(true);
        } else {
            mBtnOnceAgain.setEnabled(false);
            mBtnNext.setEnabled(false);
        }

        // Most of the applications do not have to handle the voice settings. If
        // the application
        // does not require a recognition in a specific language (i.e.,
        // different from the system
        // locale), the application does not need to read the voice settings.
        // refreshVoiceSettings(); // does not need current
    }

    private void initVariable() {
        // get cursor
        cursor = MainActivity.getGlobalCursor();

        // btn listview speak mode
        speakModeStrings = new String[] {
                getString(R.string.speak_button_sequence), getString(R.string.speak_button_random)
        };
    }

    private void initLayout() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
 
        // action bar list
        // 设置actionbar的导航模式
        actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_LIST);
        // 生成一个spinneradaper，设置actionbar下拉菜单的菜单项
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.action_bar_speak_list, android.R.layout.simple_spinner_dropdown_item);
        // 为actionbar设置适配器跟监听器
        actionBar.setListNavigationCallbacks(spinnerAdapter, new DropDownListener());
        
        mTvQuestion = (TextView) findViewById(R.id.voice_recognition_result_question);
        mTvAnswer = (TextView) findViewById(R.id.voice_recognition_result_answer);

        mBtnOnceAgain = (Button) findViewById(R.id.btn_once_again);
        mBtnNext = (Button) findViewById(R.id.btn_next);

        mBtnOnceAgain.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
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

    /**
     * Handle the click on the start recognition button.
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_once_again:
                mCurrentQuestion = generateQuestion(false);
                startVoiceRecognitionActivity();
                break;
            case R.id.btn_next:
                mCurrentQuestion = generateQuestion(true);
                startVoiceRecognitionActivity();
                break;
            default:
                break;
        }
    }

    /**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        // Display an hint to the user about what he should say.
        // intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
        // "Speech recognition demo");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mCurrentQuestion.getContent());

        // Given an hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Specify how many results you want to receive. The results will be
        // sorted
        // where the first result is the one with higher confidence.
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        // Specify the recognition language. This parameter has to be specified
        // only if the
        // recognition has to be done in a specific language and not the default
        // one (i.e., the
        // system locale). Most of the applications do not have to set this
        // parameter.
        // if
        // (!mSupportedLanguageView.getSelectedItem().toString().equals(DEFAULT_LANGUAGE))
        // {
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
        // mSupportedLanguageView.getSelectedItem().toString());
        // }
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, DEFAULT_LANGUAGE);

        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_activated_1, matches);
            mList.setAdapter(arrayAdapter);
            mList.setTextFilterEnabled(true);
            mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            int answerId = judgeAnswer(matches);
            if (answerId >= 0) {
                mList.setItemChecked(answerId, true);
                updateResultView(matches.get(answerId));
            } else {
                updateResultView(matches.get(0));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateResultView(String answer) {
        ImageView imageView = (ImageView) findViewById(R.id.voice_recognition_result_imageview);
        TextView textView = (TextView) findViewById(R.id.voice_recognition_result_textview);

        if (mLastQuestionAnswer) {
            imageView.setImageResource(drawable.correct);
            textView.setText(R.string.voice_recognition_answer_dialog_correct_message);
        } else {
            imageView.setImageResource(drawable.wrong);
            textView.setText(R.string.voice_recognition_answer_dialog_wrong_message);
        }
        mTvQuestion.setText(mCurrentQuestion.getContent());
        mTvAnswer.setText(answer);
        mBtnNext.setText(R.string.voice_recognition_answer_dialog_next);
    }

    /*
     * private void refreshVoiceSettings() { Log.i(TAG, "Sending broadcast");
     * sendOrderedBroadcast(RecognizerIntent.getVoiceDetailsIntent(this), null,
     * new SupportedLanguageBroadcastReceiver(), null, Activity.RESULT_OK, null,
     * null); }
     */

    /*
     * private void updateSupportedLanguages(List<String> languages) { // We add
     * "Default" at the beginning of the list to simulate default language.
     * languages.add(0, "Default"); SpinnerAdapter adapter = new
     * ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,
     * languages.toArray( new String[languages.size()]));
     * mSupportedLanguageView.setAdapter(adapter);
     * mSupportedLanguageView.setSelection(14); // 14 means en-US }
     */

    /*
     * private void updateLanguagePreference(String language) { TextView
     * textView = (TextView) findViewById(R.id.language_preference);
     * textView.setText(language); }
     */

    private Question generateQuestion(boolean next) {
        Question question;

        switch (mQuestionGenerateRule) {
            case QUESTION_GENERATE_RULE_SEQUENCE:
                question = generateSequenceQuestion(next);
                break;

            case QUESTION_GENERATE_RULE_RANDOM:
                question = generateRandomQuestion(next);
                break;

            default:
                question = generateSequenceQuestion(next);
                break;
        }

        return question;
    }

    /**
     * @param next true to get next question, or false to return current
     *            question;
     * @return
     */
    private Question generateSequenceQuestion(boolean next) {
        Question question;

        if (next) {
            if (this.cursor.moveToNext()) {
                mCursorId++;
            } else {
                this.cursor.moveToFirst();
                mCursorId = 0;
            }
            question = generateQuestionByCursor();
        } else {
            if (mCursorId == 0) {
                this.cursor.moveToFirst();
                question = generateQuestionByCursor();
            } else {
                question = mCurrentQuestion;
            }
        }
        return question;
    }

    /**
     * @param next true to get next question, or false to return current
     *            question;
     * @return
     */
    private Question generateRandomQuestion(boolean next) {
        Question question;
        int count = this.cursor.getCount();
        int random = 0;

        Random randomSeed = new Random();
        do {
            random = randomSeed.nextInt(this.cursor.getCount());
        } while ((count > 1) && (random == mCursorId));

        if (next) {
            this.cursor.moveToPosition(random);
            question = generateQuestionByCursor();
        } else {
            if (mCurrentQuestion != null) {
                question = mCurrentQuestion;
            } else {
                this.cursor.moveToPosition(random);
                question = generateQuestionByCursor();
            }

        }

        mCursorId = random;

        return question;
    }

    private Question generateQuestionByCursor() {
        Question question;
        int nameColumnIndex = this.cursor.getColumnIndex(SpeakOut.QuestionItem.CONTENT);
        String content = this.cursor.getString(nameColumnIndex);
        question = new Question(content);

        return question;
    }

    /**
     * @param matches
     * @return -1 means error, >= 0 means equal No.
     */
    private int judgeAnswer(ArrayList<String> matches) {
        String content = Question.getValidContent(mCurrentQuestion.getContent());
        Log.i(TAG, "judgeAnswer content = " + content);
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).equalsIgnoreCase(content)) {
                mLastQuestionAnswer = true;
                return i;
            }
        }
        mLastQuestionAnswer = false;
        return -1;
    }
    
    private class DropDownListener implements OnNavigationListener  
    {  
      
        @Override  
        public boolean onNavigationItemSelected(int itemPosition, long itemId)  
        {  
            // TODO Auto-generated method stub  
            switch (itemPosition) {
                case 0:
                    mQuestionGenerateRule = QUESTION_GENERATE_RULE_SEQUENCE;
                    break;
                case 1:
                    mQuestionGenerateRule = QUESTION_GENERATE_RULE_RANDOM;
                default:
                    break;
            }
            
            return false;
        }  
              
    }

    /**
     * Handles the response of the broadcast request about the recognizer
     * supported languages. The receiver is required only if the application
     * wants to do recognition in a specific language.
     */
    /*
     * private class SupportedLanguageBroadcastReceiver extends
     * BroadcastReceiver {
     * @Override public void onReceive(Context context, final Intent intent) {
     * Log.i(TAG, "Receiving broadcast " + intent); final Bundle extra =
     * getResultExtras(false); if (getResultCode() != Activity.RESULT_OK) {
     * mHandler.post(new Runnable() {
     * @Override public void run() { showToast("Error code:" + getResultCode());
     * } }); } if (extra == null) { mHandler.post(new Runnable() {
     * @Override public void run() { showToast("No extra"); } }); } if
     * (extra.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
     * mHandler.post(new Runnable() {
     * @Override public void run() {
     * updateSupportedLanguages(extra.getStringArrayList(
     * RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)); } }); } if
     * (extra.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
     * mHandler.post(new Runnable() {
     * @Override public void run() { updateLanguagePreference(
     * extra.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)); } }); } }
     * private void showToast(String text) {
     * Toast.makeText(VoiceRecognition.this, text, 1000).show(); } }
     */
}
