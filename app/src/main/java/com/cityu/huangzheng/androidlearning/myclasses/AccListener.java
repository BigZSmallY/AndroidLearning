package com.cityu.huangzheng.androidlearning.myclasses;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by HZ on 2015/2/17.
 */
public class AccListener implements SensorEventListener
{
    private final String TAG= "AccListener";
//    private DataNotify mNotify;
//
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        Log.d(TAG, "onAccuracyChanged");
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
//        mNotify.dealData(event.values);
    }
//
//    public void setNotify(DataNotify notify)
//    {
//        mNotify = notify;
//    }
}
