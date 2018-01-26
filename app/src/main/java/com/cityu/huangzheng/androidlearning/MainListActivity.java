package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzheng on 2/4/15.
 */
public class MainListActivity extends Activity
{
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = this;
        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listData()));
        setContentView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(MainListActivity.this, ViewFlipperActivity.class);
                        break;
                    case 1:
                        intent = new Intent(MainListActivity.this, AutoShowTextViewActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainListActivity.this, SpinnerActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MainListActivity.this, HelloJniActivity.class);
                        break;
                    case 4:
                        intent = new Intent(MainListActivity.this, GalleryActivity.class);
                        break;
                    case 5:
                        intent = new Intent(MainListActivity.this, CanvasDrawActivity.class);
                        break;
                    case 6:
                        intent = new Intent(MainListActivity.this, OpenGLActivity.class);
                        break;
                    case 7:
                        intent = new Intent(MainListActivity.this, ServiceTestActivity.class);
                        break;
                    case 8:
                        intent = new Intent(MainListActivity.this, PedometerActivity.class);
                        break;
                    case 9:
                        intent = new Intent(MainListActivity.this, AnimatorActivity.class);
                    default:
                        Toast.makeText(mContext, "Developing", Toast.LENGTH_SHORT).show();
                        break;
                }
                startActivity(intent);
            }
        });
    }

    private List<String> listData()
    {
        List<String> list = new ArrayList<String>();
        list.add("1.ViewFlipper");
        list.add("2.AutoShowTextView");
        list.add("3.Spinner");
        list.add("4.Jni NDK");
        list.add("5.Gallery");
        list.add("6.CanvasDraw");
        list.add("7.OpenGL SurfaceView");
        list.add("8.Service&BroadCast");
        list.add("9.Pedometer");
        list.add("10.Animator");
        return list;
    }


    //Menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, getString(R.string.about));
        menu.add(0, 1, 1, getString(R.string.exit));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case 0:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.about))
                        .setMessage("CityU HuangZheng 1.0 Version")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                            }
                        }).show();
                break;
            case 1:
                finish();
                break;
        }
        return true;
    }
}
