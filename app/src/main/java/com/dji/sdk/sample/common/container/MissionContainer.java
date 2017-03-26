package com.dji.sdk.sample.common.container;

import android.content.Context;

import com.dji.sdk.sample.common.droneState.api.I_FlightControllerInitializer;
import com.dji.sdk.sample.common.entity.CameraSettingsEntity;
import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.api.InertCameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.InertMissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.src.CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.src.MissionGenerator;
import com.dji.sdk.sample.common.mission.src.MissionCanceller;
import com.dji.sdk.sample.common.mission.src.MissionExecutor;
import com.dji.sdk.sample.common.mission.src.MissionInitializer;
import com.dji.sdk.sample.common.mission.src.MissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.src.MissionStateResetter;
import com.dji.sdk.sample.common.mission.src.NextWaypointMissionStarter;
import com.dji.sdk.sample.common.mission.src.SwitchBackPathGenerator;
import com.dji.sdk.sample.common.mission.src.WaypointImageShooter;
import com.dji.sdk.sample.common.mission.src.WaypointMissionCompletionCallback;
import com.dji.sdk.sample.common.mission.src.WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.src.WaypointReachedNotifier;
import com.dji.sdk.sample.common.utility.ApplicationSettingsManager;
import com.dji.sdk.sample.common.utility.I_MissionErrorNotifier;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionContainer
{
    private CameraSettingsEntity cameraSettings_;
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private MissionStateEntity missionState_;
    private DroneLocationEntity droneLocation_;

    private I_MissionPeriodicImageTransferInitiator periodicImageTransferInitiator_;
    private I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback_;

    private DroneStateContainer droneStateContainer_;

    private WaypointImageShooter waypointImageShooter_;
    private WaypointReachedNotifier waypointReachedNotifier_;
    private WaypointMissionProgressStatusCallback missionProgressStatusCallback_;

    private MissionStateResetter missionStateResetter_;
    private NextWaypointMissionStarter nextWaypointMissionStarter_;
    private WaypointMissionCompletionCallback waypointMissionCompletionCallback_;

    private SwitchBackPathGenerator switchBackPathGenerator_;
    private MissionGenerator missionGenerator_;

    private MissionInitializer missionInitializer_;
    private MissionExecutor missionExecutor_;
    private MissionCanceller missionCanceller_;

    public MissionContainer(
            Context context,
            I_MissionErrorNotifier missionErrorNotifier,
            ApplicationSettingsManager applicationSettingsManager,
            IntegrationLayerContainer integrationLayerContainer,
            ImageTransferContainer imageTransferContainer,
            boolean isLiveModeEnabled)
    {
        cameraSettings_ = new CameraSettingsEntity(context, applicationSettingsManager);
        initialMissionModel_ = new InitialMissionModel(context, applicationSettingsManager);
        generatedMissionModel_ = new GeneratedMissionModel();
        missionState_ = new MissionStateEntity(context);
        droneLocation_ = new DroneLocationEntity(context);

        if (isLiveModeEnabled){
            periodicImageTransferInitiator_ = new MissionPeriodicImageTransferInitiator(
                    missionErrorNotifier,
                    integrationLayerContainer.missionManagerSource(),
                    imageTransferContainer.imageTransferer(),
                    imageTransferContainer.droneImageDownloadQueuer());
            cameraGeneratedNewMediaFileCallback_ = new CameraGeneratedNewMediaFileCallback(
                    imageTransferContainer.droneImageDownloadQueuer(),
                    periodicImageTransferInitiator_);
        } else{
            periodicImageTransferInitiator_ = new InertMissionPeriodicImageTransferInitiator();
            cameraGeneratedNewMediaFileCallback_ = new InertCameraGeneratedNewMediaFileCallback();
        }

        droneStateContainer_ = new DroneStateContainer(
                context,
                missionErrorNotifier,
                integrationLayerContainer,
                cameraGeneratedNewMediaFileCallback_,
                cameraSettings_,
                droneLocation_);

        waypointImageShooter_ = new WaypointImageShooter(
                missionErrorNotifier,
                integrationLayerContainer.cameraSource(),
                droneStateContainer_.cameraState());
        waypointReachedNotifier_ = new WaypointReachedNotifier(context);

        missionProgressStatusCallback_ = new WaypointMissionProgressStatusCallback(
                missionErrorNotifier,
                waypointReachedNotifier_,
                waypointImageShooter_);

        missionStateResetter_ = new MissionStateResetter(
                generatedMissionModel_,
                missionProgressStatusCallback_,
                cameraGeneratedNewMediaFileCallback_,
                imageTransferContainer.imageTransferModuleEnder(),
                droneStateContainer_.batteryStateUpdateCallback());
        nextWaypointMissionStarter_ = new NextWaypointMissionStarter(
                integrationLayerContainer.missionManagerSource(),
                generatedMissionModel(),
                missionState_);
        waypointMissionCompletionCallback_ = new WaypointMissionCompletionCallback(
                missionErrorNotifier,
                waypointReachedNotifier_,
                nextWaypointMissionStarter_,
                missionProgressStatusCallback_);

        switchBackPathGenerator_ = new SwitchBackPathGenerator(
                initialMissionModel_);
        missionGenerator_ = new MissionGenerator(
                initialMissionModel_,
                generatedMissionModel_,
                switchBackPathGenerator_);

        missionInitializer_ = new MissionInitializer(
                integrationLayerContainer.missionManagerSource(),
                droneStateContainer_.flightControllerInitializer(),
                droneStateContainer_.cameraInitializer(),
                imageTransferContainer.imageTransferModuleInitializer(),
                waypointMissionCompletionCallback_,
                missionProgressStatusCallback_,
                integrationLayerContainer.batterySource(),
                droneStateContainer_.batteryStateUpdateCallback());
        missionExecutor_ = new MissionExecutor(
                context,
                missionErrorNotifier,
                missionGenerator_,
                nextWaypointMissionStarter_,
                missionInitializer_,
                integrationLayerContainer.missionManagerSource(),
                missionState_);
        missionCanceller_ = new MissionCanceller(
                context,
                missionErrorNotifier,
                missionState_,
                integrationLayerContainer.flightControllerSource(),
                missionStateResetter_);
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

    public I_FlightControllerInitializer flightControllerInitializer()
    {
        return droneStateContainer_.flightControllerInitializer();
    }
}
