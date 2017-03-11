package com.dji.sdk.sample.common.container;

import android.content.Context;

import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.mission.api.I_MissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.api.InertMissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.src.CustomMissionBuilder;
import com.dji.sdk.sample.common.mission.src.DroneLocationUpdater;
import com.dji.sdk.sample.common.mission.src.MissionCanceller;
import com.dji.sdk.sample.common.mission.src.MissionExecutor;
import com.dji.sdk.sample.common.mission.src.MissionGenerator;
import com.dji.sdk.sample.common.mission.src.MissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.src.NextWaypointMissionStarter;
import com.dji.sdk.sample.common.mission.src.WaypointMissionCompletionCallback;
import com.dji.sdk.sample.common.mission.src.WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.src.WaypointReachedNotifier;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionContainer
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private MissionStateEntity missionState_;
    private DroneLocationEntity droneLocation_;

    private NextWaypointMissionStarter nextWaypointMissionStarter_;
    private WaypointMissionCompletionCallback waypointMissionCompletionCallback_;

    private I_MissionPeriodicImageTransferInitiator periodicImageTransferInitiator_;
    private WaypointReachedNotifier waypointReachedNotifier_;
    private DroneLocationUpdater droneLocationUpdater_;
    private WaypointMissionProgressStatusCallback missionProgressStatusCallback_;

    private CustomMissionBuilder customMissionBuilder_;
    private MissionGenerator missionGenerator_;

    private MissionExecutor missionExecutor_;
    private MissionCanceller missionCanceller_;

    public MissionContainer(
            Context context,
            IntegrationLayerContainer integrationLayerContainer,
            ImageTransferContainer imageTransferContainer,
            boolean isLiveModeEnabled)
    {

        initialMissionModel_ = new InitialMissionModel();
        generatedMissionModel_ = new GeneratedMissionModel();
        missionState_ = new MissionStateEntity(context);
        droneLocation_ = new DroneLocationEntity(context);

        nextWaypointMissionStarter_ = new NextWaypointMissionStarter(
                integrationLayerContainer.missionManagerSource(),
                generatedMissionModel(),
                missionState_);
        waypointMissionCompletionCallback_ = new WaypointMissionCompletionCallback(
                nextWaypointMissionStarter_);

        if (isLiveModeEnabled){
            periodicImageTransferInitiator_ = new MissionPeriodicImageTransferInitiator(
                    integrationLayerContainer.missionManagerSource(),
                    imageTransferContainer.imageTransferer());
        } else{
            periodicImageTransferInitiator_ = new InertMissionPeriodicImageTransferInitiator();
        }
        waypointReachedNotifier_ = new WaypointReachedNotifier(context);
        droneLocationUpdater_ = new DroneLocationUpdater(
                droneLocation_,
                integrationLayerContainer.flightControllerSource());
        missionProgressStatusCallback_ = new WaypointMissionProgressStatusCallback(
                periodicImageTransferInitiator_,
                waypointReachedNotifier_,
                droneLocationUpdater_);

        customMissionBuilder_ = new CustomMissionBuilder(
                initialMissionModel_,
                generatedMissionModel_);
        missionGenerator_ = new MissionGenerator(
                customMissionBuilder_,
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                imageTransferContainer.imageTransferModuleInitializer(),
                waypointMissionCompletionCallback_,
                missionProgressStatusCallback_);

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

    public DroneLocationEntity droneLocation()
    {
        return droneLocation_;
    }
}
