package com.dji.sdk.sample.common.integration.api;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-03-12.
 */

public interface I_Camera
{
    enum CameraMode { SHOOT_PHOTO, MEDIA_DOWNLOAD }

    void setCameraMode(CameraMode mode, I_CompletionCallback callback);
    void setCameraGeneratedNewMediaFileCallback(I_CameraGeneratedNewMediaFileCallback callback);
    void setPhotoFileFormat(
            DJICameraSettingsDef.CameraPhotoFileFormat  photoFileFormat,
            I_CompletionCallback callback);
    void setExposureMode(DJICameraSettingsDef.CameraExposureMode mode, I_CompletionCallback callback);
    void setISO(DJICameraSettingsDef.CameraISO isoType, I_CompletionCallback callback);
    void setShutterSpeed (DJICameraSettingsDef.CameraShutterSpeed shutterSpeed, I_CompletionCallback callback);
    void setDJICameraUpdatedSystemStateCallback(I_CameraUpdatedSystemStateCallback callback);

    void shootSinglePhoto(I_CompletionCallback callback);
}
