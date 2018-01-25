package com.cityu.huangzheng.androidlearning;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by huangzheng on 2/6/15.
 */
public class GalleryActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);


        Gallery gallery = (Gallery) findViewById(R.id.mGallery);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(GalleryActivity.this, "" + (position+1), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class ImageAdapter extends BaseAdapter
    {
        private Context context;
        int mGalleryItemBackground;

        private Integer[] myImageIds =
                {R.drawable.photo1,
                        R.drawable.photo2,
                        R.drawable.photo3,
                        R.drawable.photo4,
                        R.drawable.photo5,
                        R.drawable.photo6};


        public ImageAdapter(Context context)
        {
            this.context = context;

            TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
            mGalleryItemBackground = typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
            typedArray.recycle();
        }


        @Override
        public int getCount()
        {
            return myImageIds.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(myImageIds[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(400,500));
            imageView.setBackgroundResource(mGalleryItemBackground);
            return imageView;
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
}
