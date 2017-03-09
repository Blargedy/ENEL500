package com.dji.sdk.sample.common.view.src;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.view.api.I_MapView;
import com.dji.sdk.sample.common.view.api.I_MissionControlsView;

/**
 * Created by Julia on 2017-03-08.
 */

public class FlightControlView
        extends RelativeLayout
        implements I_MissionControlsView, I_MapView

{
    private SeekBar surveyProgressBar_;
    private TextView surveyAreaHeightText_;
    private SeekBar surveyAreaHeightBar_;
    private TextView surveyAreaWidthText_;
    private SeekBar surveyAreaWidthBar_;

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

        surveyProgressBar_ = (SeekBar) findViewById(R.id.pbar_surveyProgressTracking);
        surveyAreaHeightText_ = (TextView) findViewById(R.id.txt_surveyAreaHeight);
        surveyAreaHeightBar_ = (SeekBar) findViewById(R.id.pbar_surveyAreaHeight);
        surveyAreaWidthText_ = (TextView) findViewById(R.id.txt_surveyAreaWidth);
        surveyAreaWidthBar_ = (SeekBar) findViewById(R.id.pbar_surveyAreaWidth);

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

    @Override
    public void enableAllControls()
    {
        acceptAreaButton_.setEnabled(true);
        startMissionButton_.setEnabled(true);
        cancelButton_.setEnabled(true);
        hoverNowToggleButton_.setEnabled(true);
    }

    @Override
    public void disableAllControls()
    {
        acceptAreaButton_.setEnabled(false);
        startMissionButton_.setEnabled(false);
        cancelButton_.setEnabled(false);
        hoverNowToggleButton_.setEnabled(false);
    }

    @Override
    public SeekBar surveyProgressBar() {
        return null;
    }

    @Override
    public TextView surveyAreaHeightText() {
        return surveyAreaHeightText_;
    }

    @Override
    public SeekBar surveyAreaHeightBar() {
        return surveyAreaHeightBar_;
    }

    @Override
    public TextView surveyAreaWidthText() {
        return surveyAreaWidthText_;
    }

    @Override
    public SeekBar surveyAreaWidthBar() {
        return surveyAreaWidthBar_;
    }
}
