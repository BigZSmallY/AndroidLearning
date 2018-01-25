package com.cityu.huangzheng.androidlearning.myclasses;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by huangzheng on 2/4/15.
 */
public class ViewFlipperAnimationHelper
{
    //slip from right to left set in event.
    public static Animation inFromRightAnimation()
    {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                +1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f
        );
        inFromRight.setDuration(350);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    //slip from right to left set out event.
    public static Animation outToLeftAnimation()
    {
        Animation outToLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                -1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f
                );
        outToLeft.setDuration(350);
        outToLeft.setInterpolator(new AccelerateInterpolator());
        return outToLeft;
    }

    //slip from left to right set in event.
    public static Animation inFromLeftAnimation()
    {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                +1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f
        );
        inFromLeft.setDuration(350);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    //slip from left to right set out event.
    public static Animation outToRightAnimation()
    {
        Animation outToRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                +1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f
        );
        outToRight.setDuration(350);
        outToRight.setInterpolator(new AccelerateInterpolator());
        return outToRight;
    }
}
