package com.dji.sdk.sample.common.entity;

import dji.common.camera.DJICameraSettingsDef;

import static dji.common.camera.DJICameraSettingsDef.CameraISO.ISO_1600;

/**
 * Created by Julia on 2017-03-23.
 */

public class CameraSettingsEntity
{
    private boolean isInAutomaticMode_;
    private DJICameraSettingsDef.CameraISO cameraISO_;
    private DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed_;

    public CameraSettingsEntity()
    {
//        isInAutomaticMode_ = true;

        isInAutomaticMode_ = false;
        cameraISO_ = ISO_1600;
        cameraShutterSpeed_ = DJICameraSettingsDef.CameraShutterSpeed.ShutterSpeed1_5;
    }

    public void setIsInAutomaticMode(boolean isInAutomaticMode)
    {
        isInAutomaticMode_ = isInAutomaticMode;
    }

    public void setCameraISO(DJICameraSettingsDef.CameraISO cameraISO)
    {
        cameraISO_ = cameraISO;
    }

    public void setCameraShutterSpeed(DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed)
    {
        cameraShutterSpeed_ = cameraShutterSpeed;
    }

    public boolean isInAutomaticMode()
    {
        return isInAutomaticMode_;
    }

    public DJICameraSettingsDef.CameraISO cameraISO()
    {
        return cameraISO_;
    }

    public DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed()
    {
        return cameraShutterSpeed_;
    }
}
