package com.dji.sdk.sample.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dji.sdk.sample.R;

/**
 * Created by Julia on 2017-01-15.
 */

public class SimpleDemoView
        extends RelativeLayout
        implements I_ShootPhotoView, I_BasicFlightControlView, I_AircraftConnectionView
{
    private TextView connectionStatusText_;
    private Button takeOffButton_;
    private Button landButton_;
    private Button shootPhotoButton_;

    public SimpleDemoView(
            Context context) {
        super(context);
        initUI();
    }

    public SimpleDemoView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public SimpleDemoView(
            Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    @Override
    public TextView connectionStatusText()
    {
        return connectionStatusText_;
    }

    @Override
    public Button takeOffButton()
    {
        return takeOffButton_;
    }

    @Override
    public Button landButton()
    {
        return landButton_;
    }

    @Override
    public Button shootPhotoButton()
    {
        return shootPhotoButton_;
    }

    private void initUI()
    {
        inflate(getContext(), R.layout.simple_demo, this);

        connectionStatusText_ = (TextView) findViewById(R.id.text_connection_status);

        takeOffButton_ = (Button) findViewById(R.id.btn_open_flight_control);
        takeOffButton_.setEnabled(false);

        landButton_ = (Button) findViewById(R.id.btn_land);
        landButton_.setVisibility(View.GONE);

        shootPhotoButton_ = (Button) findViewById(R.id.btn_shoot_photo);
        shootPhotoButton_.setEnabled(false);
    }
}
