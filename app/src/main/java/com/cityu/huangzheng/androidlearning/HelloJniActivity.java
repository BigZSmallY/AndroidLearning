package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by huangzheng on 2/5/15.
 */
public class HelloJniActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        TextView  tv = new TextView(this);
        tv.setText( stringFromJNI() );
        setContentView(tv);
    }


    /* A native method that is implemented by the 'hello-jni' native library, which is packaged with this application. */
    public native String  stringFromJNI();

    public native String  unimplementedStringFromJNI();

    static {
        System.loadLibrary("hello-jni");
    }
}
