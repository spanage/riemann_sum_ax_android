package com.sommerpanage.riemannsum.android;

import android.content.Context;
import android.graphics.Rect;

import com.sommerpanage.android.riemannsum.R;

/**
 * Created by spanage on 1/31/14.
 */
public class AxisVirtualView extends GraphVirtualView {

    public AxisVirtualView(boolean isY, int x, int y, int length,
                           float min, float max, int span, Context context) {

        final int offset = span / 2;

        mViewRect = (isY) ? new Rect(x - offset, y, x + offset, y + length) :
                            new Rect(x, y - offset, x + length, y + offset);

        final String format = (isY) ? context.getString(R.string.graph_y_axis_disc_format) :
                                context.getString(R.string.graph_x_axis_disc_format);

        mContentDescription = String.format(format, min, max);
    }
}
