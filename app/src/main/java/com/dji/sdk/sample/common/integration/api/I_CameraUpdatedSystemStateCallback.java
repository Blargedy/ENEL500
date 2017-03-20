package com.dji.sdk.sample.common.integration.api;

import dji.common.camera.CameraSystemState;
import dji.sdk.camera.DJICamera;

/**
 * Created by Julia on 2017-03-20.
 */

public interface I_CameraUpdatedSystemStateCallback extends DJICamera.CameraUpdatedSystemStateCallback
{
    void onResult(CameraSystemState cameraSystemState);
}
