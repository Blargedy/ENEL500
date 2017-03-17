package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_CameraModeChanger;
import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MediaManager;
import com.dji.sdk.sample.common.integration.api.I_MediaManagerSource;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-02-12.
 */

public class CameraModeChanger implements I_CameraModeChanger
{
    private I_MediaManagerSource mediaManagerSource_;
    private I_CameraSource cameraSource_;

    public CameraModeChanger(
            I_MediaManagerSource mediaManagerSource,
            I_CameraSource cameraSource)
    {
        mediaManagerSource_ = mediaManagerSource;
        cameraSource_ = cameraSource;
    }

    @Override
    public void changeToMediaDownloadMode(I_CompletionCallback callback)
    {
        I_Camera camera = cameraSource_.getCamera();
        camera.setCameraMode(I_Camera.CameraMode.MEDIA_DOWNLOAD, callback);
}

    public void changeToShootPhotoMode(I_CompletionCallback callback)
    {
        I_Camera camera = cameraSource_.getCamera();
        camera.setCameraMode(I_Camera.CameraMode.SHOOT_PHOTO, callback);
    }
}
