package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import dji.common.camera.DJICameraSettingsDef;
import dji.sdk.camera.DJICamera;

/**
 * Created by Julia on 2017-03-12.
 */

public class Camera implements I_Camera
{
    private DJICamera camera_;

    public Camera(
            DJICamera camera)
    {
        camera_ = camera;
    }

    @Override
    public void setCameraMode(CameraMode mode, I_CompletionCallback callback)
    {
        switch (mode)
        {
            case SHOOT_PHOTO:
                camera_.setCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto, callback);
                break;
            default:
                break;
        }
    }
}
