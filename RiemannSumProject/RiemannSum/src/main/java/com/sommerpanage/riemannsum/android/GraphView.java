package com.sommerpanage.riemannsum.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import com.sommerpanage.riemannsum.model.RiemannCoordinates;
import com.sommerpanage.riemannsum.model.RiemannModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spanage on 11/26/13.
 */
public class GraphView extends View{

    private static final float AXIS_STROKE_WIDTH = 2.0f;
    private static final float AXIS_LABEL_BUFFER = 16.0f;
    private static final float AXIS_LABEL_MARGIN = 20.0f;
    private static final float CURVE_STROKE_WIDTH = 2.0f;
    private static final float RECT_STROKE_WIDTH = 1.0f;
    private static final int VIRTUAL_AXIS_SPAN = 16;


    private RiemannModel mModel;
    private double mModelXMin, mModelXMax, mModelYMin, mModelYMax;
    private RiemannCoordinates mCurveCoordinates, mCurveScreenCoordinates;
    private Paint mAxisPaint, mCurvePaint, mRectPaint;
    private GraphViewTouchHelper mTouchHelper;
    private float mOriginX, mOriginY, mWidth, mHeight;
    private ArrayList<GraphVirtualView> mVirtualViews;

    public GraphView(Context context, RiemannModel model) {
        super(context);

        mModel = model;

        mAxisPaint = new Paint();
        mAxisPaint.setColor(Color.BLACK);
        mAxisPaint.setStrokeWidth(AXIS_STROKE_WIDTH);
        mAxisPaint.setTextSize(AXIS_LABEL_BUFFER);

        mCurvePaint = new Paint();
        mCurvePaint.setColor(Color.RED);
        mCurvePaint.setStrokeWidth(CURVE_STROKE_WIDTH);

        mRectPaint = new Paint();
        mRectPaint.setStrokeWidth(RECT_STROKE_WIDTH);

        mTouchHelper = new GraphViewTouchHelper(this);
        ViewCompat.setAccessibilityDelegate(this, mTouchHelper);

        mVirtualViews = new ArrayList<GraphVirtualView>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mVirtualViews.clear();

        if (mModel.isValid()) {
            setBackgroundColor(Color.WHITE);
            calculateCoordinates();
            drawRects(canvas);
            drawAxes(canvas);
            drawCurve(canvas);
        } else {
            setBackgroundColor(Color.RED);
        }
    }

    @Override
    public boolean dispatchHoverEvent(MotionEvent event) {
        if (mTouchHelper.dispatchHoverEvent(event)) {
            return true;
        }
        return super.dispatchHoverEvent(event);
    }

    private void drawAxes(Canvas canvas) {
        final float screenY_xAxis = (float) yFunctionToScreenCoordinate(yForXAxis());
        final float screenX_yAxis = (float) xFunctionToScreenCoordinate(xForYAxis());

        canvas.drawLine(mOriginX, screenY_xAxis,
                        mOriginX + mWidth, screenY_xAxis, mAxisPaint);
        canvas.drawLine(screenX_yAxis, mOriginY,
                        screenX_yAxis, mOriginY + mHeight, mAxisPaint);

        int axisStartIndex = 0;

        mVirtualViews.add(axisStartIndex, new AxisVirtualView(false, (int)mOriginX, (int)screenY_xAxis, (int)mWidth,
                (float)mModelXMin, (float)mModelXMax, VIRTUAL_AXIS_SPAN, getContext()));
        mVirtualViews.add(++axisStartIndex, new AxisVirtualView(true, (int)screenX_yAxis, (int)mOriginY, (int)mWidth,
                (float)mModelXMin, (float)mModelXMax, VIRTUAL_AXIS_SPAN, getContext()));

        final String xMinString = String.format("%.02f", mModelXMin);
        final String xMaxString = String.format("%.02f", mModelXMax);
        final String yMinString = String.format("%.02f", mModelYMin);
        final String yMaxString = String.format("%.02f",  mModelYMax);

        Rect stringBounds = new Rect();
        float x, y;

        // Draw yMin label
        mAxisPaint.getTextBounds(yMaxString, 0, yMaxString.length(), stringBounds);
        float rightBound = mOriginX + mWidth + AXIS_LABEL_BUFFER;
        x = screenX_yAxis - (stringBounds.width() / 2);
        if (x < (mOriginX - AXIS_LABEL_BUFFER)) {
            x = mOriginX;
        } else if (x + stringBounds.width() > rightBound) {
            x -= (x + stringBounds.width() - rightBound);
        }
        y = mOriginY - AXIS_LABEL_BUFFER + stringBounds.height();
        canvas.drawText(yMaxString, x, y, mAxisPaint);

        // Draw yMax label
        mAxisPaint.getTextBounds(yMinString, 0, yMinString.length(), stringBounds);
        x = screenX_yAxis - (stringBounds.width() / 2);
        if (x < (mOriginX - AXIS_LABEL_BUFFER)) {
            x = mOriginX;
        } else if (x + stringBounds.width() > rightBound) {
            x -= (x + stringBounds.width() - rightBound);
        }
        y = mOriginY + mHeight + AXIS_LABEL_BUFFER;
        canvas.drawText(yMinString, x, y, mAxisPaint);

        // Draw xMin label
        mAxisPaint.getTextBounds(xMinString, 0, xMinString.length(), stringBounds);
        float bottomBound = mOriginY + mHeight + AXIS_LABEL_BUFFER;
        y = screenY_xAxis + (stringBounds.width() / 2);
        if (y > bottomBound) {
            y = bottomBound;
        } else if (y + stringBounds.width() < mOriginY - AXIS_LABEL_BUFFER) {
            y += ((mOriginY - AXIS_LABEL_BUFFER) - (y + stringBounds.width()));
        }
        x = mOriginX - AXIS_LABEL_BUFFER + stringBounds.height();
        canvas.rotate(-90, x, y);
        canvas.drawText(xMinString, x, y, mAxisPaint);
        canvas.rotate(90, x, y);

        // Draw xMax label
        mAxisPaint.getTextBounds(xMaxString, 0, xMaxString.length(), stringBounds);
        x = mOriginX + mWidth + AXIS_LABEL_BUFFER - stringBounds.height();
        y = screenY_xAxis - (stringBounds.width() / 2);
        if (y + stringBounds.width() > bottomBound) {
            y -= (y + stringBounds.width() - bottomBound) ;
        } else if (y < mOriginY - AXIS_LABEL_BUFFER) {
            y = mOriginY - AXIS_LABEL_BUFFER;
        }
        canvas.rotate(90, x, y);
        canvas.drawText(xMaxString, x, y, mAxisPaint);
        canvas.rotate(-90, x, y);
    }

    private void drawCurve(Canvas canvas) {
        for (int i = 0; i < mCurveScreenCoordinates.getCoordinateCount() - 1; i++) {
            final float x1 = (float) mCurveScreenCoordinates.xCoordinateAtIndex(i);
            final float y1 = (float) mCurveScreenCoordinates.yCoordinateAtIndex(i);
            final float x2 = (float) mCurveScreenCoordinates.xCoordinateAtIndex(i + 1);
            final float y2 = (float) mCurveScreenCoordinates.yCoordinateAtIndex(i + 1);

            canvas.drawLine(x1, y1, x2, y2, mCurvePaint);
        }
    }

    private void drawRects(Canvas canvas) {

        final float screenY_xAxis = (float)yFunctionToScreenCoordinate(yForXAxis());
        final float screen_dx = (float) (mModel.getDX() * mWidth / (mModelXMax - mModelXMin));
        final RiemannCoordinates riemannCoordinates = mModel.getCoordinates();

        for (int i = 0; i < riemannCoordinates.getCoordinateCount(); i++) {
            mRectPaint.setColor(Color.LTGRAY);
            final double modelX = riemannCoordinates.xCoordinateAtIndex(i);
            final double modelY = riemannCoordinates.yCoordinateAtIndex(i);
            final double modelDX = mModel.getDX();
            final float x = (float)xFunctionToScreenCoordinate(modelX);
            final float y = (float)yFunctionToScreenCoordinate(modelY);
            if (y > screenY_xAxis) {
                canvas.drawRect(x, screenY_xAxis, x + screen_dx, y, mRectPaint);
                mVirtualViews.add(new RectVirtualView(i, (int)x, (int)screenY_xAxis, (int)(x + screen_dx), (int)y,
                        (float)modelX, (float)(modelX + modelDX), (float)(modelY * modelDX), getContext()));
            } else {
                canvas.drawRect(x, y, x + screen_dx, screenY_xAxis, mRectPaint);
                mVirtualViews.add(new RectVirtualView(i, (int)x, (int)y, (int)(x + screen_dx), (int)screenY_xAxis,
                        (float)modelX, (float)(modelX + modelDX), (float)(modelY * modelDX), getContext()));
            }
            float[] border = {x, y, x + screen_dx, y,
                    x + screen_dx, y, x + screen_dx, screenY_xAxis,
                    x, y, x, screenY_xAxis};
            mRectPaint.setColor(Color.BLACK);
            canvas.drawLines(border, mRectPaint);
        }
    }

    private double yForXAxis() {
        return (mModelYMin > 0.0f) ? mModelYMin : Math.min(mModelYMax, 0.0f);
    }

    private double xForYAxis() {
        return (mModelXMin > 0.0f) ? mModelXMin : Math.min(mModelXMax, 0.0f);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mOriginX = getPaddingLeft() + AXIS_LABEL_MARGIN;
        mOriginY = getPaddingTop() + AXIS_LABEL_MARGIN;
        mWidth = w - (getPaddingLeft() + getPaddingRight()) - 2 * AXIS_LABEL_MARGIN;
        mHeight = h - (getPaddingTop() + getPaddingBottom()) - 2 * AXIS_LABEL_MARGIN;
    }

    private void calculateCoordinates() {

        final double dx = ((double)(mModel.getXMax() - mModel.getXMin())) / (double)mWidth;
        final double screen_dx = 1.0;

        mCurveCoordinates = new RiemannCoordinates(mModel.getXMin(), dx);
        mModelXMin = mModel.getXMin();
        mModelXMax = mModel.getXMax();
        mModelYMin = Double.MAX_VALUE;
        mModelYMax = Double.MIN_VALUE;

        double x = mModelXMin;
        for (int i = 0; i <= mWidth; ++i) {
            final double y = mModel.getFunction().evaluate(x);
            mCurveCoordinates.addNextYValue(y);
            if (y < mModelYMin) {
                mModelYMin = y;
            }
            if (y > mModelYMax) {
                mModelYMax = y;
            }
            x += dx;
        }

        mCurveScreenCoordinates = new RiemannCoordinates((double)mOriginX, screen_dx);
        for (int i = 0; i <= mWidth; ++i) {
            final double y = mCurveCoordinates.yCoordinateAtIndex(i);
            mCurveScreenCoordinates.addNextYValue(yFunctionToScreenCoordinate(y));
        }
    }

    private double xFunctionToScreenCoordinate(double x) {
        final double m = ((double )mWidth) / (double) (mModelXMax - mModelXMin);
        return (x - mModel.getXMin()) * m + mOriginX;
    }

    private double yFunctionToScreenCoordinate(double y) {
        final double m = ((double )mHeight) / (mModelYMax - mModelYMin);
        return (mOriginY + mHeight) - (y - mModelYMin) * m;
    }

    private static class GraphViewTouchHelper extends ExploreByTouchHelper {

        private GraphView mGraphView;

        public GraphViewTouchHelper(GraphView forView) {
            super(forView);
            mGraphView = forView;
        }

        @Override
        protected int getVirtualViewAt(float v, float v2) {
            int i = 0;
            for (GraphVirtualView vv : mGraphView.mVirtualViews) {
                final Rect rect = vv.getViewRect();
                if (rect.contains((int)v, (int)v2)) {
                    return i;
                }
                ++i;
            }
            return ExploreByTouchHelper.INVALID_ID;
        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> integers) {
            Integer i = 0;
            for (GraphVirtualView vv : mGraphView.mVirtualViews) {
                integers.add(i++);
            }
        }

        @Override
        protected void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent) {
            if (i < mGraphView.mVirtualViews.size()) {
                GraphVirtualView v = mGraphView.mVirtualViews.get(i);
                accessibilityEvent.setContentDescription(v.getContentDescription());
            }
        }

        @Override
        protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (i < mGraphView.mVirtualViews.size()) {
                GraphVirtualView v = mGraphView.mVirtualViews.get(i);
                accessibilityNodeInfoCompat.setContentDescription(v.getContentDescription());
                // Reported bounds should be consistent with onDraw().
                Rect rect = v.getViewRect();
                // Rect cannot be visually empty.
                if (rect.height() == 0) {
                    rect.set(rect.left, rect.top - 1, rect.right, rect.bottom);
                }
                accessibilityNodeInfoCompat.setBoundsInParent(rect);
            }
        }

        @Override
        protected boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            return false;
        }
    }
}
