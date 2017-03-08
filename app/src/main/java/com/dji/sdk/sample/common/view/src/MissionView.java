package com.dji.sdk.sample.common.view.src;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.view.api.I_MissionView;

/**
 * Created by Julia on 2017-03-08.
 */

public class MissionView extends RelativeLayout implements
        I_MissionView,
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener
{
    private MissionStateEnum currentState_;

    private Button acceptAreaButton_;
    private Button startMissionButton_;
    private Button cancelButton_;
    private ToggleButton hoverNowToggleButton_;

    public MissionView(
            Context context) {
        super(context);
        initUI();
    }

    public MissionView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MissionView(
            Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI()
    {
        inflate(getContext(), R.layout.mission_controls, this);

        acceptAreaButton_ = (Button) findViewById(R.id.btn_accept_area);
        startMissionButton_ = (Button) findViewById(R.id.btn_start_mission);
        cancelButton_ = (Button) findViewById(R.id.btn_cancel);
        hoverNowToggleButton_ = (ToggleButton) findViewById(R.id.btn_toggle_hover_now);
        setButtonOnClickListeners();

        currentState_ = MissionStateEnum.SELECT_AREA;
        currentMissionStateChanged();
    }

    @Override
    public MissionStateEnum currentMissionState()
    {
        return currentState_;
    }

    @Override
    public void setCurrentMissionState(MissionStateEnum state)
    {
        currentState_ = state;
        currentMissionStateChanged();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == acceptAreaButton_.getId()) {
            currentState_ = MissionStateEnum.VIEW_MISSION;
        } else if (v.getId() == startMissionButton_.getId()) {
            currentState_ = MissionStateEnum.EXECUTE_MISSION;
        } else if (v.getId() == cancelButton_.getId()) {
            cancelButtonPressed();
        }
        currentMissionStateChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView.getId() == hoverNowToggleButton_.getId())
        {
            hoverNowToggleButtonPressed(isChecked);
        }
        currentMissionStateChanged();
    }

    private void setButtonOnClickListeners()
    {
        acceptAreaButton_.setOnClickListener(this);
        startMissionButton_.setOnClickListener(this);
        cancelButton_.setOnClickListener(this);
        hoverNowToggleButton_.setOnCheckedChangeListener(this);
    }

    private void cancelButtonPressed()
    {
        switch (currentState_)
        {
            case VIEW_MISSION:
                currentState_ = MissionStateEnum.SELECT_AREA;
                break;

            case EXECUTE_MISSION:
                currentState_ = MissionStateEnum.CANCEL_MISSION;
                break;

            case PAUSE_MISSION:
                currentState_ = MissionStateEnum.CANCEL_MISSION;
                break;

            default:
                break;
        }
    }

    private void hoverNowToggleButtonPressed(boolean isChecked)
    {
        switch (currentState_)
        {
            case EXECUTE_MISSION:
                currentState_ = MissionStateEnum.PAUSE_MISSION;
                break;

            case PAUSE_MISSION:
                currentState_ = MissionStateEnum.EXECUTE_MISSION;
                break;

            case CANCEL_MISSION:
                currentState_ = MissionStateEnum.PAUSE_CANCEL_MISSION;
                break;

            case PAUSE_CANCEL_MISSION:
                currentState_ = MissionStateEnum.CANCEL_MISSION;
                break;

            default:
                break;
        }
    }

    private void currentMissionStateChanged()
    {
        Intent intent = new Intent(BroadcastIntentNames.MISSION_STATE_CHANGED);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        setViewBasedOnMissionState();
    }

    private void setViewBasedOnMissionState()
    {
        switch (currentState_)
        {
            case SELECT_AREA:
                acceptAreaButton_.setVisibility(View.VISIBLE);
                startMissionButton_.setVisibility(View.GONE);
                cancelButton_.setVisibility(View.GONE);
                hoverNowToggleButton_.setVisibility(View.GONE);
                break;

            case VIEW_MISSION:
                acceptAreaButton_.setVisibility(View.GONE);
                startMissionButton_.setVisibility(View.VISIBLE);
                cancelButton_.setVisibility(View.VISIBLE);
                hoverNowToggleButton_.setVisibility(View.GONE);
                break;

            case EXECUTE_MISSION:
                acceptAreaButton_.setVisibility(View.GONE);
                startMissionButton_.setVisibility(View.GONE);
                cancelButton_.setVisibility(View.VISIBLE);
                hoverNowToggleButton_.setVisibility(View.VISIBLE);
                break;

            case PAUSE_MISSION:
                acceptAreaButton_.setVisibility(View.GONE);
                startMissionButton_.setVisibility(View.GONE);
                cancelButton_.setVisibility(View.VISIBLE);
                hoverNowToggleButton_.setVisibility(View.VISIBLE);
                break;

            case CANCEL_MISSION:
                acceptAreaButton_.setVisibility(View.GONE);
                startMissionButton_.setVisibility(View.GONE);
                cancelButton_.setVisibility(View.VISIBLE);
                hoverNowToggleButton_.setVisibility(View.VISIBLE);

                cancelButton_.setEnabled(false);
                break;

            case PAUSE_CANCEL_MISSION:
                acceptAreaButton_.setVisibility(View.GONE);
                startMissionButton_.setVisibility(View.GONE);
                cancelButton_.setVisibility(View.VISIBLE);
                hoverNowToggleButton_.setVisibility(View.VISIBLE);

                cancelButton_.setEnabled(false);
                break;

            default:
                break;
        }
    }
}
