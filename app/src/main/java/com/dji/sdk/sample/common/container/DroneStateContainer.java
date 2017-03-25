package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.droneState.api.I_CameraInitializer;
import com.dji.sdk.sample.common.droneState.api.I_FlightControllerInitializer;
import com.dji.sdk.sample.common.droneState.src.BatteryTemperatureWarningNotifier;
import com.dji.sdk.sample.common.droneState.src.CameraInitializer;
import com.dji.sdk.sample.common.droneState.src.CameraState;
import com.dji.sdk.sample.common.droneState.src.FlightControllerInitializer;
import com.dji.sdk.sample.common.droneState.src.FlightControllerUpdateSystemStateCallback;
import com.dji.sdk.sample.common.entity.CameraSettingsEntity;
import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.integration.api.I_BatteryStateUpdateCallback;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.utility.I_MissionErrorNotifier;

/**
 * Created by Julia on 2017-03-25.
 */

public class DroneStateContainer
{
    private BatteryTemperatureWarningNotifier batteryTemperatureWarningNotifier_;

    private CameraState cameraState_;
    private CameraInitializer cameraInitializer_;

    private FlightControllerUpdateSystemStateCallback flightControllerUpdateSystemStateCallback_;
    private FlightControllerInitializer flightControllerInitializer_;

    public DroneStateContainer(
            I_MissionErrorNotifier missionErrorNotifier,
            IntegrationLayerContainer integrationLayerContainer,
            I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback,
            CameraSettingsEntity cameraSettings,
            DroneLocationEntity droneLocationEntity)
    {
        batteryTemperatureWarningNotifier_ = new BatteryTemperatureWarningNotifier(
                missionErrorNotifier);

        cameraState_ = new CameraState();
        cameraInitializer_ = new CameraInitializer(
                integrationLayerContainer.cameraSource(),
                integrationLayerContainer.gimbalSource(),
                cameraGeneratedNewMediaFileCallback,
                cameraState_,
                cameraSettings);

        flightControllerUpdateSystemStateCallback_ = new FlightControllerUpdateSystemStateCallback(
                droneLocationEntity);

        flightControllerInitializer_ = new FlightControllerInitializer(
                integrationLayerContainer.flightControllerSource(),
                flightControllerUpdateSystemStateCallback_);
    }

    public CameraState cameraState()
    {
        return cameraState_;
    }

    public I_BatteryStateUpdateCallback batteryStateUpdateCallback()
    {
        return batteryTemperatureWarningNotifier_;
    }

    public I_FlightControllerInitializer flightControllerInitializer()
    {
        return flightControllerInitializer_;
    }

    public I_CameraInitializer cameraInitializer()
    {
        return cameraInitializer_;
    }
}
