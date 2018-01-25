package com.cityu.huangzheng.androidlearning;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cityu.huangzheng.androidlearning.myclasses.ViewFlipperAnimationHelper;


public class ViewFlipperActivity extends Activity
{
    private TextView mTextView;
    private ViewFlipper mViewFlipper;
    private float mOldTouchValue;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        ((Gallery) findViewById(R.id.myGallery1)).setAdapter(new ImageAdapter(this));

        //set application fullscreen.
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_flipper);
        mViewFlipper = (ViewFlipper)findViewById(R.id.myViewFlipper);
    }
/**
    //Gallery adapter.
    public class ImageAdapter extends BaseAdapter
    {
        private Context myContext;
        private int[] myImageIds =
                {
                        android.R.drawable.btn_minus,
                        android.R.drawable.btn_radio,
                        android.R.drawable.ic_lock_idle_low_battery,
                        android.R.drawable.ic_menu_camera
                };


        public ImageAdapter(Context context)
        {
            this.myContext = context;
        }

        @Override
        public int getCount()
        {
            return this.myImageIds.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView i = new ImageView(this.myContext);
            i.setImageResource(this.myImageIds[position]);
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setLayoutParams(new Gallery.LayoutParams(120, 120));
            return i;
        }

        public float getScale(boolean focused, int offset)
        {
            return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }
    }
*/

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mOldTouchValue = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = event.getX();
                if(mOldTouchValue < currentX)
                {
                    mViewFlipper.setInAnimation(ViewFlipperAnimationHelper.inFromLeftAnimation());
                    mViewFlipper.setOutAnimation(ViewFlipperAnimationHelper.outToRightAnimation());
                    mViewFlipper.showNext();
                }
                if(mOldTouchValue > currentX)
                {
                    mViewFlipper.setInAnimation(ViewFlipperAnimationHelper.inFromRightAnimation());
                    mViewFlipper.setOutAnimation(ViewFlipperAnimationHelper.outToLeftAnimation());
                    mViewFlipper.showPrevious();
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}
