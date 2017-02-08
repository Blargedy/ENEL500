package com.dji.sdk.sample.common.mission;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;

/**
 * Created by Matthew on 2017-02-08.
 */

public class MissionController implements I_MissionController {
    private GeneratedMissionModel missionModel_;

    public MissionController(GeneratedMissionModel missionModel)
    {
        missionModel_ = missionModel;
    }

    public void startMission()
    {

    }
}
