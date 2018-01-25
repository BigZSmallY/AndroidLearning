package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by huangzheng on 2/6/15.
 */
public class CanvasDrawActivity extends Activity
{
    private final String TAG = "CanvasDrawActivity";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private boolean mIsNeedDrawBackground = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas_draw_layout);

        mSurfaceView = (SurfaceView) findViewById(R.id.mCanvasDrawSurfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {

                mCanvas = mSurfaceHolder.lockCanvas();
                //Draw white background
                mCanvas.drawColor(getResources().getColor(R.color.white));
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);

                mCanvas = mSurfaceHolder.lockCanvas();
                //Draw white background
                mCanvas.drawColor(getResources().getColor(R.color.white));
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {

            }
        });

        mSurfaceView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_UP:
//                        if(mIsNeedDrawBackground)
//                        {
//                            mCanvas = mSurfaceHolder.lockCanvas();
//                            //Draw white background
//                            mCanvas.drawColor(getResources().getColor(R.color.white));
//                            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
//                            mIsNeedDrawBackground = false;
//
//                            mCanvas = mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
//                            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
//                        }
                        mCanvas = mSurfaceHolder.lockCanvas(new Rect(x-50, y-70, x+50, y+50));
                        Paint paint = new Paint();
                        paint.setColor(getResources().getColor(R.color.green));
                        mCanvas.save();
                        mCanvas.rotate(30, x, y);
                        mCanvas.drawRect(x-50, y-50, x, y, paint);
                        mCanvas.restore();
                        mCanvas.drawRect(x, y, x+50, y+50, paint);
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);

                        break;
                }
                return true;
            }
        });
    }
}
