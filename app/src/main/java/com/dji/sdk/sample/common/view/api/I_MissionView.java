package com.dji.sdk.sample.common.view.api;

import com.dji.sdk.sample.common.entity.MissionStateEnum;

/**
 * Created by Julia on 2017-03-08.
 */

public interface I_MissionView
{
    MissionStateEnum currentMissionState();
    void setCurrentMissionState(MissionStateEnum state);
}
