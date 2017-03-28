package com.dji.sdk.sample.common.view.src;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dji.sdk.sample.R;

/**
 * Created by Julia on 2017-03-26.
 */

public class AndroidPcConnectionView
        extends LinearLayout
{
    private EditText pcIpAddress_;
    private TextView connectionStatus_;
    private Button testConnectionButton_;
    private Button continueButton_;
    private ImageView droneImage_;

    public AndroidPcConnectionView(
            Context context) {
        super(context);
        initUI();
    }

    public AndroidPcConnectionView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public AndroidPcConnectionView(
            Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    public EditText pcIpAddress()
    {
        return pcIpAddress_;
    }

    public TextView connectionStatus()
    {
        return connectionStatus_;
    }

    public Button testConnectionButton()
    {
        return testConnectionButton_;
    }

    public Button continueButton()
    {
        return continueButton_;
    }

    public ImageView droneImage()
    {
        return droneImage_;
    }

    private void initUI()
    {
        inflate(getContext(), R.layout.android_pc_connection_screen, this);
        //droneImage_ = (ImageView) findViewById(R.id.droneImage);
        pcIpAddress_ = (EditText) findViewById(R.id.ipAddress);
        connectionStatus_ = (TextView) findViewById(R.id.txt_connectionStatus);
        testConnectionButton_ = (Button) findViewById(R.id.btn_testConnection);
        continueButton_ = (Button) findViewById(R.id.btn_continue);

        //droneImage_.setAlpha(1.0f);
        //droneImage().setEnabled(true);
        //droneImage_.setVisibility(View.VISIBLE);
    }
}
