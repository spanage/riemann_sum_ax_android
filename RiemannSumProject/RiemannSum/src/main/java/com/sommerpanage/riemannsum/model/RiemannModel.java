package com.sommerpanage.riemannsum.model;

import android.util.Log;

/**
 * Created by spanage on 10/29/13.
 */
public class RiemannModel {

    private static final String TAG = RiemannModel.class.getCanonicalName();

    private double mXMin;
    private double mXMax;
    private int mIntervalCount;
    private RiemannCoordinates mCoordinates;

    private Function function;

    public boolean isValid() {
        return mXMax > mXMin && mIntervalCount >= 0 && function != null;
    }

    public double getXMax() {
        return mXMax;
    }

    public void setXMax(double xMax) {
        this.mXMax = xMax;
        mCoordinates = null;
        Log.d(TAG, "X Max updated: " + this.mXMax);
    }

    public int getIntervalCount() {
        return mIntervalCount;
    }

    public void setIntervalCount(int intervalCount) {
        this.mIntervalCount = intervalCount;
        mCoordinates = null;
        Log.d(TAG, "Interval count updated: " + this.mIntervalCount);

    }

    public void setXMin(double xMin) {
        this.mXMin = xMin;
        mCoordinates = null;
        Log.d(TAG, "X Min updated: " + this.mXMin);

    }

    public double getXMin() {
        return mXMin;
    }

    public void setFunction(Function function) {
        this.function = function;
        mCoordinates = null;
        Log.d(TAG, "Function updated: " + this.function);

    }

    public Function getFunction() {
        return function;
    }

    public double getDX() {
        return (mXMax - mXMin) / mIntervalCount;
    }

    public RiemannCoordinates getCoordinates() {
        if (mCoordinates == null) {
            calculateCoordinates();
        }
        return mCoordinates;
    }

    public double getSum() {
        RiemannCoordinates coords = getCoordinates();
        double sum = 0;
        final double dx = getDX();
        for (int i = 0; i < coords.getCoordinateCount(); i++) {
            sum += coords.yCoordinateAtIndex(i) * dx;
        }
        return sum;
    }

    private void calculateCoordinates() {
        if (isValid()) {
            final double dx = getDX();
            mCoordinates = new RiemannCoordinates(mXMin, dx);
            final double x0 = mXMin;
            for (int i = 0; i < mIntervalCount; i++) {
                mCoordinates.addNextYValue(function.evaluate(x0 + ((double) i * dx)));
            }
        }
    }

}
