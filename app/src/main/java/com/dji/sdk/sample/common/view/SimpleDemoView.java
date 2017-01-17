package com.dji.sdk.sample.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dji.sdk.sample.R;

/**
 * Created by Julia on 2017-01-15.
 */

public class SimpleDemoView extends RelativeLayout
{
    private TextView connectionStatusText_;
    private Button hoverNowButton_;
    private Button landButton_;

    public SimpleDemoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public TextView connectionStatusText()
    {
        return connectionStatusText_;
    }

    public Button hoverNowButton()
    {
        return hoverNowButton_;
    }

    public Button landButton()
    {
        return landButton_;
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        initUI();
    }

    private void initUI()
    {
        connectionStatusText_ = (TextView) findViewById(R.id.text_connection_status);
        hoverNowButton_ = (Button) findViewById(R.id.btn_hover);
        landButton_ = (Button) findViewById(R.id.btn_execute_mission);
    }
}
