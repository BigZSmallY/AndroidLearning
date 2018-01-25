package com.cityu.huangzheng.androidlearning.service;

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

public class PedometerService extends Service {
    private final String TAG = "PedometerService";

    private boolean flag = true; //Stop Thread
    private StepStatistic mStepStatistic;//AccListener & Statistic steps.
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private BroadcastReceiver mReceiver;
    private PowerManager.WakeLock mWakeLock;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();
        new Thread(new PedometerServiceThread(this)).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(mStepStatistic);
        if (mReceiver.isOrderedBroadcast()) {
            unregisterReceiver(mReceiver);
        }
        mWakeLock.release();
        Log.d(TAG, "onDestroy");
    }

    public interface UpdateUIListener {
        public void dismissDialog();

        public void updateSteps(int steps);

        public void updateHeat(double heat);

        public void updateEffective(String isEffective);
    }

    class MyUpdateUIListener implements UpdateUIListener {
        @Override
        public void dismissDialog() {
            Intent intent = new Intent("com.cityU.hz.PedometerUpdateUi");
            intent.putExtra("flag", 0);
            sendBroadcast(intent);
        }

        @Override
        public void updateSteps(int steps) {
            Intent intent = new Intent("com.cityU.hz.PedometerUpdateUi");
            intent.putExtra("flag", 1);
            intent.putExtra("steps", steps);
            sendBroadcast(intent);
        }

        @Override
        public void updateHeat(double heat) {

        }

        @Override
        public void updateEffective(String isEffective) {
            Intent intent = new Intent("com.cityU.hz.PedometerUpdateUi");
            intent.putExtra("flag", 2);
            intent.putExtra("isEffective", isEffective);
            sendBroadcast(intent);
        }
    }

    class PedometerServiceThread implements Runnable {

        private Context mContext;

        public PedometerServiceThread(Context context) {
            this.mContext = context;
        }

        @Override
        public void run() {
            final MyUpdateUIListener myListener = new MyUpdateUIListener();
            mStepStatistic = new StepStatistic(mContext, myListener);
            mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
            mAccelerometer = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(mStepStatistic, mAccelerometer,
                    SensorManager.SENSOR_DELAY_GAME);

            IntentFilter stopServiceFilter = new IntentFilter("com.cityU.hz.pedometer");
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (intent.getIntExtra("flag", -1)) {
                        //stop service
                        case 0:
                            mSensorManager.unregisterListener(mStepStatistic);
                            String tips = intent.getStringExtra("tips");
                            if (null != tips) {
                                mStepStatistic.saveData(tips);
                            }
                            unregisterReceiver(this);
                            stopSelf();
                            break;
                        //start statistic
                        case 1:
                            mStepStatistic.mIsStart = true;
                            break;
                        //stop statistic
                        case 2:
                            mStepStatistic.mIsStart = false;
                            break;
                        //make zero
                        case 3:
                            mStepStatistic.mIsStart = false;
                            mStepStatistic.initial();
                            break;
                        //is train over to dismiss dialog
                        case 4:
                            if (mStepStatistic.isTrainOver()) {
                                myListener.dismissDialog();
                            }
                            break;
                        default:
                            break;
                    }

                }
            };
            registerReceiver(mReceiver, stopServiceFilter);
        }
    }
}
