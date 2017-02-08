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

public class FlightControlView
        extends RelativeLayout
{
    private TextView connectionStatusText_;
    private Button generateMissionButton_;
    private Button startMissionButton_;
    private Button shootPhotoButton_;

    public FlightControlView(
            Context context) {
        super(context);
        initUI();
    }

    public FlightControlView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public FlightControlView(
            Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    public TextView connectionStatusText()
    {
        return connectionStatusText_;
    }

    public Button generateMissionButton()
    {
        return generateMissionButton_;
    }

    public Button startMissionButton()
    {
        return startMissionButton_;
    }

    public Button shootPhotoButton()
    {
        return shootPhotoButton_;
    }

    private void initUI()
    {
        inflate(getContext(), R.layout.simple_demo, this);

        connectionStatusText_ = (TextView) findViewById(R.id.text_connection_status);
        generateMissionButton_ = (Button) findViewById(R.id.btn_generate_mission);
        startMissionButton_ = (Button) findViewById(R.id.btn_start_mission);
        shootPhotoButton_ = (Button) findViewById(R.id.btn_shoot_photo);
    }
}
