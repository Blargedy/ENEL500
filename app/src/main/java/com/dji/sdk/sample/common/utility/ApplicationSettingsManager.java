package com.dji.sdk.sample.common.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-03-25.
 */

public class ApplicationSettingsManager
{
    Context context_;
    Activity activity_;

    public ApplicationSettingsManager(
            Context context)
    {
        context_ = context;
    }

    public float getAltitudeFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getFloat(ApplicationSettingsKeys.ALTITUDE, 20.0f);
    }

    public float getMissionSpeedFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getFloat(ApplicationSettingsKeys.MISSION_SPEED, 5.0f);
    }

    public float getMinimumPercentImageOverlapFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getFloat(ApplicationSettingsKeys.MINIMUM_PERCENT_IMAGE_OVERLAP, 0.8f);
    }

    public float getMinimumPercentSwathOverlapFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getFloat(ApplicationSettingsKeys.MINIMUM_PERCENT_SWATH_OVERLAP, 0.5f);
    }

    public boolean getIsCameraInAutomaticModeFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getBoolean(ApplicationSettingsKeys.IS_CAMERA_AUTOMATIC_MODE, true);
    }

    public String getCameraIsoFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getString(
                    ApplicationSettingsKeys.CAMERA_ISO,
                    DJICameraSettingsDef.CameraISO.ISO_100.name());
    }

    public String getCameraShutterSpeedFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getString(
                    ApplicationSettingsKeys.CAMERA_SHUTTER_SPEED,
                    DJICameraSettingsDef.CameraShutterSpeed.ShutterSpeed1_1000.name());
    }

    public void saveAltitudeToSettings(float altitude)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putFloat(ApplicationSettingsKeys.ALTITUDE, altitude);
        editor.commit();
    }

    public void saveMissionSpeedToSettings(float missionSpeed)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putFloat(ApplicationSettingsKeys.MISSION_SPEED, missionSpeed);
        editor.commit();
    }

    public void saveMinimumPercentImageOverlapToSettings(float minimumPercentImageOverlap)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putFloat(
                ApplicationSettingsKeys.MINIMUM_PERCENT_IMAGE_OVERLAP, minimumPercentImageOverlap);
        editor.commit();
    }

    public void saveMinimumPercentSwathOverlapToSettings(float minimumPercentSwathOverlap)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putFloat(
                ApplicationSettingsKeys.MINIMUM_PERCENT_SWATH_OVERLAP, minimumPercentSwathOverlap);
        editor.commit();
    }

    public void saveIsCameraInAutomaticModeToSettings(boolean isCameraInAutomaticMode)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putBoolean(ApplicationSettingsKeys.IS_CAMERA_AUTOMATIC_MODE, isCameraInAutomaticMode);
        editor.commit();
    }

    public void saveCameraIsoToSettings(DJICameraSettingsDef.CameraISO cameraISO)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putString(ApplicationSettingsKeys.CAMERA_ISO, cameraISO.name());
        editor.commit();
    }

    public void saveCameraShutterSpeedToSettings(DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putString(ApplicationSettingsKeys.CAMERA_SHUTTER_SPEED, cameraShutterSpeed.name());
        editor.commit();
    }
}
