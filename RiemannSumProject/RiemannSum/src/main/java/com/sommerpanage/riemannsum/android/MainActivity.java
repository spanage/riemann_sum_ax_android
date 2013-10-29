package com.sommerpanage.riemannsum.android;

import android.app.Activity;
import android.os.Bundle;

import com.sommerpanage.android.riemannsum.R;
import com.sommerpanage.riemannsum.model.CubeFunction;
import com.sommerpanage.riemannsum.model.Function;
import com.sommerpanage.riemannsum.model.RiemannModel;
import com.sommerpanage.riemannsum.model.SineFunction;
import com.sommerpanage.riemannsum.model.SquareFunction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ControlsFragment.OnControlsUpdatedListener{

    private static List<Function> sFunctions;
    private RiemannModel mModel;

    private GraphFragment mGraphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mModel = new RiemannModel();

            sFunctions = new ArrayList<Function>();
            sFunctions.add(new SquareFunction());
            sFunctions.add(new CubeFunction());
            sFunctions.add(new SineFunction());

            ControlsFragment controlsFragment = new ControlsFragment(sFunctions);
            controlsFragment.setOnControlsUpdatedListener(this);

            mGraphFragment = new GraphFragment(mModel);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, controlsFragment)
                    .add(R.id.container, mGraphFragment)
                    .commit();
        }
    }

    @Override
    public void onControlsUpdate(Function newFunction, double newMin, double newMax, int newCount) {
        mModel.setFunction(newFunction);
        mModel.setXMin(newMin);
        mModel.setXMax(newMax);
        mModel.setIntervalCount(newCount);
        mGraphFragment.update();
    }
}
