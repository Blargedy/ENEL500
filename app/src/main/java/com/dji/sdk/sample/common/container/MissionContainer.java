package com.dji.sdk.sample.common.container;

import android.content.Context;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.mission.src.CustomMissionBuilder;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.mission.src.MissionCanceller;
import com.dji.sdk.sample.common.mission.src.MissionController;
import com.dji.sdk.sample.common.mission.src.MissionGenerator;
import com.dji.sdk.sample.common.mission.src.MissionStepCompletionCallback;
import com.dji.sdk.sample.common.mission.src.MissionPreparer;
import com.dji.sdk.sample.common.mission.src.WaypointReachedHandler;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionContainer
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private MissionStateEntity missionState_;

    private MissionController missionController_;

    private WaypointReachedHandler waypointReachedHandler_;
    private MissionStepCompletionCallback missionStepCompletionCallback_;
    private CustomMissionBuilder customMissionBuilder_;
    private MissionPreparer missionPreparer_;
    private MissionGenerator missionGenerator_;
    private MissionCanceller missionCanceller_;

    public MissionContainer(
            IntegrationLayerContainer integrationLayerContainer,
            ImageTransferContainer imageTransferContainer,
            Context context)
    {

        initialMissionModel_ = new InitialMissionModel();
        generatedMissionModel_ = new GeneratedMissionModel();
        missionState_ = new MissionStateEntity(
                context);

        missionController_ = new MissionController(
                integrationLayerContainer.missionManagerSource());

        waypointReachedHandler_ = new WaypointReachedHandler(
                context,
                missionController_,
                imageTransferContainer.imageTransferer());
        missionStepCompletionCallback_ = new MissionStepCompletionCallback(
                waypointReachedHandler_,
                context);
        customMissionBuilder_ = new CustomMissionBuilder(
                initialMissionModel_,
                generatedMissionModel_,
                missionStepCompletionCallback_);
        missionPreparer_ = new MissionPreparer(
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                generatedMissionModel_);
        missionGenerator_ = new MissionGenerator(
                customMissionBuilder_,
                missionPreparer_,
                imageTransferContainer.imageTransferModuleInitializer());
        missionCanceller_ = new MissionCanceller(
                context,
                missionState_,
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource());
    }

    public InitialMissionModel initialMissionModel()
    {
        return initialMissionModel_;
    }

    public GeneratedMissionModel generatedMissionModel()
    {
        return generatedMissionModel_;
    }

    public I_MissionController missionController()
    {
        return missionController_;
    }

    public I_MissionGenerator missionGenerator()
    {
        return missionGenerator_;
    }

    public MissionStateEntity missionState()
    {
        return missionState_;
    }
}
