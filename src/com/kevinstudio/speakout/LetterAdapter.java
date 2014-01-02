package com.kevinstudio.speakout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class LetterAdapter extends BaseAdapter {
    private String[] letter = {};
    
    public static final int FIRST_LETTER_ITEM = 0;
    public static final int WORK_ITEM = 1;
    public static final int VIEW_TYPE_COUNT = WORK_ITEM + 1;
    private Context mContext = null;
    
    public LetterAdapter(Context context, String[] data) {
        mContext = context;
        letter = data;
    }
    
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (letter[position].length() == 1) {
            return FIRST_LETTER_ITEM;
        } else {
            return WORK_ITEM;
        }
    }
    
    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return letter[position].length() != 1;
    }
    
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return letter.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return letter[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            if (getItemViewType(position) == FIRST_LETTER_ITEM) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.first_letter_item, parent, false);
                vh.tv = (TextView) convertView.findViewById(R.id.firstletter);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.word_item, parent, false);
                vh.tv = (TextView) convertView.findViewById(R.id.word);
            }
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(letter[position]);
        return convertView;
    }
    
    class ViewHolder{
        TextView tv;
    }
}
