package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_CameraModeChanger;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.api.I_WaypointMissionProgressStatusCallback;
import com.dji.sdk.sample.common.mission.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionInitializer;

import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-16.
 */

public class MissionInitializer implements
        I_MissionInitializer,
        I_CompletionCallback
{
    private enum ExpectedCallback {
        SET_HOME_LOCATION,
        INITIALIZE_IMAGE_TRANSFER,
        CHANGE_CAMERA_MODE,
        SET_PHOTO_FILE_FORMAT }
    private MissionInitializer.ExpectedCallback expectedCallback_;

    private I_MissionManagerSource missionManagerSource_;
    private I_FlightControllerSource flightControllerSource_;
    private I_CameraSource cameraSource_;
    private I_ImageTransferModuleInitializer imageTransferModuleInitializer_;
    private I_CameraModeChanger cameraModeChanger_;
    private I_CompletionCallback missionExecutionCompletionCallback_;
    private I_WaypointMissionProgressStatusCallback missionProgressStatusCallback_;
    private I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback_;

    private I_CompletionCallback callback_;

    public MissionInitializer(
            I_MissionManagerSource missionManagerSource,
            I_FlightControllerSource flightControllerSource,
            I_CameraSource cameraSource,
            I_ImageTransferModuleInitializer imageTransferModuleInitializer,
            I_CameraModeChanger cameraModeChanger,
            I_CompletionCallback missionExecutionCompletionCallback,
            I_WaypointMissionProgressStatusCallback missionProgressStatusCallback,
            I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback)
    {
        missionManagerSource_ = missionManagerSource;
        flightControllerSource_ = flightControllerSource;
        cameraSource_ = cameraSource;
        imageTransferModuleInitializer_ = imageTransferModuleInitializer;
        cameraModeChanger_ = cameraModeChanger;
        missionExecutionCompletionCallback_ = missionExecutionCompletionCallback;
        missionProgressStatusCallback_ = missionProgressStatusCallback;
        cameraGeneratedNewMediaFileCallback_ = cameraGeneratedNewMediaFileCallback;
    }

    public void initializeMissionPriorToTakeoff(I_CompletionCallback callback)
    {
        callback_ = callback;

        I_MissionManager missionManager = missionManagerSource_.getMissionManager();
        missionManager.setMissionExecutionFinishedCallback(missionExecutionCompletionCallback_);
        missionManager.setMissionProgressStatusCallback(missionProgressStatusCallback_);

        I_Camera camera = cameraSource_.getCamera();
        camera.setCameraGeneratedNewMediaFileCallback(cameraGeneratedNewMediaFileCallback_);

        expectedCallback_ = MissionInitializer.ExpectedCallback.SET_HOME_LOCATION;
        flightControllerSource_.getFlightController().setHomeLocationUsingAircraftCurrentLocation(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case SET_HOME_LOCATION:
                    expectedCallback_ = MissionInitializer.ExpectedCallback.INITIALIZE_IMAGE_TRANSFER;
                    imageTransferModuleInitializer_.initializeImageTransferModulePriorToFlight(this);
                    break;
                case INITIALIZE_IMAGE_TRANSFER:
                    expectedCallback_ = MissionInitializer.ExpectedCallback.CHANGE_CAMERA_MODE;
                    cameraModeChanger_.changeToShootPhotoMode(this);
                    break;
                case CHANGE_CAMERA_MODE:
                    expectedCallback_ = MissionInitializer.ExpectedCallback.SET_PHOTO_FILE_FORMAT;
                    cameraSource_.getCamera().setPhotoFileFormat(
                            DJICameraSettingsDef.CameraPhotoFileFormat.RAW,
                            this);
                    break;
                case SET_PHOTO_FILE_FORMAT:
                    callback_.onResult(null);
                    break;
                default:
                    break;
            }
        }
        else
        {
            callback_.onResult(error);
        }
    }
}
