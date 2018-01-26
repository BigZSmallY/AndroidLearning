package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.cityu.huangzheng.androidlearning.animator.CatalogueAdapter;

/**
 * Created by xiaoyan on 2018/1/26.
 */

public class AnimatorActivity extends Activity {
    private TextView mAnimatorText;
    private ListView mCatalogueListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.animator_main);

        mAnimatorText = (TextView) findViewById(R.id.animator_text);
        mCatalogueListView = (ListView) findViewById(R.id.animator_list_view);
        mCatalogueListView.setAdapter(new CatalogueAdapter(this));

    }
}