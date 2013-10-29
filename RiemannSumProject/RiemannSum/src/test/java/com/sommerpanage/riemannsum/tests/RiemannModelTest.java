package com.sommerpanage.riemannsum.tests;

import com.sommerpanage.riemannsum.model.CubeFunction;
import com.sommerpanage.riemannsum.model.Function;
import com.sommerpanage.riemannsum.model.RiemannCoordinates;
import com.sommerpanage.riemannsum.model.RiemannModel;
import com.sommerpanage.riemannsum.model.SineFunction;
import com.sommerpanage.riemannsum.model.SquareFunction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Sommer Panage on 10/29/13.
 */
public class RiemannModelTest{

    private static final double errorDelta = 0.001;
    private RiemannModel model;

    @Before
    public void setup() {
        this.model = new RiemannModel();
    }

    @Test
    public void newModel() throws Exception {
        runDummyModelTest(0, 0.0, 0.0, false, null, null);
    }

    @Test
    public void validModelPositiveXRange() throws Exception {
        runDummyModelTest(1, 1.0, 1945.34, true, new double[]{1.0}, new double[]{0.0});
    }

    @Test
    public void validModelNegativeXRange() throws Exception {
        runDummyModelTest(1, -100.9, -5.23, true, new double[]{-100.9}, new double[]{0.0});
    }

    @Test
    public void validModelMixed() throws Exception {
        runDummyModelTest(3, -100.0, 40001.0, true,
                new double[]{-100.0, 13267.0, 26634.0}, new double[]{0.0, 0.0, 0.0});
    }

    @Test
    public void invalidModelPositiveXRange() throws Exception {
        runDummyModelTest(1, 100000.0, 1945.34, false, null, null);
    }

    @Test
    public void invalidModelNegativeXRange() throws Exception {
        runDummyModelTest(1, -1.3, -15.23, false, null, null);
    }

    @Test
    public void invalidModelMixedA() throws Exception {
        runDummyModelTest(-4, -44.899, 39393.3, false, null, null);
    }

    @Test
    public void invalidModelMixedB() throws Exception {
        runDummyModelTest(-4, 3, -4, false, null, null);
    }

    @Test
    public void testSquareModelSimple() throws Exception {
        runTest(1, 0, 1, true, new SquareFunction(),new double[] {0.0}, new double[] {0.0});
    }

    @Test
    public void testSquareModelNegative() throws Exception {
        runTest(3, -4, -1, true, new SquareFunction(),
                new double[] {-4.0, -3.0, -2.0}, new double[] {16.0, 9.0, 4.0});
    }

    @Test
    public void testSquareModelPositive() throws Exception {
        runTest(3, 1, 4, true, new SquareFunction(),
                new double[] {1.0, 2.0, 3.0}, new double[] {1.0, 4.0, 9.0});
    }

    @Test
    public void testSquareModelMixed() throws Exception {
        runTest(6, -3, 3, true, new SquareFunction(),
                new double[] {-3.0, -2.0, -1.0, 0.0, 1.0, 2.0},
                new double[] {9.0, 4.0, 1.0, 0.0, 1.0, 4.0});
    }

    @Test
    public void testCubeModelSimple() throws Exception {
        runTest(1, 0, 1, true, new CubeFunction(),new double[] {0.0}, new double[] {0.0});
    }

    @Test
    public void testCubeModelNegative() throws Exception {
        runTest(3, -4, -1, true, new CubeFunction(),
                new double[] {-4.0, -3.0, -2.0}, new double[] {-64.0, -27.0, -8.0});
    }

    @Test
    public void testCubeModelPositive() throws Exception {
        runTest(3, 1, 4, true, new CubeFunction(),
                new double[] {1.0, 2.0, 3.0}, new double[] {1.0, 8.0, 27.0});
    }

    @Test
    public void testCubeModelMixed() throws Exception {
        runTest(6, -3, 3, true, new CubeFunction(),
                new double[] {-3.0, -2.0, -1.0, 0.0, 1.0, 2.0},
                new double[] {-27.0, -8.0, -1.0, 0.0, 1.0, 8.0});
    }

    @Test
    public void testSineModelSimple() throws Exception {
        runTest(1, 0, 1, true, new SineFunction(),new double[] {0.0}, new double[] {0.0});
    }

    @Test
    public void testSineModelNegative() throws Exception {
        runTest(3, -4, -1, true, new SineFunction(),
                new double[] {-4.0, -3.0, -2.0}, new double[] {0.757, -0.141, -0.909});
    }

    @Test
    public void testSineModelPositive() throws Exception {
        runTest(3, 1, 4, true, new SineFunction(),
                new double[] {1.0, 2.0, 3.0}, new double[] {0.841, 0.909, 0.141});
    }

    @Test
    public void testSineModelMixed() throws Exception {
        runTest(6, -3, 3, true, new SineFunction(),
                new double[] {-3.0, -2.0, -1.0, 0.0, 1.0, 2.0},
                new double[] {-0.141, -0.909, -0.841, 0.0, 0.841, 0.909});
    }

    private void runDummyModelTest(int intervalCount, double xMin, double xMax, boolean valid,
            double[] expectedXValues, double[] expectedYValues) {
        class ZeroFunction extends Function {

            @Override
            public double evaluate(double x) {
                return 0;
            }

            @Override
            public double evaluateIntegral(double x1, double x2) {
                return 0;
            }

            @Override
            public int getNameResourceId() {
                return 0;
            }
        }
        runTest(intervalCount, xMin, xMax, valid, new ZeroFunction(), expectedXValues, expectedYValues);
    }

    private void runTest(int intervalCount, double xMin, double xMax, boolean valid, Function function,
            double[] expectedXValues, double[] expectedYValues) {
        setupModel(intervalCount, xMin, xMax, function);
        verifyModel(intervalCount, xMin, xMax, valid, expectedXValues, expectedYValues);
    }

    private void setupModel(int intervalCount, double xMin, double xMax, Function function) {
        model.setXMin(xMin);
        model.setXMax(xMax);
        model.setIntervalCount(intervalCount);
        model.setFunction(function);
    }

    private void verifyModel(int intervalCount, double xMin, double xMax, boolean valid,
            double[] expectedXValues, double[] expectedYValues) {
        assertEquals(model.getIntervalCount(), intervalCount);
        assertEquals(model.getXMin(), xMin, errorDelta);
        assertEquals(model.getXMax(), xMax, errorDelta);
        assertEquals(model.isValid(), valid);
        RiemannCoordinates coordinates = model.getCoordinates();
        if (!valid) {
            assertNull(coordinates);
        } else for (int i = 0; i < intervalCount; i++) {
            assertEquals(coordinates.xCoordinateAtIndex(i), expectedXValues[i], errorDelta);
            assertEquals(coordinates.yCoordinateAtIndex(i), expectedYValues[i], errorDelta);
        }
    }
}
