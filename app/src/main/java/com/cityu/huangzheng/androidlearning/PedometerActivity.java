package com.cityu.huangzheng.androidlearning;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cityu.huangzheng.androidlearning.service.PedometerService;

import java.text.DecimalFormat;

public class PedometerActivity extends FragmentActivity implements
        OnClickListener
{
    private final String TAG = "PedometerActivity";
    private TextView          mTimes;
    private TextView          mStatusText;
    private Button            mBeginButton;
    private Button            mEndButton;
    private TextView          mHeatConsumption;
    private ProgressDialog    mTrainDialog;
    private int               mSteps;
    private int               mHeat;
    private EditText          tv;
    private int               isEffective;
    private BroadcastReceiver mReceiver;
    private DecimalFormat     df;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_times);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mBeginButton = (Button) findViewById(R.id.walkBegin);
        mEndButton = (Button) findViewById(R.id.walkEnd);
        mTimes = (TextView) findViewById(R.id.walkTime);
        mStatusText = (TextView) findViewById(R.id.walkState);
        mHeatConsumption = (TextView) findViewById(R.id.heatConsumption);
        df = new DecimalFormat("#.000");
        mBeginButton.setOnClickListener(this);
        mEndButton.setOnClickListener(this);

        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                switch (intent.getIntExtra("flag", -1))
                {
                    case 0:
                        mTrainDialog.dismiss();
                        break;
                    case 1:
                        int steps = intent.getIntExtra("steps", 0);
                            mTimes.setText(steps + "");
                        break;
                    case 2:
                        String isEffective = intent.getStringExtra("isEffective");
                        mStatusText.setText(isEffective);
                        break;
                    default:
                        break;
                }
            }
        };
        IntentFilter receiverFilter = new IntentFilter("com.cityU.hz.PedometerUpdateUi");
        registerReceiver(mReceiver, receiverFilter);
        startService(new Intent(PedometerActivity.this, PedometerService.class));

        mTrainDialog = new ProgressDialog(PedometerActivity.this);
        mTrainDialog.setTitle(getString(R.string.loading));
        mTrainDialog.setMessage(getString(R.string.initial));
        mTrainDialog.show();

        Intent isTrainOver = new Intent("com.cityU.hz.pedometer");
        isTrainOver.putExtra("flag", 4);
        sendBroadcast(isTrainOver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            showTips();
        }
        return false;
    }

    private void showTips()
    {
        tv = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.backTip)
                        // .setMessage(getString(R.string.walkTips) + times.getText())
                .setMessage(getResources().getString(R.string.savaData))
                .setView(tv)
                .setPositiveButton(R.string.positive,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                Intent stopService = new Intent("com.cityU.hz.pedometer");
                                stopService.putExtra("flag", 0);
                                stopService.putExtra("tips", tv.getText().toString());
                                sendBroadcast(stopService);
                                finish();
                            }
                        })
                .setNegativeButton(R.string.negative,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                Intent stopService = new Intent("com.cityU.hz.pedometer");
                                stopService.putExtra("flag", 0);
                                sendBroadcast(stopService);
                                finish();
                            }
                        }).create();
        dialog.show();
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {
        Button button = (Button) v;
        if (button.equals(mBeginButton))
        {

            if (((String) button.getText()).equals(getString(R.string.begin)))
            {
                button.setText(getString(R.string.pause));
                mStatusText.setText(getString(R.string.begin));
                Intent startStatistic = new Intent("com.cityU.hz.pedometer");
                startStatistic.putExtra("flag", 1);
                sendBroadcast(startStatistic);
            }
            else if (((String) button.getText())
                    .equals(getString(R.string.pause)))
            {

                button.setText(getString(R.string.begin));
                mStatusText.setText(getString(R.string.pause));
                Intent startStatistic = new Intent("com.cityU.hz.pedometer");
                startStatistic.putExtra("flag", 2);
                sendBroadcast(startStatistic);
            }
        }
        else if (button.equals(mEndButton))
        {

            mTimes.setText(getString(R.string.zero));
            mStatusText.setText(getString(R.string.end));
            mBeginButton.setText(getString(R.string.begin));
            mHeatConsumption.setText(R.string.zero);
            mSteps = 0;
            Intent startStatistic = new Intent("com.cityU.hz.pedometer");
            startStatistic.putExtra("flag", 3);
            sendBroadcast(startStatistic);
        }

    }
}