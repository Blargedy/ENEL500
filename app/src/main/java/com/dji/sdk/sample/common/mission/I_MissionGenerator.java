package com.dji.sdk.sample.common.mission;

import dji.sdk.missionmanager.DJICustomMission;

/**
 * Created by Julia on 2017-02-04.
 */

public interface I_MissionGenerator {

    public void generateMission(MissionBoundary boundary, double altitude);

}
