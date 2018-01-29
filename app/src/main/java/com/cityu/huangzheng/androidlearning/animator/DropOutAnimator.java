package com.cityu.huangzheng.androidlearning.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by xiaoyan on 2018/1/26.
 */

public class DropOutAnimator extends BaseViewAnimator{
    @Override
    public void startAnimate(final View target) {
        float top = target.getTop() + target.getHeight();
        AnimatorSet set = new AnimatorSet();
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                target.setAlpha((float)animation.getAnimatedValue());
//            }
//        });
//        valueAnimator.start();

        set.play(ObjectAnimator.ofFloat(target, "alpha", 0, 1).setDuration(3000)).with(
                ObjectAnimator.ofFloat(target, "translationY", -top, 0).setDuration(1000)
        );
        set.start();
    }
}
