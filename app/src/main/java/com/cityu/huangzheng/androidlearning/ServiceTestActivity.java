package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cityu.huangzheng.androidlearning.service.ServiceTestUpdateUiService;

/**
 * Created by HZ on 2015/2/15.
 */
public class ServiceTestActivity extends Activity
{
    private final String TAG = "ServiceTestActivity";
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_test_layout);
        Button stopServiceButton = (Button) findViewById(R.id.stop_service_button);
        final TextView helloWorld = (TextView) findViewById(R.id.helloWorld);

        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                helloWorld.setText(intent.getIntExtra("data", 0) + "");
            }
        };

        stopServiceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendBroadcast(new Intent("com.cityU.hz.stopTestService"));
            }
        });

        IntentFilter updateUiFilter = new IntentFilter("com.cityU.hz.updateUiTest");
        registerReceiver(mReceiver, updateUiFilter);
        startService(new Intent(this, ServiceTestUpdateUiService.class));
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
