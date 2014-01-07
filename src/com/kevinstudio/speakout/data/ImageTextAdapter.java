package com.kevinstudio.speakout.data;

import com.kevinstudio.speakout.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageTextAdapter extends BaseAdapter {
    
    private LayoutInflater mInflater;
    private Bitmap mIcon1;
    private String[] mData;
    private int  selectItem=-1; 
    private Context mContext;

    public ImageTextAdapter(Context context, String data[]) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
        // Icons bound to the rows.
        mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.stepforwardhot);
//        mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_2);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
     // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.voice_recognition_speak_mode_list, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.text.setText(mData[position]);
        holder.icon.setImageBitmap(mIcon1);
        
      //如果位置相同则设置背景为黄色
        if (position == selectItem) {  
            convertView.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));  
        }   
        else {  
            convertView.setBackgroundColor(Color.TRANSPARENT);  
        } 

        return convertView;
    }
    
    public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
   } 
    
    static class ViewHolder {
        TextView text;
        ImageView icon;
    }

}
