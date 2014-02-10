package com.sommerpanage.riemannsum.model;

/**
 * Created by spanage on 10/30/13.
 */
 public abstract class Function {

    public abstract double evaluate(double x);
    public abstract double evaluateIntegral(double x1, double x2);
    public abstract int getNameResourceId();
    public abstract int getPhoneticNameResourceId();

}
