package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleEnder;
import com.dji.sdk.sample.common.integration.api.I_BatteryStateUpdateCallback;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionStateResetter;

/**
 * Created by Julia on 2017-03-25.
 */

public class MissionStateResetter implements I_MissionStateResetter
{
    private GeneratedMissionModel generatedMissionModel_;
    private I_WaypointMissionProgressStatusCallback missionProgressStatusCallback_;
    private I_ImageTransferModuleEnder imageTransferModuleEnder_;
    private I_BatteryStateUpdateCallback batteryStateUpdateCallback_;


    public MissionStateResetter(
            GeneratedMissionModel generatedMissionModel,
            I_WaypointMissionProgressStatusCallback missionProgressStatusCallback,
            I_ImageTransferModuleEnder imageTransferModuleEnder,
            I_BatteryStateUpdateCallback batteryStateUpdateCallback)
    {
        generatedMissionModel_ = generatedMissionModel;
        missionProgressStatusCallback_ = missionProgressStatusCallback;
        imageTransferModuleEnder_ = imageTransferModuleEnder;
        batteryStateUpdateCallback_ = batteryStateUpdateCallback;
    }

    @Override
    public void resetMissionState()
    {
        generatedMissionModel_.clearWaypointMissions();
        missionProgressStatusCallback_.resetWaypointCount();
        imageTransferModuleEnder_.endImageTransfer(null);
        batteryStateUpdateCallback_.resetIfWarningHasBeenShown();
    }
}
