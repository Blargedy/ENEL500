package com.dji.sdk.sample.common.integration.api;


/**
 * Created by Julia on 2017-03-12.
 */

public interface I_Camera
{
    enum CameraMode { SHOOT_PHOTO }

    void setCameraMode(CameraMode mode, I_CompletionCallback callback);
}
