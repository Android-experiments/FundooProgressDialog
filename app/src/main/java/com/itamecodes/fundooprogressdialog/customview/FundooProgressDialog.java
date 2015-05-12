package com.itamecodes.fundooprogressdialog.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.itamecodes.fundooprogressdialog.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek on 5/9/15.
 */
public class FundooProgressDialog extends View {

    private Paint mPaint, mArrowPaint,mElasticWavePaint,mCirclePlaceHolderPaint;
    private List<Point> mPathPoints = new ArrayList<Point>();
    private List<Point> mArrowPoints = new ArrayList<Point>();
    private Point mTargetPoint;
    private Point mTargetPointArrow = new Point(250, 300);
    private Point mTargetPointStartArrow;
    private Point mTargetPointEndArrow;
    private int mSweepAngle = 0;

    private Point mArrowPointStart;
    private Point mArrowPointEnd;
    private int mRectX;
    private int mRecty;
    private int mRecty1;
    private int mRectx1;
    private int mCounter=0;
    RectF mCricleRect;
    private Point mStartPointElasticBand;
    private Point mMiddlePointElasticBand;
    private Point mEndPointElasticBand;

    private int cPointx1=195;
    private int cPointy1=250;
    private int cPointx2=305;
    private int cPointy2=350;
    private boolean cPointx1Increment=true;
    private boolean cPointy1Increment=true;
    private boolean cPointx2Increment=true;
    private boolean cPointy2Increment=true;

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

    public FundooProgressDialog(Context context) {
        super(context);
        //init(context);
    }

    public FundooProgressDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }




    public void init(Context ctx,AttributeSet attrs) {
        TypedArray attributes=ctx.obtainStyledAttributes(attrs, R.styleable.FundooProgressDialog);
        int startX=attributes.getInteger(R.styleable.FundooProgressDialog_centerStartX,-1);
        int startY=attributes.getInteger(R.styleable.FundooProgressDialog_centerStartY,-1);
        int endX=attributes.getInteger(R.styleable.FundooProgressDialog_centerEndX,-1);
        int offsetX=attributes.getInteger(R.styleable.FundooProgressDialog_offSetX,-1);


        if(startX<0||startY<0||endX<0){
            throw new IllegalArgumentException("You have either not declared rectX or rectY or the values are negative");
        }
        int midPointX=((endX-startX)/2)+startX;
        mTargetPoint=new Point(midPointX,startY);
        mTargetPointStartArrow=new Point(midPointX,startY-10);
        mTargetPointEndArrow=new  Point(midPointX,(endX-startX)/2);
        mArrowPointStart=new Point(midPointX,(endX-startX)/2);
        mArrowPointEnd=new Point(midPointX,(startY+50));
        mStartPointElasticBand=new Point(startX+offsetX,startY);
        mEndPointElasticBand=new Point(endX-offsetX,startY);
        mMiddlePointElasticBand=new Point(midPointX,(startY+50));
        mRectX=startX;
        mRectx1=endX;
        mRecty=(endX-startX)/2;
        mRecty1=startY+(mRecty);
       mCricleRect = new RectF(mRectX, mRecty, mRectx1, mRecty1);
        mCirclePlaceHolderPaint = new Paint();
        mCirclePlaceHolderPaint.setStyle(Paint.Style.STROKE);
        mCirclePlaceHolderPaint.setColor(getResources().getColor(R.color.lightblue));
        mCirclePlaceHolderPaint.setStrokeWidth(20);
        mCirclePlaceHolderPaint.setAntiAlias(true);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);

        mElasticWavePaint = new Paint();
        mElasticWavePaint.setStyle(Paint.Style.STROKE);
        mElasticWavePaint.setColor(Color.WHITE);
        mElasticWavePaint.setStrokeWidth(10);
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
        canvas.drawArc(mCricleRect, 270, 360, false, mCirclePlaceHolderPaint);
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
        canvas.drawArc(mCricleRect, 270, 360, false, mCirclePlaceHolderPaint);
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
        canvas.drawArc(mCricleRect, 270, 360, false, mCirclePlaceHolderPaint);
        Path path = new Path();
        path.moveTo(mPathPoints.get(0).x, mPathPoints.get(0).y);

        for (int i = 1; i < mPathPoints.size(); i++) {
            path.lineTo(mPathPoints.get(i).x, mPathPoints.get(i).y);
        }
        canvas.drawPath(path, mPaint);
    }

    private void animateElasticBandToPosition(Canvas canvas) {
        canvas.drawArc(mCricleRect, 270, 360, false, mCirclePlaceHolderPaint);
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
        canvas.drawArc(mCricleRect, 270, 360, false, mCirclePlaceHolderPaint);
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
        canvas.drawArc(mCricleRect, 270, 360, false, mCirclePlaceHolderPaint);
        if (mSweepAngle != 360) {
            mSweepAngle = mSweepAngle + 6;

        } else {
            mCircleComplete = true;
        }
        canvas.drawArc(mCricleRect, 270, mSweepAngle, false, mPaint);

        drawElasticBandWave(canvas);
      //  if (!mCircleComplete) {
            postInvalidateDelayed(15);
      //  }

    }

    private void animateElasticwave(Canvas canvas) {

    }

    private void drawElasticBandWave(Canvas canvas) {
       // canvas.drawArc(mCricleRect, 270, 360, false, mCirclePlaceHolderPaint);
        List<Point> pathPoints = null;
      /*  if (mCounter % 2 == 0) {
            pathPoints = mIntermediatePointList1;
        } else {
            pathPoints = mIntermediatePointList2;
        }*/

        Path path = new Path();

         int startPoint=1;
        pathPoints=mPathPoints;
       /* switch(mCounter%3){
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
        }*/
        path.moveTo(pathPoints.get(0).x, pathPoints.get(0).y);
        Log.v("vivekanim", cPointx1 + "-" + cPointy1 + "-" + cPointx2 + "-" + cPointy2);
        path.cubicTo(cPointx1, cPointy1, cPointx2, cPointy2, 360,300);
        if(cPointx1Increment) {
            if (cPointx1 < 305) {
                cPointx1 = cPointx1 + 5;
            }else{
                cPointx1Increment=false;
            }
        }else{
            if(cPointx1>195){
                cPointx1 = cPointx1 - 5;
            }else{
                cPointx1Increment=true;
            }
        }
        if(cPointy1Increment) {
            if (cPointy1 < 350) {
                cPointy1 = cPointy1 + 5;
            }else{
                cPointy1Increment=false;
            }
        }else{
            if(cPointy1>250){
                cPointy1=cPointy1-5;
            }else{
                cPointy1Increment=true;
            }
        }
        if(cPointy2Increment) {
            if (cPointy2 > 250) {
                cPointy2 = cPointy2 - 5;
            }else{
                cPointy2Increment=false;
            }
        }else{
            if (cPointy2 < 350) {
                cPointy2 = cPointy2 +5;
            }else{
                cPointy2Increment=true;
            }
        }
        if(cPointx2Increment){
            if(cPointx2>195){
                cPointx2=cPointx2-5;
            }else{
                cPointx2Increment=false;
            }
        }else{
            if(cPointx2<305){
                cPointx2=cPointx2+5;
            }else{
                cPointx2Increment=true;
            }
        }


        /* for (int i = startPoint; i < pathPoints.size(); i++) {
            path.lineTo(pathPoints.get(i).x, pathPoints.get(i).y);
        }*/
        canvas.drawPath(path, mElasticWavePaint);
         mCounter++;
        if (mCircleComplete) {
            canvas.drawArc(mCricleRect, 270, mSweepAngle, false, mPaint);
           postInvalidateDelayed(15);
        }
    }
}
