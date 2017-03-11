package com.dji.sdk.sample.common.container;

import android.content.Context;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.mission.api.I_NextWaypointMissionStarter;
import com.dji.sdk.sample.common.mission.src.CustomMissionBuilder;
import com.dji.sdk.sample.common.mission.src.MissionCanceller;
import com.dji.sdk.sample.common.mission.src.MissionExecutor;
import com.dji.sdk.sample.common.mission.src.MissionGenerator;
import com.dji.sdk.sample.common.mission.src.MissionStepCompletionCallback;
import com.dji.sdk.sample.common.mission.src.NextWaypointMissionStarter;
import com.dji.sdk.sample.common.mission.src.WaypointMissionCompletionCallback;
import com.dji.sdk.sample.common.mission.src.WaypointReachedHandler;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionContainer
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private MissionStateEntity missionState_;

    private I_NextWaypointMissionStarter nextWaypointMissionStarter_;
    private WaypointMissionCompletionCallback waypointMissionCompletionCallback_;

    private WaypointReachedHandler waypointReachedHandler_;
    private MissionStepCompletionCallback missionStepCompletionCallback_;
    private CustomMissionBuilder customMissionBuilder_;
    private MissionGenerator missionGenerator_;

    private MissionExecutor missionExecutor_;
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

        nextWaypointMissionStarter_ = new NextWaypointMissionStarter(
                integrationLayerContainer.missionManagerSource(),
                generatedMissionModel(),
                missionState_);
        waypointMissionCompletionCallback_ = new WaypointMissionCompletionCallback(
                nextWaypointMissionStarter_);

        waypointReachedHandler_ = new WaypointReachedHandler(
                context,
                imageTransferContainer.imageTransferer());
        missionStepCompletionCallback_ = new MissionStepCompletionCallback(
                waypointReachedHandler_,
                context);
        customMissionBuilder_ = new CustomMissionBuilder(
                initialMissionModel_,
                generatedMissionModel_,
                missionStepCompletionCallback_);
        missionGenerator_ = new MissionGenerator(
                customMissionBuilder_,
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                imageTransferContainer.imageTransferModuleInitializer(),
                waypointMissionCompletionCallback_);

        missionExecutor_ = new MissionExecutor(
                context,
                missionGenerator_,
                nextWaypointMissionStarter_,
                integrationLayerContainer.missionManagerSource(),
                missionState_);
        missionCanceller_ = new MissionCanceller(
                context,
                missionState_,
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                imageTransferContainer.imageTransferModuleEnder());
    }

    public InitialMissionModel initialMissionModel()
    {
        return initialMissionModel_;
    }

    public GeneratedMissionModel generatedMissionModel()
    {
        return generatedMissionModel_;
    }

    public MissionStateEntity missionState()
    {
        return missionState_;
    }
}
