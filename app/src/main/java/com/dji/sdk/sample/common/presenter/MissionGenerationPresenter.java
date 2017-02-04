package com.dji.sdk.sample.common.presenter;

import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.mission.MissionGenerator;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionGenerationPresenter implements View.OnClickListener
{
    Button generateMissionButton_;
    Button startMissionButton_;
    MissionGenerator missionGenerator_;

    public MissionGenerationPresenter(
            Button generateMissionButton,
            Button startMissionButton,
            MissionGenerator missionGenerator)
    {
        generateMissionButton_ = generateMissionButton;
        generateMissionButton_.setOnClickListener(this);

        startMissionButton_ = startMissionButton;

        missionGenerator_ = missionGenerator;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == generateMissionButton_.getId())
        {
            //missionGenerator_.generateMission()
            startMissionButton_.setEnabled(true);
        }
    }
}
