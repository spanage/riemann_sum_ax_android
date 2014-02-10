package com.sommerpanage.riemannsum.model;

import com.sommerpanage.android.riemannsum.R;

/**
 * Created by spanage on 10/30/13.
 */
public class SquareFunction extends Function {

    @Override
    public double evaluate(double x) {
        return Math.pow(x, 2);
    }

    @Override
    public double evaluateIntegral(double x1, double x2) {
        return (1.0/3.0) * (Math.pow(x2, 3) - Math.pow(x1,3));
    }

    @Override
    public int getNameResourceId() {
        return R.string.function_x_squared;
    }

    @Override
    public int getPhoneticNameResourceId() {
        return R.string.function_x_squared_phonetic;
    }

}
