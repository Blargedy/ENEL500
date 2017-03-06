package com.dji.sdk.sample.common.mission.api;

import dji.sdk.missionmanager.DJICustomMission;

/**
 * Created by Julia on 2017-02-04.
 */

public interface I_MissionGenerator
{
    void generateMission(I_MissionGenerationCompletionCallback callback);
}
