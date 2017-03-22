package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_CameraUpdatedSystemStateCallback;
import com.dji.sdk.sample.common.mission.api.I_CameraInitializer;
import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;

import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-18.
 */

public class CameraInitializer implements
        I_CameraInitializer,
        I_CompletionCallback
{
    private static final String TAG = "HydraCameraInitializer";

    private enum ExpectedCallback { SET_CAMERA_MODE, SET_FILE_FORMAT, SET_EXPOSURE_MODE }

    private ExpectedCallback expectedCallback_;
    private I_CompletionCallback callback_;

    private I_CameraSource cameraSource_;
    private I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback_;
    private I_CameraUpdatedSystemStateCallback cameraUpdatedSystemStateCallback_;

    public CameraInitializer(
            I_CameraSource cameraSource,
            I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback,
            I_CameraUpdatedSystemStateCallback cameraUpdatedSystemStateCallback)
    {
        cameraSource_ = cameraSource;
        cameraGeneratedNewMediaFileCallback_ = cameraGeneratedNewMediaFileCallback;
        cameraUpdatedSystemStateCallback_ = cameraUpdatedSystemStateCallback;
    }

    @Override
    public void initializeCameraForMission(I_CompletionCallback callback)
    {
        callback_ = callback;

        I_Camera camera = cameraSource_.getCamera();
        camera.setCameraGeneratedNewMediaFileCallback(cameraGeneratedNewMediaFileCallback_);
        camera.setDJICameraUpdatedSystemStateCallback(cameraUpdatedSystemStateCallback_);

        expectedCallback_ = ExpectedCallback.SET_CAMERA_MODE;
        camera.setCameraMode(I_Camera.CameraMode.SHOOT_PHOTO, this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case SET_CAMERA_MODE:
                    expectedCallback_ = ExpectedCallback.SET_FILE_FORMAT;
                    cameraSource_.getCamera().setPhotoFileFormat(
                            DJICameraSettingsDef.CameraPhotoFileFormat.JPEG,
                            this);
                    break;

                case SET_FILE_FORMAT:
                    expectedCallback_ = ExpectedCallback.SET_EXPOSURE_MODE;
                    cameraSource_.getCamera().setExposureModeToAutomatic(this);
                    break;

                case SET_EXPOSURE_MODE:
                    callback(null);
                    break;
            }
        }
        else
        {
            Log.e(TAG, error.toString());
            callback(error);
        }
    }

    private void callback(DJIError error)
    {
        if (callback_!= null)
        {
            callback_.onResult(error);
        }
    }
}