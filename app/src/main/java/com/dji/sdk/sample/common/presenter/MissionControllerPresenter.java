package com.dji.sdk.sample.common.presenter;

import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.mission.MissionController;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionControllerPresenter implements View.OnClickListener
{
    private Button startMissionButton_;
    private MissionController controller_;
    // Image transfer service
    // Periodically pause the mission, trigger a transfer and restart the mission

    public MissionControllerPresenter(
            Button startMissionButton,
            MissionController controller)
    {
        startMissionButton_ = startMissionButton;
        controller_ = controller;
        startMissionButton_.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == startMissionButton_.getId())
        {
            controller_.takeOff();
            controller_.startMission();
        }
    }
}
