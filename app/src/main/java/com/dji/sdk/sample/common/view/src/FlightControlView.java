package com.dji.sdk.sample.common.view.src;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.view.api.I_ErrorConsoleView;
import com.dji.sdk.sample.common.view.api.I_MapView;
import com.dji.sdk.sample.common.view.api.I_MissionControlsView;

/**
 * Created by Julia on 2017-03-08.
 */

public class FlightControlView
        extends LinearLayout
        implements I_MissionControlsView, I_MapView, I_ErrorConsoleView
{
    private TextView errorConsole_;

    private SeekBar surveyProgressBar_;
    private TextView surveyAreaHeightText_;
    private SeekBar surveyAreaHeightBar_;
    private TextView surveyAreaWidthText_;
    private SeekBar surveyAreaWidthBar_;
    private LinearLayout linearLayoutMainV_;
    private TextView txtPercentCompleteMap_;

    private Button acceptAreaButton_;
    private Button startMissionButton_;
    private Button cancelButton_;
    private ToggleButton hoverNowToggleButton_;
    private Button settingsButton_;
    private Button btnFindMeNow_;

    private ProgressBar loadingProgressAnimation_;

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

        errorConsole_ = (TextView) findViewById(R.id.txt_console);

        surveyProgressBar_ = (SeekBar) findViewById(R.id.surveyProgressBar);
        surveyAreaHeightText_ = (TextView) findViewById(R.id.txt_surveyAreaHeight);
        surveyAreaHeightBar_ = (SeekBar) findViewById(R.id.pbar_surveyAreaHeight);
        surveyAreaWidthText_ = (TextView) findViewById(R.id.txt_surveyAreaWidth);
        surveyAreaWidthBar_ = (SeekBar) findViewById(R.id.pbar_surveyAreaWidth);
        linearLayoutMainV_ = (LinearLayout) findViewById(R.id.linearLayoutMainV);

        acceptAreaButton_ = (Button) findViewById(R.id.btn_accept_area);
        startMissionButton_ = (Button) findViewById(R.id.btn_start_mission);
        cancelButton_ = (Button) findViewById(R.id.btn_cancel);
        hoverNowToggleButton_ = (ToggleButton) findViewById(R.id.btn_toggle_hover_now);
        settingsButton_ = (Button) findViewById(R.id.btn_settings);

        txtPercentCompleteMap_ = (TextView)findViewById(R.id.txtPercentCompleteMap);

        btnFindMeNow_ = (Button)findViewById(R.id.btnFindMeNow);
        loadingProgressAnimation_ = (ProgressBar)findViewById(R.id.loadingProgressAnimation);
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
    public SeekBar surveyProgressBar() {
        return surveyProgressBar_;
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

    @Override
    public Button settingsButton()
    {
        return settingsButton_;
    }

    @Override
    public LinearLayout linearLayoutMainV()
    {
        return linearLayoutMainV_;
    }

    @Override
    public TextView errorConsoleText()
    {
        return errorConsole_;
    }

    @Override
    public TextView txtPercentCompleteMap()
    {
        return txtPercentCompleteMap_;
    }

    @Override
    public Button btnFindMeNow()
    {
        return btnFindMeNow_;
    }

    @Override
    public ProgressBar loadingProgressAnimation()
    {
        return loadingProgressAnimation_;
    }


}
