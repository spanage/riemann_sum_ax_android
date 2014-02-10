package com.sommerpanage.riemannsum.android;

import android.graphics.Rect;

/**
 * Created by spanage on 1/31/14.
 */
abstract public class GraphVirtualView {
    protected Rect mViewRect;
    protected String mContentDescription;

    public Rect getViewRect() {
        return mViewRect;
    }

    public String getContentDescription() {
        return mContentDescription;
    }
}
