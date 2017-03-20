package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferPathsSource;
import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MediaDownloadListener;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.I_WaypointReachedNotifier;

import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-20.
 */

public class InvestigativeWaypointReachedHandler implements
        I_WaypointReachedNotifier,
        I_CompletionCallback,
        I_CameraGeneratedNewMediaFileCallback,
        I_MediaDownloadListener
{
    private static final String TAG = "HydraInvestigativeWaypointReachedHandler";

    private enum ExpectedCallback {PAUSE_MISSION, SHOOT_PHOTO, CHANGE_TO_DOWNLOAD_MODE, CHANGE_TO_SHOOTPHOTO_MODE, RESUME_MISSION}
    private ExpectedCallback expectedCallback_;

    private I_MissionManagerSource missionManagerSource_;
    private I_CameraSource cameraSource_;
    private I_ImageTransferPathsSource pathsSource_;

    private InvestigativeCameraSystemState cameraSystemState_;
    private DJIMedia mediaToDownload_;

    public InvestigativeWaypointReachedHandler(
            I_MissionManagerSource missionManagerSource,
            I_CameraSource cameraSource,
            InvestigativeCameraSystemState cameraSystemState,
            I_ImageTransferPathsSource pathsSource)
    {
        missionManagerSource_ = missionManagerSource;
        cameraSource_ = cameraSource;
        pathsSource_ = pathsSource;
        cameraSystemState_ = cameraSystemState;
    }

    @Override
    public void notifyWaypointAtIndexHasBeenReached(int waypointIndex)
    {
        Log.d(TAG, "Reached waypoint at index " + waypointIndex + ", pausing mission");
        expectedCallback_ = ExpectedCallback.PAUSE_MISSION;
        missionManagerSource_.getMissionManager().pauseMissionExecution(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case PAUSE_MISSION:
                    Log.d(TAG, "Finished pausing mission, shooting photo");
                    expectedCallback_ = ExpectedCallback.SHOOT_PHOTO;
                    cameraSource_.getCamera().shootSinglePhoto(this);
                    break;

                case SHOOT_PHOTO:
                    Log.d(TAG, "Shoot photo command sent, waiting for photo to be stored");
                    break;

                case CHANGE_TO_DOWNLOAD_MODE:
                    Log.d(TAG, "Change camera mode sent, waiting for camera mode to be download");
                    while (cameraSystemState_.cameraSystemState().getCameraMode() != DJICameraSettingsDef.CameraMode.MediaDownload)
                    {
                    }

                    Log.d(TAG, "Camera mode changed, downloading photo");
                    mediaToDownload_.fetchMediaData(pathsSource_.androidDeviceImagePath(), null, this);
                    break;

                case CHANGE_TO_SHOOTPHOTO_MODE:
                    Log.d(TAG, "Change camera mode sent, waiting for camera mode to be shoot photo");
                    while (cameraSystemState_.cameraSystemState().getCameraMode() != DJICameraSettingsDef.CameraMode.ShootPhoto)
                    {
                    }

                    Log.d(TAG, "Camera mode changed, resuming mission");
                    expectedCallback_ = ExpectedCallback.RESUME_MISSION;
                    missionManagerSource_.getMissionManager().resumeMissionExecution(this);
                    break;

                case RESUME_MISSION:
                    Log.d(TAG, "Mission resumed");

                default:
                    break;
            }
        }
        else
        {
            Log.d(TAG, "Failed on " + expectedCallback_.name() + ": error.getDescription()");
            expectedCallback_ = ExpectedCallback.RESUME_MISSION;
            missionManagerSource_.getMissionManager().resumeMissionExecution(this);
        }
    }

    @Override
    public void onResult(DJIMedia media)
    {
        if (media.getMediaType() == DJIMedia.MediaType.JPG)
        {
            Log.d(TAG, "Photo stored");

            mediaToDownload_ = media;

            Log.d(TAG, "Waiting for camera state to be ready for download");
            while (cameraSystemState_.cameraSystemState().isStoringPhoto() ||
                    cameraSystemState_.cameraSystemState().isShootingSinglePhoto()) {
            }

            Log.d(TAG, "Changing camera mode to MediaDownloadMode");
            expectedCallback_ = ExpectedCallback.CHANGE_TO_DOWNLOAD_MODE;
            cameraSource_.getCamera().setCameraMode(I_Camera.CameraMode.MEDIA_DOWNLOAD, this);
        }
    }

    @Override
    public void onStart()
    {
        Log.d(TAG, "Starting download");
    }

    @Override
    public void onRateUpdate(long total, long current, long persize) {

    }

    @Override
    public void onProgress(long total, long current) {

    }

    @Override
    public void onSuccess(String path)
    {
        Log.d(TAG, "Finished downloading photo successfully");

        Log.d(TAG, "Changing camera mode to ShootPhotoMode");
        expectedCallback_ = ExpectedCallback.CHANGE_TO_SHOOTPHOTO_MODE;
        cameraSource_.getCamera().setCameraMode(I_Camera.CameraMode.SHOOT_PHOTO, this);
    }

    @Override
    public void onFailure(DJIError error)
    {
        Log.d(TAG, "Failed to download photo successfully: " + error.getDescription());
        expectedCallback_ = ExpectedCallback.RESUME_MISSION;
        missionManagerSource_.getMissionManager().resumeMissionExecution(this);
    }
}
