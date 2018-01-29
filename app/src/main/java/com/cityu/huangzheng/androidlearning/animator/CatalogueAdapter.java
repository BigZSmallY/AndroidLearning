package com.cityu.huangzheng.androidlearning.animator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cityu.huangzheng.androidlearning.R;

/**
 * Created by xiaoyan on 2018/1/26.
 */

public class CatalogueAdapter extends BaseAdapter {
    private Context mContext;

    public CatalogueAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return AnimateCatalogue.values().length;
    }

    @Override
    public Object getItem(int position) {
        return AnimateCatalogue.values()[position].getAnimator();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.animator_item,null,false);
        TextView t = (TextView)v.findViewById(R.id.list_item_text);
        Object o = getItem(position);
        int start = o.getClass().getName().lastIndexOf(".") + 1;
        String name = o.getClass().getName().substring(start);
        t.setText(name);
        v.setTag(getItem(position));
        return v;
    }
}
