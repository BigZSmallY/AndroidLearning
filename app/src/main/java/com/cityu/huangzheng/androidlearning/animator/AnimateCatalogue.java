package com.cityu.huangzheng.androidlearning.animator;

import com.cityu.huangzheng.androidlearning.AnimatorActivity;

/**
 * Created by xiaoyan on 2018/1/26.
 */

public enum AnimateCatalogue {
    DroupOut(DroupOutAnimator.class);

    private Class mClz;
    private AnimateCatalogue(Class clz){
        mClz = clz;
    }
}
