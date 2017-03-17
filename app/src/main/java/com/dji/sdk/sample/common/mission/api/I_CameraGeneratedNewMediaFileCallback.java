package com.dji.sdk.sample.common.mission.api;

import dji.sdk.camera.DJICamera;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-16.
 */

public interface I_CameraGeneratedNewMediaFileCallback extends DJICamera.CameraGeneratedNewMediaFileCallback
{
    void onResult(DJIMedia media);
}
