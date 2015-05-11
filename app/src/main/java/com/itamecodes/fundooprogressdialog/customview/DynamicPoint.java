package com.itamecodes.fundooprogressdialog.customview;

import android.graphics.Point;

/**
 * Created by vivek on 5/9/15.
 */
public class DynamicPoint extends Point {
    private long mLastTime=0;
    private float mVelocity=0;
    private float targetY,presentY,springiness,damping;

    public void update(long now){
        long dt=Math.min(now-mLastTime,50);
        mVelocity=(targetY-presentY)*springiness;
        mVelocity*=(1-damping);
        presentY=mVelocity*dt/1000;
        mLastTime=now;
    }

}
