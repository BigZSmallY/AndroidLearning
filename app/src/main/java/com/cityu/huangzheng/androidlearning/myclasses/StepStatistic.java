package com.cityu.huangzheng.androidlearning.myclasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Environment;
import android.util.Log;

import com.cityu.huangzheng.androidlearning.R;
import com.cityu.huangzheng.androidlearning.service.PedometerService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import weka.classifiers.trees.M5P;
import weka.core.Instance;

/**
 * Created by HZ on 2015/2/17.
 */
public class StepStatistic implements SensorEventListener {
    //define constant variable.
    private final String TAG = "StepStatistic";
    private final int TRAIN_POINT_NUM = 100;
    private final int WINDOW_TIME = 6000;
    private final int SMOOTH_TIME_DISTANCE = 200;
    private float MIN_INTERVAL = 1.5f;
    private float MAX_INTERVAL = 5f;
    private int MIN_TIME_INTERVAL = 100;
    private int MAX_TIME_INTERVAL = 500;
    private float MIN_MEANS = 10f;
    private float MAX_MEANS = 22;
    private final long mTrainStartTime;

    //define local variable
    public boolean mIsStart;
    private int mSmoothFactor;
    private int mAccWindowLength;
    private int mAccIndex;
    private int mSampleSequence;
    private int mStep;
    private int mTrainPoints;
    private M5P mTree;
    private double[] mInstance;
    private float[] mAccWindow;
    private float[] mAccWindowBuffer;
    private float[] mAccWaveMax;

    //save data
    private StringBuilder accBeforeSmooth;
    private StringBuilder xBeforeAcc;
    private StringBuilder xAfterAcc;
    private StringBuilder accAfterSmooth;
    private StringBuilder accWaveMax;
    private StringBuilder character;

    //update UI
    private PedometerService.UpdateUIListener mUpdateUIListener;

    public StepStatistic(Context mContext, PedometerService.UpdateUIListener listener) {
        accWaveMax = new StringBuilder();
        accBeforeSmooth = new StringBuilder();
        accAfterSmooth = new StringBuilder();
        character = new StringBuilder();
        xBeforeAcc = new StringBuilder();
        xAfterAcc = new StringBuilder();
        try {
            ObjectInputStream ois = new ObjectInputStream(mContext.getResources().openRawResource(R.raw.tree));
            mTree = (M5P) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mUpdateUIListener = listener;
        mTrainStartTime = System.currentTimeMillis();
    }

    public boolean isTrainOver() {
        return mTrainPoints > TRAIN_POINT_NUM;
    }

    public void initial() {
        mAccIndex = 0;
        mStep = 0;
        accAfterSmooth.delete(0, accAfterSmooth.length());
        accWaveMax.delete(0, accWaveMax.length());
        character.delete(0, character.length());
    }

    private void dealData(float[] data) {
        //Train period to get Sample Frequency and Initial.
        if (TRAIN_POINT_NUM > mTrainPoints) {
            mTrainPoints++;
            return;
        } else if (TRAIN_POINT_NUM == mTrainPoints) {
            final long trainEndTime = System.currentTimeMillis();
            mSampleSequence = (int) ((trainEndTime - mTrainStartTime) / mTrainPoints);
            mAccWindowLength = WINDOW_TIME / mSampleSequence;
            mAccWindow = new float[mAccWindowLength];
            mAccWindowBuffer = new float[mAccWindowLength];
            mAccWaveMax = new float[mAccWindowLength];
            mSmoothFactor = SMOOTH_TIME_DISTANCE / mSampleSequence;
            Log.d(TAG,
                    String.valueOf("mSampleSequence:" + mSampleSequence
                            + ",mSmoothFactor:" + mSmoothFactor));
            mTrainPoints = Integer.MAX_VALUE;
            mUpdateUIListener.dismissDialog();
        }

        if (!mIsStart) {
            return;
        }
        //Get Data Period (save date to float[][] Array)
        float acc = (float) Math.sqrt(data[0] * data[0] + data[1] * data[1]
                + data[2] * data[2]);

        if (mAccWindowLength > mAccIndex) {
            xBeforeAcc.append(data[0]);
            xBeforeAcc.append(" ");
            // Calculate combine accelerate.
            mAccWindowBuffer[mAccIndex] = acc;
            mAccIndex++;
            return;
        } else {
            xBeforeAcc.append("\r\n");
            xBeforeAcc.append(data[0]);
            xBeforeAcc.append(" ");
            //Swap buffer and save this data.
            float[] temp = mAccWindowBuffer;
            mAccWindowBuffer = mAccWindow;
            mAccWindow = temp;
            mAccWindowBuffer[0] = acc;
            mAccIndex = 1;
        }

        Log.d(TAG, System.currentTimeMillis() + "dataCollectOver");
        new Thread(new DealData(mAccWindow)).start();
    }

    private void smooth(float[] acc, int smoothFactor) {
        if (smoothFactor % 2 == 0) {
            smoothFactor--;
        }
        for (int i = 1; i < acc.length - 1; i++) {

            float sum = 0;
            int k = 1;
            for (; k <= smoothFactor / 2 && (i + k) < acc.length
                    && (i - k) >= 0; k++) {
                sum += acc[i - k] + acc[i + k];
            }
            sum += acc[i];
            acc[i] = sum / (2 * k - 1);
        }
    }

    private void searchWaveMax(float[] acc, float[] waveMax) {
        //first clear waveMax array.
        for (int i = 0; i < waveMax.length; i++) {
            waveMax[i] = 0;
        }

        // search extremely point.
        final int MAX = 0, MIN = 1;
        int preIndex = 0;
        int preStatus = MAX;
        for (int i = 1; i < acc.length - 1; i++) {
            //search max
            if (acc[i] > acc[i - 1] && acc[i] > acc[i + 1]) {
                //first max
                if (preIndex == 0) {
                    preIndex = i;
                    preStatus = MAX;
                    waveMax[i] = acc[i];
                } else {
                    //pre is max
                    if (preStatus == MAX) {
                        if (acc[preIndex] > acc[i]) {
                            waveMax[i] = 0;
                        } else {
                            waveMax[preIndex] = 0;
                            waveMax[i] = acc[i];
                            preIndex = i;
                        }
                    }
                    //pre is min
                    else {
                        if (Math.abs(acc[i] - acc[preIndex]) < 1.5) {
                            continue;
                        }
                        waveMax[i] = acc[i];
                        preIndex = i;
                        preStatus = MAX;
                    }
                }
            }
            //search min
            else if (acc[i] < acc[i - 1] && acc[i] < acc[i + 1]) {
                if (preIndex == 0) {
                    preIndex = i;
                    preStatus = MIN;
                    waveMax[i] = acc[i];
                } else {
                    if (preStatus == MIN) {
                        if (acc[preIndex] < acc[i]) {
                            waveMax[i] = 0;
                        } else {
                            waveMax[preIndex] = 0;
                            waveMax[i] = acc[i];
                            preIndex = i;
                        }
                    } else {
                        if (Math.abs(acc[i] - acc[preIndex]) < 1.5) {
                            continue;
                        }
                        waveMax[i] = acc[i];
                        preIndex = i;
                        preStatus = MIN;
                    }
                }
            }
        }
    }

    private double[] extraCharac(float[] accWaveMax) {
        double[] character = new double[6];

        ArrayList<Float> waveMax = new ArrayList<Float>();
        ArrayList<Float> distanceOfMaxAndMin = new ArrayList<Float>();
        ArrayList<Float> timeOfMaxAndMin = new ArrayList<Float>();
        float preExtre = 0;
        int preExtreIndex = 0;

        for (int i = 0; i < accWaveMax.length; i++) {
            if (Math.abs(accWaveMax[i]) > 0.00000001) {
                if (preExtreIndex != 0) {
                    distanceOfMaxAndMin.add((float) (Math.abs(accWaveMax[i]
                            - preExtre)));
                    timeOfMaxAndMin.add((float) (i - preExtreIndex));

                    if (waveMax.size() == 0) {
                        waveMax.add(accWaveMax[i] > preExtre ? accWaveMax[i]
                                : preExtre);
                    } else if (accWaveMax[i] > preExtre) {
                        waveMax.add(accWaveMax[i]);
                    }
                }
                preExtreIndex = i;
                preExtre = accWaveMax[i];
            }
        }

        float[] maxWaveArray = new float[waveMax.size()];
        float[] distanceOfMaxAndMinArray = new float[distanceOfMaxAndMin.size()];
        float[] timeOfMaxAndMinArray = new float[timeOfMaxAndMin.size()];
        for (int i = 0; i < maxWaveArray.length; i++) {
            maxWaveArray[i] = waveMax.get(i);
        }
        for (int i = 0; i < distanceOfMaxAndMinArray.length; i++) {
            distanceOfMaxAndMinArray[i] = distanceOfMaxAndMin.get(i);
        }
        for (int i = 0; i < timeOfMaxAndMinArray.length; i++) {
            timeOfMaxAndMinArray[i] = timeOfMaxAndMin.get(i);
        }

        character[0] = cmpMeans(maxWaveArray);
        character[1] = cmpVariance(maxWaveArray);
        character[2] = cmpMeans(distanceOfMaxAndMinArray);
        character[3] = cmpVariance(distanceOfMaxAndMinArray);
        character[4] = cmpVariance(timeOfMaxAndMinArray);
        character[5] = maxWaveArray.length;
        return character;
    }

    private float cmpVariance(float[] accWaveMax) {
        if (accWaveMax == null || accWaveMax.length == 0)
            return 0;

        int waveMaxCount = 0;
        float sum = 0;
        float means;
        float variance = 0;
        float max = accWaveMax[0];
        float min = accWaveMax[0];

        //in case the accidental data, so exclude the max and the min data.
        for (int i = 0; i < accWaveMax.length; i++) {
            if (accWaveMax[i] > max) {
                max = accWaveMax[i];
            }
            if (accWaveMax[i] < min) {
                min = accWaveMax[i];
            }
        }

        for (int i = 0; i < accWaveMax.length; i++) {
            if (accWaveMax[i] != 0 && (max - accWaveMax[i]) > 0.00000001
                    && (accWaveMax[i] - min) > 0.00000001) {
                sum += accWaveMax[i];
                waveMaxCount++;
            }
        }

        if (waveMaxCount == 0)
            return 0;

        means = sum / waveMaxCount;

        for (int i = 0; i < accWaveMax.length; i++) {
            if (accWaveMax[i] != 0 && (max - accWaveMax[i]) > 0.00000001
                    && (accWaveMax[i] - min) > 0.00000001) {
                variance += (accWaveMax[i] - means) * (accWaveMax[i] - means);
            }
        }
        return variance / waveMaxCount;
    }

    private float cmpMeans(float[] data) {
        if (data == null || data.length == 0)
            return 0;

        float sum = 0;
        int count = 0;
        float max, min;
        max = min = data[0];

        for (int i = 0; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
            if (data[i] < min) {
                min = data[i];
            }
        }

        for (float each : data) {
            if (each > 0.00000001 && (each - min) > 0.00000001
                    && max - each > 0.00000001) {
                sum += each;
                count++;
            }
        }
        if (0 != count) {
            return sum / count;
        } else {
            return 0;
        }
    }

    public void saveData(String tips) {
        String rootPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        String filePath = rootPath + File.separator + "StepTimesData" + File.separator + tips + System.nanoTime() + File.separator;
        File accelerometerDataDir = new File(filePath);
        if (!accelerometerDataDir.exists()) {
            boolean result = accelerometerDataDir.mkdirs();
            if (!result) {
                return;
            }
        }
        saveData(accBeforeSmooth.toString(), "ab", filePath);
        saveData(accWaveMax.toString(), "am", filePath);
        saveData(accAfterSmooth.toString(), "aa", filePath);
        saveData(character.toString(), "character", filePath);
        saveData(xBeforeAcc.toString(), "xb", filePath);
    }

    private void saveData(String data, String fileName, String filePath) {
        File file = new File(filePath, fileName + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(data);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        dealData(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged");
    }

    private void saveBeforeSmoothData() {
        for (int i = 0; i < mAccWindow.length; i++) {
            accBeforeSmooth.append(mAccWindow[i]);
            accBeforeSmooth.append("   ");
        }
        accBeforeSmooth.append("\r\n");
    }

    private void saveAfterSmoothData() {
        //Save After Smooth Data
        for (int i = 0; i < mAccWindow.length; i++) {
            accAfterSmooth.append(mAccWindow[i]);
            accAfterSmooth.append("   ");
        }
        accAfterSmooth.append("\r\n");
    }

    private void saveWaveMaxData() {
        for (int i = 0; i < mAccWaveMax.length; i++) {
            accWaveMax.append(mAccWaveMax[i]);
            accWaveMax.append(" ");
        }
        accWaveMax.append("\r\n");
    }


    class DealData implements Runnable {
        private float[] mAccWindow;

        public DealData(float[] accData) {
            mAccWindow = accData;
        }

        @Override
        public void run() {
            saveBeforeSmoothData();

            //smooth wave.
            smooth(mAccWindow, mSmoothFactor);


            //search wave max and wave max time interval.
            searchWaveMax(mAccWindow, mAccWaveMax);


            //extract character.
            double[] characterArray = extraCharac(mAccWaveMax);

            //judge effective.
            mInstance = characterArray;
            double isEffective = 0.5;
            try {
                Instance instance = new Instance(6, mInstance);
                isEffective = mTree.classifyInstance(instance);
                Log.d(TAG, "result:" + isEffective);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //walk
            if (isEffective > 5 && isEffective < 15) {
                MIN_INTERVAL = 2.0f;
                MAX_INTERVAL = 7.0f;
                MIN_TIME_INTERVAL = 150;
                MAX_TIME_INTERVAL = 600;
            }
            //down stairs and upstairs
            else if (isEffective > 15 && isEffective < 25) {
                MIN_INTERVAL = 3.0f;
                MAX_INTERVAL = 9.0f;
                MIN_TIME_INTERVAL = 150;
                MAX_TIME_INTERVAL = 600;
            }
            //run
            else if (isEffective > 25 && isEffective < 35) {
                MIN_INTERVAL = 9.0f;
                MAX_INTERVAL = 20f;
                MIN_TIME_INTERVAL = 50;
                MAX_TIME_INTERVAL = 400;
            }
            //put phone in trousers pocket.
            else if (isEffective > 35 && isEffective < 45) {
                smooth(mAccWindow, 2 * mSmoothFactor);
                searchWaveMax(mAccWindow, mAccWaveMax);
                characterArray = extraCharac(mAccWaveMax);
                MIN_INTERVAL = 0.5f;
                MAX_INTERVAL = 12f;
                MIN_TIME_INTERVAL = 50;
                MAX_TIME_INTERVAL = 600;
            } else {
                return;
            }

            //save data
            saveAfterSmoothData();
            saveWaveMaxData();

            //Statistic steps
            int windowStep = 0;
            if (isEffective > 0) {
                mUpdateUIListener.updateEffective("有效");

                int maxCount = (int) characterArray[5];
                float firstExtre = 0, secondExtre = 0, thirdExtre = 0;
                int firstIndex = 0, secondIndex = 0, thirdIndex = 0;
                int maxIndex = 0;

                for (int i = 0; i < mAccWaveMax.length; i++) {
                    if (mAccWaveMax[i] > 0.0000001) {
                        if (firstExtre < 0.0000001) {
                            firstExtre = mAccWaveMax[i];
                            firstIndex = i;
                        } else if (secondExtre < 0.0000001) {
                            secondExtre = mAccWaveMax[i];
                            secondIndex = i;

                            float interval = mAccWaveMax[firstIndex] - mAccWaveMax[secondIndex];
                            int timeInterval = (secondIndex - firstIndex) * mSampleSequence;
                            if (interval > MIN_INTERVAL && interval < MAX_INTERVAL && timeInterval > MIN_TIME_INTERVAL && timeInterval < MAX_TIME_INTERVAL) {
                                maxIndex++;
                                windowStep++;
                                firstExtre = secondExtre;
                                firstIndex = secondIndex;
                                secondExtre = 0;
                                secondIndex = 0;
                            } else if (interval > 0) {
                                maxIndex++;
                                firstExtre = secondExtre;
                                firstIndex = secondIndex;
                                secondExtre = 0;
                                secondIndex = 0;
                            }

                            if (maxCount - 1 == maxIndex) {
                                if (-interval > MIN_INTERVAL && -interval < MAX_INTERVAL && timeInterval > MIN_TIME_INTERVAL && timeInterval < MAX_TIME_INTERVAL) {
                                    windowStep++;
                                    break;
                                }
                            }
                        } else if (thirdExtre < 0.0000001) {
                            thirdExtre = mAccWaveMax[i];
                            thirdIndex = i;
                            maxIndex++;

                            float firstInterval = secondExtre - firstExtre;
                            float secondInterval = secondExtre - thirdExtre;
                            int firstTimeInterval = (secondIndex - firstIndex) * mSampleSequence;
                            int secondTimeInterval = (thirdIndex - secondIndex) * mSampleSequence;


                            if (firstInterval > MIN_INTERVAL && secondInterval > MIN_INTERVAL
                                    && firstInterval < MAX_INTERVAL && secondInterval < MAX_INTERVAL
                                    && firstTimeInterval > MIN_TIME_INTERVAL && secondTimeInterval > MIN_TIME_INTERVAL
                                    && firstTimeInterval < MAX_TIME_INTERVAL && secondTimeInterval < MAX_TIME_INTERVAL
                                    && secondExtre > MIN_MEANS && secondExtre < MAX_MEANS) {
                                windowStep++;
                            }

                            firstExtre = thirdExtre;
                            firstIndex = thirdIndex;
                            secondExtre = 0;
                            secondIndex = 0;
                            thirdExtre = 0;
                            thirdIndex = 0;
                        }
                    }
                }
            } else {
                mUpdateUIListener.updateEffective("无效");
            }

            //Save Variance And Time Interval As Character.
            characterArray[5] = windowStep;
            for (double each : characterArray) {
                character.append(each);
                character.append("  ");
            }
            character.append(isEffective);
            character.append("  ");
            character.append("\r\n");
            //update UI
            Log.d(TAG, System.currentTimeMillis() + "dataDealOver");
            mUpdateUIListener.updateSteps(mStep += windowStep);
        }
    }
}


