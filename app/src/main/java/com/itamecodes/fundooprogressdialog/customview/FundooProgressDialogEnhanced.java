package com.itamecodes.fundooprogressdialog.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek on 5/9/15.
 */
public class FundooProgressDialogEnhanced extends View {

    private Paint mPaint, mArrowPaint,mElasticWavePaint;
    private List<Point> mPathPoints = new ArrayList<Point>();
    private List<Point> mArrowPoints = new ArrayList<Point>();
    private Point mTargetPoint = new Point(250, 300);
    private Point mTargetPointArrow = new Point(250, 300);
    private Point mTargetPointStartArrow = new Point(250, 290);
    private Point mTargetPointEndArrow = new Point(250, 150);
    private int mSweepAngle = 0;

    private Point mArrowPointStart = new Point(250, 150);
    private Point mArrowPointEnd = new Point(250, 350);
    private int mRectX = 100;
    private int mRecty = 150;
    private int mRecty1 = 450;
    private int mRectx1 = 400;
    private int mCounter=0;
    RectF mCricleRect = new RectF(mRectX, mRecty, mRectx1, mRecty1);
    private Point mStartPointElasticBand = new Point(140, 300);
    private Point mMiddlePointElasticBand = new Point(250, 350);
    private Point mEndPointElasticBand = new Point(360, 300);

    private ArrayList<Point> mIntermediatePointList1 = new ArrayList<Point>();
    private ArrayList<Point> mIntermediatePointList2 = new ArrayList<Point>();
    private ArrayList<Point> mIntermediatePointList3 = new ArrayList<Point>();


    private boolean mArrowPointStartReachedTarget = false;
    private boolean mArrowPointEndReachedTarget = false;
    private boolean mStopAnimationArrow = false;
    private boolean mFirstFrameDrawn = false;
    private boolean mElasticBandInPosition = false;
    private boolean mPointBounceReachedTarget = false;
    private boolean mCircleComplete = false;

    public FundooProgressDialogEnhanced(Context context) {
        super(context);
        init();
    }

    public FundooProgressDialogEnhanced(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);

        mElasticWavePaint = new Paint();
        mElasticWavePaint.setStyle(Paint.Style.STROKE);
        mElasticWavePaint.setColor(Color.RED);
        mElasticWavePaint.setStrokeWidth(4);
        mElasticWavePaint.setDither(true);
        mElasticWavePaint.setPathEffect(new CornerPathEffect(10));
        mElasticWavePaint.setAntiAlias(true);


        mPathPoints.add(mStartPointElasticBand);
        mPathPoints.add(mMiddlePointElasticBand);
        mPathPoints.add(mEndPointElasticBand);

        int counter = 0;
        for (int i = 0; mStartPointElasticBand.x + i <= mEndPointElasticBand.x; i = i + 25) {
            int x = mStartPointElasticBand.x + i;
            int y = 0;
            if (counter % 2 == 0) {
                y = mStartPointElasticBand.y + 5;
            } else {
                y = mStartPointElasticBand.y - 5;
            }

            Point p = new Point(x, y);
            mIntermediatePointList1.add(p);
            counter++;
        }
        counter = 0;
        for (int i = 0; mStartPointElasticBand.x + i <= mEndPointElasticBand.x; i = i + 30) {
            int x = mStartPointElasticBand.x + i;
            int y = 0;
            if (counter % 2 == 0) {
                y = mStartPointElasticBand.y + 5;
            } else {
                y = mStartPointElasticBand.y - 5;
            }

            Point p = new Point(x, y);
            mIntermediatePointList2.add(p);
            counter++;
        }
        counter = 0;
        for (int i = 0; mStartPointElasticBand.x + i <= mEndPointElasticBand.x; i = i + 40) {
            int x = mStartPointElasticBand.x + i;
            int y = 0;
            if (counter % 2 == 0) {
                y = mStartPointElasticBand.y + 5;
            } else {
                y = mStartPointElasticBand.y - 5;
            }

            Point p = new Point(x, y);
            mIntermediatePointList3.add(p);
            counter++;
        }


        mArrowPoints.add(mArrowPointStart);
        mArrowPoints.add(mArrowPointEnd);

    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (!mFirstFrameDrawn) {
            initFirstFrameEver(canvas);
        } else if (!mStopAnimationArrow) {
            reduceArrowFrame(canvas);
        } else if (!mElasticBandInPosition) {
            animateElasticBandToPosition(canvas);
        } else if (!mPointBounceReachedTarget) {
            bouncePointArrow(canvas);
        } else if(!mCircleComplete){
            drawCircle(canvas);
        }else{
             drawElasticBandWave(canvas);
        }


    }

    private void initFirstFrameEver(Canvas canvas) {
        Path pathArrow = new Path();
        pathArrow.moveTo(mArrowPoints.get(0).x, mArrowPoints.get(0).y);
        pathArrow.lineTo(mArrowPoints.get(1).x, mArrowPoints.get(1).y);

        drawElasticBand(canvas);
        canvas.drawPath(pathArrow, mPaint);
        mFirstFrameDrawn = true;
        Log.v("vivekdraw", "drawn first frame");

        postInvalidateDelayed(15);
    }

    private void reduceArrowFrame(Canvas canvas) {
        mArrowPoints.clear();
        if (mArrowPointStartReachedTarget && mArrowPointEndReachedTarget) {
            mStopAnimationArrow = true;
        }
        if (!(mArrowPointStart.equals(mTargetPoint.x, mTargetPoint.y))) {
            mArrowPointStart.offset(0, 1);
        } else {
            mArrowPointStartReachedTarget = true;
        }
        if ((!mArrowPointEnd.equals(mTargetPoint.x, mTargetPoint.y))) {
            mArrowPointEnd.offset(0, -1);
        } else {
            mArrowPointEndReachedTarget = true;
        }
        mArrowPoints.add(mArrowPointStart);
        mArrowPoints.add(mArrowPointEnd);
        Path pathArrow = new Path();
        pathArrow.moveTo(mArrowPoints.get(0).x, mArrowPoints.get(0).y);
        pathArrow.lineTo(mArrowPoints.get(1).x, mArrowPoints.get(1).y);

        drawElasticBand(canvas);
        Log.v("vivekdraw", "drawn animate frame");
        if (!mStopAnimationArrow) {
            canvas.drawPath(pathArrow, mPaint);

        } else {
            canvas.drawPoint(mTargetPoint.x, mTargetPoint.y, mPaint);
        }
        postInvalidateDelayed(15);
    }

    private void drawElasticBand(Canvas canvas) {
        Path path = new Path();
        path.moveTo(mPathPoints.get(0).x, mPathPoints.get(0).y);

        for (int i = 1; i < mPathPoints.size(); i++) {
            path.lineTo(mPathPoints.get(i).x, mPathPoints.get(i).y);
        }
        canvas.drawPath(path, mPaint);
    }

    private void animateElasticBandToPosition(Canvas canvas) {

        if (!mMiddlePointElasticBand.equals(mTargetPoint.x, mTargetPoint.y)) {
            mMiddlePointElasticBand.offset(0, -5);
        } else {
            mElasticBandInPosition = true;
        }
        Log.v("vivekdraw", "elastic band animated" + mElasticBandInPosition);
        drawElasticBand(canvas);
        canvas.drawPoint(mTargetPoint.x, mTargetPoint.y, mPaint);
        // if (!mElasticBandInPosition) {
        postInvalidateDelayed(5);
        //  }

    }

    private void bouncePointArrow(Canvas canvas) {
        if (!mTargetPointStartArrow.equals(mTargetPointEndArrow.x, mTargetPointEndArrow.y)) {
            mTargetPointStartArrow.offset(0, -5);
        } else {
            mPointBounceReachedTarget = true;
        }
        canvas.drawPoint(mTargetPointStartArrow.x, mTargetPointStartArrow.y, mPaint);
        drawElasticBand(canvas);
        //if (!mPointBounceReachedTarget) {
        postInvalidateDelayed(15);
        //}
    }

    private void drawCircle(Canvas canvas) {
        if (mSweepAngle != 360) {
            mSweepAngle = mSweepAngle + 6;

        } else {
            mCircleComplete = true;
        }
        canvas.drawArc(mCricleRect, 270, mSweepAngle, false, mPaint);

        drawElasticBandWave(canvas);
      //  if (!mCircleComplete) {
            postInvalidateDelayed(50);
      //  }

    }

    private void animateElasticwave(Canvas canvas) {

    }

    private void drawElasticBandWave(Canvas canvas) {
        ArrayList<Point> pathPoints = null;
      /*  if (mCounter % 2 == 0) {
            pathPoints = mIntermediatePointList1;
        } else {
            pathPoints = mIntermediatePointList2;
        }*/

        Path path = new Path();

         int startPoint=1;

        switch(mCounter%3){
            case 0:{
                  pathPoints = mIntermediatePointList1;
                break;
            }
            case 1:{
                 pathPoints = mIntermediatePointList2;
                break;
            }
            case 2:{
                 pathPoints = mIntermediatePointList3;
                break;
            }
        }
        path.moveTo(pathPoints.get(0).x, pathPoints.get(0).y);
         for (int i = startPoint; i < pathPoints.size(); i++) {
            path.lineTo(pathPoints.get(i).x, pathPoints.get(i).y);
        }

        canvas.drawPath(path, mElasticWavePaint);
         mCounter++;
        if(mCircleComplete){
            canvas.drawArc(mCricleRect, 270, mSweepAngle, false, mPaint);
           postInvalidateDelayed(50);
        }
    }
}
