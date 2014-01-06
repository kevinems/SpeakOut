
package com.kevinstudio.speakout.data;

import com.kevinstudio.speakout.R;
import com.kevinstudio.speakout.R.string;
import com.kevinstudio.speakout.provider.SpeakOut;
import com.kevinstudio.speakout.provider.SpeakOut.QuestionItem;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QustionListAdapter extends CursorAdapter {

    private final String TAG = "QustionListAdapter";
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        ViewHolder holder = (ViewHolder) view.getTag();

        // content
        String content = cursor.getString(cursor.getColumnIndex(SpeakOut.QuestionItem.CONTENT));
        holder.item_tv_content.setText(content);

        // createddate
        long time = cursor.getLong(cursor.getColumnIndex(QuestionItem.CREATED_DATE));

        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy年MM月dd日 HH时mm分ss秒");

        holder.item_tv_createdate.setText(sdf.format(new Date(time)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflate = inflater.inflate(R.layout.library_list_item, null);
        holder.item_tv_content = (TextView) inflate
                .findViewById(R.id.library_question_list_item_content);
        holder.item_tv_createdate = (TextView) inflate
                .findViewById(R.id.library_question_list_item_createddate);

        inflate.setTag(holder);
        return inflate;
    }

    public QustionListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        // TODO Auto-generated constructor stub
    }

    public QustionListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        // TODO Auto-generated constructor stub
    }

    class ViewHolder {
        TextView item_tv_content;

        TextView item_tv_createdate;
    }

}
