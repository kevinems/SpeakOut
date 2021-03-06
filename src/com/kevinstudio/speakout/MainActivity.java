
package com.kevinstudio.speakout;

import com.kevinstudio.speakout.data.Question;
import com.kevinstudio.speakout.data.QustionListAdapter;
import com.kevinstudio.speakout.provider.SpeakOut;
import com.kevinstudio.speakout.provider.SpeakOut.QuestionItem;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    // library list view frament
    private static ContentResolver mCR;

    private static Cursor mCursor;

    private static QustionListAdapter mListAdapter;
    
    private static TextView mTextViewSumary;
    
    private static String[] columnsProject = new String[] {
            QuestionItem._ID, QuestionItem.CONTENT, QuestionItem.COMMON_LEVEL, QuestionItem.FAVOR,
            QuestionItem.CREATED_DATE, QuestionItem.PRACTISE_COUNT,
            QuestionItem.LAST_PRACTISE_DATE, QuestionItem.WRONG_COUNT, QuestionItem.SOUND
    };
    
    private static Uri myUri = SpeakOut.QuestionItem.CONTENT_URI;
    
    private static final int CONTEXT_MENU_LIBRARY_LIST_VIEW_ID = 100000;
    private static final int CONTEXT_MENU_LIBRARY_LIST_DELETE_ID = CONTEXT_MENU_LIBRARY_LIST_VIEW_ID + 1;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        mCR = getContentResolver();
        
     // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
        Log.i(TAG, "onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void notifyDataSetChanged() {
            // TODO Auto-generated method stub
            super.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public static final int FRAGMENT_MAIN_VIEW = 1;

        public static final int FRAGMENT_LIBRARY_LIST = 2;

        public static final int FRAGMENT_OTHER = 3;

        public static LibraryListView mListView;

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = null;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == FRAGMENT_MAIN_VIEW) {
                rootView = createMainView(inflater, container, savedInstanceState);
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == FRAGMENT_LIBRARY_LIST) {
                rootView = createLibraryListView(inflater, container, savedInstanceState);
            } else {
                rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
                TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
                dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            }

            return rootView;
        }

        // FRAGMENT_MAIN_VIEW = 1
        private View createMainView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = null;
            rootView = inflater.inflate(R.layout.fragment_main_view, container, false);
            TextView textViewTips = (TextView) rootView.findViewById(R.id.main_view_tips);

            textViewTips.setText(getString(R.string.mainv_view_tips));

            // edittext content
            final EditText editTextContent = (EditText) rootView
                    .findViewById(R.id.main_view_quick_insert_edittext_content);

            // button clear
            Button buttonClear = (Button) rootView
                    .findViewById(R.id.main_view_quick_insert_button_clear);
            buttonClear.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (editTextContent != null) {
                        editTextContent.setText("");
                    }
                }
            });

            // button commnit
            Button buttonCommit = (Button) rootView
                    .findViewById(R.id.main_view_quick_insert_button_insert);
            buttonCommit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String contentToBeInserted = editTextContent.getText().toString();
                    if (Question.isContentValid(contentToBeInserted)) {

                        // content provider
                        Log.i(TAG, "insert content provider");
                        ContentValues values = new ContentValues();
                        values.put(SpeakOut.QuestionItem.CONTENT, contentToBeInserted);
                        // Uri tempUri =
                        // ContentUris.withAppendedId(SpeakOut.Notes.CONTENT_URI,
                        // 1);
                        // mCR.update(tempUri, values, null, null);
                        mCR.insert(myUri, values);

                        ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
                        Toast.makeText(getActivity().getBaseContext(),
                                R.string.main_view_quick_insert_content_done, Toast.LENGTH_SHORT)
                                .show();
                        
                        // clear edittext content
                        editTextContent.setText("");
                    } else {
                        Toast.makeText(getActivity().getBaseContext(),
                                R.string.main_view_quick_insert_content_invalid, Toast.LENGTH_SHORT)
                                .show();
                    }
                }

            });
            
            ImageButton buttonPractise = (ImageButton) rootView.findViewById(R.id.main_view_quick_practise_button);
            buttonPractise.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity().getBaseContext(), VoiceRecognition.class);
                    startActivity(intent);
                }
            });

            return rootView;
        }

        // FRAGMENT_LIBRARY_LIST = 2
        static private final String LIBRARY_LIST_VIEW_TAG = "library_list_view_tag";

        private View createLibraryListView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = null;
            rootView = inflater.inflate(R.layout.fragment_library_list, container, false);
            
            // listview
            mListView = (LibraryListView) rootView.findViewById(R.id.library_list_questions);
            
            mCursor = mCR.query(myUri, columnsProject, null, null, null);
            if (mCursor != null) {
                
                mListAdapter = new QustionListAdapter(getActivity().getBaseContext(), mCursor, true);
                mListView.setAdapter(mListAdapter);
            } else {
                Log.i(TAG, "cur == null");
            }
            
            // textview sumary
            mTextViewSumary = (TextView) rootView.findViewById(R.id.library_sumary);
            mTextViewSumary.setText(getString(R.string.library_list_view_sumary) + " "
                    + String.valueOf(mCursor.getCount()));
            
            // register ContextMenu for listview
            registerForContextMenu(mListView);
            
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity().getBaseContext(), ViewQuestionActivity.class);
                    startActivity(intent);
                }
            });
            
            return rootView;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO Auto-generated method stub
        return new CursorLoader(this, myUri, columnsProject, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        if (arg1 != null && mListAdapter != null) {
            mCursor = arg1;
            mListAdapter.swapCursor(mCursor);
            updateAllViewsRelatedToCursor();    
        }
    }

    private void updateAllViewsRelatedToCursor() {
        mTextViewSumary = (TextView) findViewById(R.id.library_sumary);
        if (mTextViewSumary != null && mCursor != null) {
            mTextViewSumary.setText(getString(R.string.library_list_view_sumary) + " "
                    + String.valueOf(mCursor.getCount()));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        mListAdapter.swapCursor(null);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        menu.add(menu.NONE, CONTEXT_MENU_LIBRARY_LIST_VIEW_ID, Menu.NONE,
                getString(R.string.library_list_view_context_menu_view));
        menu.add(menu.NONE, CONTEXT_MENU_LIBRARY_LIST_DELETE_ID, Menu.NONE,
                getString(R.string.library_list_view_context_menu_delete));
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            
            case CONTEXT_MENU_LIBRARY_LIST_VIEW_ID: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();

//                String[] selectionArgs = new String[] {
//                    String.valueOf(info.id)
//                };
                
                Intent intent = new Intent(this, ViewQuestionActivity.class);
                startActivity(intent);

                return true;
                // break;
            }
            
            case CONTEXT_MENU_LIBRARY_LIST_DELETE_ID: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();

                String[] selectionArgs = new String[] {
                    String.valueOf(info.id)
                };
                String selection = "" + SpeakOut.QuestionItem._ID + "=?";
                mCR.delete(myUri, selection, selectionArgs);

                return true;
            }
                // break;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mCursor != null) {
            mCursor.close();
        }
    }
    
    static public Cursor getGlobalCursor() {
        return mCursor;
    }
    
    static public void setGlobalCursor(Cursor aCursor) {
        mCursor = aCursor;
    }
}
