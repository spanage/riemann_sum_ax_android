package com.sommerpanage.riemannsum.android;

import android.content.Context;
import android.graphics.Rect;

import com.sommerpanage.android.riemannsum.R;

/**
 * Created by spanage on 1/31/14.
 */
public class RectVirtualView extends GraphVirtualView{
    public RectVirtualView(int index, int left, int top, int right, int bottom,
                           float minX, float maxX, float area, Context context) {

        mViewRect = new Rect(left, top, right, bottom);

        final String format = context.getString(R.string.graph_rect_disc_format);
        mContentDescription = String.format(format, index, minX, maxX, area);
    }
}
