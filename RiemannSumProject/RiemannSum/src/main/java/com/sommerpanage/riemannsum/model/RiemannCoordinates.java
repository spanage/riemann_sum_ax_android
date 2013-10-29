package com.sommerpanage.riemannsum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spanage on 10/31/13.
 */
public class RiemannCoordinates {

    private static class Pair {
        private double x;
        private double y;

        private Pair(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private List<Pair> mCoordinateList;
    private double mDx;
    private double mX0;

    public RiemannCoordinates(double x0, double dx) {
        mCoordinateList = new ArrayList<Pair>();
        this.mDx = dx;
        this.mX0 = x0;
    }

    public double xCoordinateAtIndex(int index) {
        return mCoordinateList.get(index).x;
    }

    public double yCoordinateAtIndex(int index) {
        return mCoordinateList.get(index).y;
    }

    public int getCoordinateCount() {
        return mCoordinateList.size();
    }

    public void addNextYValue(double y) {
        final double x;
        if (mCoordinateList.size() == 0) {
            x = mX0;
        } else {
            x = mCoordinateList.get(mCoordinateList.size() - 1).x + mDx;
        }
        mCoordinateList.add(new Pair(x, y));
    }
}
