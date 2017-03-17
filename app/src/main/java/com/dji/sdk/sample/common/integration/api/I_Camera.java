package com.dji.sdk.sample.common.integration.api;

import com.dji.sdk.sample.common.mission.api.I_CameraGeneratedNewMediaFileCallback;

/**
 * Created by Julia on 2017-03-12.
 */

public interface I_Camera
{
    enum CameraMode { SHOOT_PHOTO }

    void setCameraMode(CameraMode mode, I_CompletionCallback callback);
    void setCameraGeneratedNewMediaFileCallback(I_CameraGeneratedNewMediaFileCallback callback);
    void shootSinglePhoto(I_CompletionCallback callback);
}
