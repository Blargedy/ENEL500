package com.dji.sdk.sample.common.droneState.src;

import com.dji.sdk.sample.common.integration.api.I_CameraUpdatedSystemStateCallback;

import dji.common.camera.CameraSystemState;

/**
 * Created by Julia on 2017-03-20.
 */

public class CameraState implements I_CameraUpdatedSystemStateCallback
{
    private CameraSystemState cameraSystemState_;

    @Override
    public void onResult(CameraSystemState cameraSystemState)
    {
        setCameraSystemState(cameraSystemState);
    }

    synchronized private void setCameraSystemState(CameraSystemState cameraSystemState)
    {
        cameraSystemState_ = cameraSystemState;
    }

    synchronized public CameraSystemState cameraSystemState()
    {
        return cameraSystemState_;
    }
}
