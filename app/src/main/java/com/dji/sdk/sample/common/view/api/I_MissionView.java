package com.dji.sdk.sample.common.view.api;

import com.dji.sdk.sample.common.view.src.MissionState;

/**
 * Created by Julia on 2017-03-08.
 */

public interface I_MissionView
{
    MissionState currentMissionState();
    void setCurrentMissionState(MissionState state);
}
