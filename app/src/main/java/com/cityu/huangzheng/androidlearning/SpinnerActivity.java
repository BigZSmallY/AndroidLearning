package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by huangzheng on 2/4/15.
 */
public class SpinnerActivity extends Activity
{
    private Context mContext;

    private ArrayList<String> mCountries;
    private TextView mTextView;
    private Spinner mSpinner;
    private EditText mEditText;
    private Button mAddButton;
    private Button mRemoveButton;
    private ArrayAdapter<String> mAdapter;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.spinner_layout);

        mCountries = new ArrayList<String>();
        mCountries.add("Beijing");
        mCountries.add("Shanghai");
        mCountries.add("Chongqing");
        mCountries.add("Guangzhou");
        mCountries.add("Nanjing");

        mTextView = (TextView) findViewById(R.id.mSpinnerText);
        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        mEditText = (EditText)findViewById(R.id.mSpinnerAddEditText);
        mEditText.clearFocus();
        mAddButton = (Button)findViewById(R.id.mSpinnerAddButton);
        mRemoveButton = (Button)findViewById(R.id.mSpinnerRemoveButton);


        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCountries);
        mAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selected = mCountries.get(position);
                mTextView.setText("You have select:" + selected);
                mEditText.setText(selected);
                mSpinner.setAlpha(1.0f);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        mAnimation = AnimationUtils.loadAnimation(this, R.anim.spinner_anim);

        mSpinner.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                v.startAnimation(mAnimation);
                mSpinner.setAlpha(0.2f);
                return false;
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String text = mEditText.getText().toString();
                if(null != text)
                {
                    if(!mCountries.contains(text) && mCountries.add(text))
                    {
                        Toast.makeText(mContext, "Add Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Add Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        mRemoveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String text = mEditText.getText().toString();
                if(mCountries.contains(text))
                {
                    mCountries.remove(text);
                    Toast.makeText(mContext, "Remove Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(mContext, "This Country is not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
