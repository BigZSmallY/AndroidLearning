package com.cityu.huangzheng.androidlearning.animator;

/**
 * Created by xiaoyan on 2018/1/26.
 */

public enum AnimateCatalogue {
    DroupOut(DropOutAnimator.class);

    private Class mClz;
    private AnimateCatalogue(Class clz){
        mClz = clz;
    }

    public BaseViewAnimator getAnimator(){
        try {
            return (BaseViewAnimator) mClz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
