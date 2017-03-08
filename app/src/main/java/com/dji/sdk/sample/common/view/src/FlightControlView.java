package com.dji.sdk.sample.common.view.src;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.view.api.I_MissionControlsView;

/**
 * Created by Julia on 2017-03-08.
 */

public class FlightControlView
        extends RelativeLayout
        implements I_MissionControlsView
{
    private Button acceptAreaButton_;
    private Button startMissionButton_;
    private Button cancelButton_;
    private ToggleButton hoverNowToggleButton_;

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

    private void initUI()
    {
        inflate(getContext(), R.layout.flight_control_screen, this);

        acceptAreaButton_ = (Button) findViewById(R.id.btn_accept_area);
        startMissionButton_ = (Button) findViewById(R.id.btn_start_mission);
        cancelButton_ = (Button) findViewById(R.id.btn_cancel);
        hoverNowToggleButton_ = (ToggleButton) findViewById(R.id.btn_toggle_hover_now);
    }

    @Override
    public Button acceptAreaButton() {
        return acceptAreaButton_;
    }

    @Override
    public Button startMissionButton() {
        return startMissionButton_;
    }

    @Override
    public Button cancelButton() {
        return cancelButton_;
    }

    @Override
    public ToggleButton hoverNowToggleButton() {
        return hoverNowToggleButton_;
    }
}
