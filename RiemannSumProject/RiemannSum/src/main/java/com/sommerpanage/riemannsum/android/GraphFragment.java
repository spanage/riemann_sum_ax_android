package com.sommerpanage.riemannsum.android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sommerpanage.android.riemannsum.R;
import com.sommerpanage.riemannsum.model.RiemannModel;

/**
 * Created by spanage on 11/25/13.
 */
public class GraphFragment extends Fragment {

    private RiemannModel mModel;
    private static final int PADDING = 20;
    private GraphView mGraphView;
    private TextView mActualResultTextView, mRiemannResultTextView;

    public GraphFragment(RiemannModel model) {
        this.mModel = model;
    }

    public void update() {
        mGraphView.invalidate();
        if (mModel.isValid()) {
            final double integral = mModel.getFunction().evaluateIntegral(mModel.getXMin(), mModel.getXMax());
            mActualResultTextView.setText("Integral = " + String.format("%.02f", integral));
            mRiemannResultTextView.setText("Riemann Sum = " + String.format("%.02f", mModel.getSum()));
        } else {
            clearText();
        }
    }

    private void clearText() {
        mActualResultTextView.setText("");
        mRiemannResultTextView.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout graphContainer = (LinearLayout) inflater.inflate(R.layout.fragment_graph, container, false);
        mActualResultTextView = (TextView) graphContainer.findViewById(R.id.actualResultText);
        mRiemannResultTextView = (TextView) graphContainer.findViewById(R.id.riemannResultText);

        mGraphView = new GraphView(getActivity(), mModel);
        mGraphView.setPadding(PADDING, PADDING, PADDING, PADDING);
        graphContainer.addView(mGraphView);

        return graphContainer;
    }
}