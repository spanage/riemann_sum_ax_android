package com.sommerpanage.riemannsum.model;

import com.sommerpanage.android.riemannsum.R;

/**
 * Created by spanage on 10/30/13.
 */
public class SineFunction extends Function {

    @Override
    public double evaluate(double x) {
        return Math.sin(x);
    }

    @Override
    public double evaluateIntegral(double x1, double x2) {
        return (-Math.cos(x2)) - (-Math.cos(x1));
    }

    @Override
    public int getNameResourceId() {
        return R.string.function_sine_x;
    }
}
