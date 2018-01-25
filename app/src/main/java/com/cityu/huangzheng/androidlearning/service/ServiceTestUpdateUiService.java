package com.cityu.huangzheng.androidlearning.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.cityu.huangzheng.androidlearning.myclasses.StepStatistic;

/**
 * Created by HZ on 2015/2/15.
 */
public class ServiceTestUpdateUiService extends Service
{
    private final String TAG = "DealDataService";

    private boolean flag = true; //Stop Thread
    private BroadcastReceiver mStopServiceReceiver;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardLock;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        new UpdateUI().start();

        IntentFilter filter = new IntentFilter("com.cityU.hz.stopTestService");
        mStopServiceReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                flag = false;
                unregisterReceiver(this);
                stopSelf();
            }
        };
        registerReceiver(mStopServiceReceiver, filter);

        mPowerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mStopServiceReceiver.isOrderedBroadcast())
        {
            unregisterReceiver(mStopServiceReceiver);
        }

        if(mWakeLock!= null)
        {
            mWakeLock.release();
            mWakeLock = null;
        }

        if(mKeyguardLock != null)
        {
            mKeyguardLock.reenableKeyguard();
        }
        Log.d(TAG, "onDestroy");
    }

    class UpdateUI extends Thread
    {
        @Override
        public void run()
        {
            int i = 0;
            super.run();
            while (flag)
            {
                super.run();
                Intent intent = new Intent("com.cityU.hz.updateUiTest");
                intent.putExtra("data", i++);
                Log.d(TAG, ""+i);
                if(i == 10)
                {
                    Log.d(TAG, "Light screen");
                    mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "Tag");
                    mWakeLock.acquire();
                    //unlock
                    mKeyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
                    mKeyguardLock = mKeyguardManager.newKeyguardLock("");
                    mKeyguardLock.disableKeyguard();
                }
                sendBroadcast(intent);
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
