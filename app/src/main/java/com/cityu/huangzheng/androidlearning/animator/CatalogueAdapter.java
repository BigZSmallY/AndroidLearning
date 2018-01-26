package com.cityu.huangzheng.androidlearning.animator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
