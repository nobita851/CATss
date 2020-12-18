package com.example.cat.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class customlist extends ListView {
    public customlist(Context context) {
        super(context);
    }
    public customlist(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public customlist(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        setSelection(getCount());

    }
}

