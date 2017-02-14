package com.dji.sdk.sample.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.dji.sdk.sample.R;

/**
 * Created by Julia on 2017-01-15.
 */

public class MapView
        extends RelativeLayout
{
    //private TextView connectionStatusText_;


    public MapView(
            Context context) {
        super(context);
        initUI();
    }

    public MapView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MapView(
            Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI()
    {
        inflate(getContext(), R.layout.map_screen, this);
    }
}
