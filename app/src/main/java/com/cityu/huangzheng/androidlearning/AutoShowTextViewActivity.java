package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by huangzheng on 2/4/15.
 */
public class AutoShowTextViewActivity extends Activity
{
    private EditText mEditText;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_show_text_view);

        mEditText = (EditText) findViewById(R.id.mEditText);
        mTextView = (TextView) findViewById(R.id.mTextView);
        mEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mTextView.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }
}
