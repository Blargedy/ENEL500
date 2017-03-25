package com.dji.sdk.sample.common.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-03-25.
 */

public class ApplicationSettingsManager
{
    Activity activity_;

    public ApplicationSettingsManager(
            Activity activity)
    {
        activity_ = activity;
    }

    public float getAltitudeFromSettings()
    {
        return activity_.getPreferences(Context.MODE_PRIVATE)
                .getFloat(ApplicationSettingsKeys.ALTITUDE, 20.0f);
    }

    public float getMinimumPercentImageOverlapFromSettings()
    {
        return activity_.getPreferences(Context.MODE_PRIVATE)
                .getFloat(ApplicationSettingsKeys.MINIMUM_PERCENT_IMAGE_OVERLAP, 0.8f);
    }

    public float getMinimumPercentSwathOverlapFromSettings()
    {
        return activity_.getPreferences(Context.MODE_PRIVATE)
                .getFloat(ApplicationSettingsKeys.MINIMUM_PERCENT_SWATH_OVERLAP, 0.5f);
    }

    public boolean getIsCameraInAutomaticModeFromSettings()
    {
        return activity_.getPreferences(Context.MODE_PRIVATE)
                .getBoolean(ApplicationSettingsKeys.IS_CAMERA_AUTOMATIC_MODE, true);
    }

    public String getCameraIsoFromSettings()
    {
        return activity_.getPreferences(Context.MODE_PRIVATE).getString(
                ApplicationSettingsKeys.CAMERA_ISO,
                DJICameraSettingsDef.CameraISO.ISO_100.name());
    }

    public String getCameraShutterSpeedFromSettings()
    {
        return activity_.getPreferences(Context.MODE_PRIVATE).getString(
                ApplicationSettingsKeys.CAMERA_SHUTTER_SPEED,
                DJICameraSettingsDef.CameraShutterSpeed.ShutterSpeed1_1000.name());
    }

    public void saveAltitudeToSettings(float altitude)
    {
        SharedPreferences.Editor editor = activity_.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putFloat(ApplicationSettingsKeys.ALTITUDE, altitude);
        editor.commit();
    }

    public void saveMinimumPercentImageOverlapToSettings(float minimumPercentImageOverlap)
    {
        SharedPreferences.Editor editor = activity_.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putFloat(
                ApplicationSettingsKeys.MINIMUM_PERCENT_IMAGE_OVERLAP, minimumPercentImageOverlap);
        editor.commit();
    }

    public void saveMinimumPercentSwathOverlapToSettings(float minimumPercentSwathOverlap)
    {
        SharedPreferences.Editor editor = activity_.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putFloat(
                ApplicationSettingsKeys.MINIMUM_PERCENT_SWATH_OVERLAP, minimumPercentSwathOverlap);
        editor.commit();
    }

    public void saveIsCameraInAutomaticModeToSettings(boolean isCameraInAutomaticMode)
    {
        SharedPreferences.Editor editor = activity_.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putBoolean(ApplicationSettingsKeys.IS_CAMERA_AUTOMATIC_MODE, isCameraInAutomaticMode);
        editor.commit();
    }

    public void saveCamerIsoToSettings(DJICameraSettingsDef.CameraISO cameraISO)
    {
        SharedPreferences.Editor editor = activity_.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(ApplicationSettingsKeys.CAMERA_ISO, cameraISO.name());
        editor.commit();
    }

    public void saveCameraShutterSpeedToSettings(DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed)
    {
        SharedPreferences.Editor editor = activity_.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(ApplicationSettingsKeys.CAMERA_SHUTTER_SPEED, cameraShutterSpeed.name());
        editor.commit();
    }
}
