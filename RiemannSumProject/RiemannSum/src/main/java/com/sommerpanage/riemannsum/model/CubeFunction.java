package com.sommerpanage.riemannsum.model;

import com.sommerpanage.android.riemannsum.R;

/**
 * Created by Sommer Panage on 10/30/13.
 */
public class CubeFunction extends Function{

    @Override
    public double evaluate(double x) {
        return Math.pow(x, 3);
    }

    @Override
    public double evaluateIntegral(double x1, double x2) {
        return (0.25) * (Math.pow(x2, 4) - Math.pow(x1, 4));
    }

    @Override
    public int getNameResourceId() {
        return R.string.function_x_cubed;
    }

    @Override
    public int getPhoneticNameResourceId() {
        return R.string.function_x_cubed_phonetic;
    }
}
