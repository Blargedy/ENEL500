package com.dji.sdk.sample.common.container;

import android.content.Context;

import com.dji.sdk.sample.common.entity.CameraSettingsEntity;
import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.api.I_MissionStateResetter;
import com.dji.sdk.sample.common.mission.api.InertCameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.InertMissionPeriodicImageTransferInitiator;
import com.dji.sdk.sample.common.mission.src.BatteryTemperatureWarningNotifier;
import com.dji.sdk.sample.common.mission.src.CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.src.CameraInitializer;
import com.dji.sdk.sample.common.mission.src.FlightControllerInitializer;
import com.dji.sdk.sample.common.mission.src.CameraState;
import com.dji.sdk.sample.common.mission.src.MissionGenerator;
import com.dji.sdk.sample.common.mission.src.DroneLocationUpdater;
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

    private BatteryTemperatureWarningNotifier batteryTemperatureWarningNotifier_;
    private CameraState cameraSystemState_;
//    private InvestigativeWaypointReachedHandler investigatingImageTransfer_;

    private I_MissionPeriodicImageTransferInitiator periodicImageTransferInitiator_;
    private I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback_;
    private CameraInitializer cameraInitializer_;
    private FlightControllerInitializer flightControllerInitializer_;

    private WaypointImageShooter waypointImageShooter_;
    private WaypointReachedNotifier waypointReachedNotifier_;
    private DroneLocationUpdater droneLocationUpdater_;
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

        batteryTemperatureWarningNotifier_ = new BatteryTemperatureWarningNotifier(
                missionErrorNotifier);

        cameraSystemState_ = new CameraState();
//        investigatingImageTransfer_ = new InvestigativeWaypointReachedHandler(
//                integrationLayerContainer.missionManagerSource(),
//                integrationLayerContainer.cameraSource(),
//                cameraSystemState_,
//                imageTransferContainer.imageTransferPathsSource());

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

        cameraInitializer_ = new CameraInitializer(
                integrationLayerContainer.cameraSource(),
                integrationLayerContainer.gimbalSource(),
                cameraGeneratedNewMediaFileCallback_ /*investigatingImageTransfer_*/,
                cameraSystemState_,
                cameraSettings_);
        flightControllerInitializer_ = new FlightControllerInitializer(
                integrationLayerContainer.flightControllerSource());

        waypointImageShooter_ = new WaypointImageShooter(
                missionErrorNotifier,
                integrationLayerContainer.cameraSource(),
                cameraSystemState_);
        waypointReachedNotifier_ = new WaypointReachedNotifier(context);
        droneLocationUpdater_ = new DroneLocationUpdater(
                droneLocation_,
                integrationLayerContainer.flightControllerSource());
        missionProgressStatusCallback_ = new WaypointMissionProgressStatusCallback(
                missionErrorNotifier,
                waypointReachedNotifier_ /*investigatingImageTransfer_*/,
                droneLocationUpdater_,
                waypointImageShooter_);

        missionStateResetter_ = new MissionStateResetter(
                generatedMissionModel_,
                missionProgressStatusCallback_,
                cameraGeneratedNewMediaFileCallback_,
                imageTransferContainer.imageTransferModuleEnder(),
                batteryTemperatureWarningNotifier_);
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
                flightControllerInitializer_,
                cameraInitializer_,
                imageTransferContainer.imageTransferModuleInitializer(),
                waypointMissionCompletionCallback_,
                missionProgressStatusCallback_,
                integrationLayerContainer.batterySource(),
                batteryTemperatureWarningNotifier_);
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
}
