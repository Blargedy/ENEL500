package com.dji.sdk.sample.common.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-03-25.
 */

public class ApplicationSettingsManager
{
    Context context_;

    public ApplicationSettingsManager(
            Context context)
    {
        context_ = context;
    }

    public String getPcIpAddressFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getString(ApplicationSettingsKeys.PC_IP_ADDRESS, "0.0.0.0");
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
                .getFloat(ApplicationSettingsKeys.MINIMUM_PERCENT_IMAGE_OVERLAP, 0.88f);
    }

    public float getMinimumPercentSwathOverlapFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getFloat(ApplicationSettingsKeys.MINIMUM_PERCENT_SWATH_OVERLAP, 0.6f);
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

    public String getImageTypeFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getString(
                        ApplicationSettingsKeys.IMAGE_TYPE,
                        DJICameraSettingsDef.CameraPhotoFileFormat.JPEG.name());
    }

    public float getWaypointSizeFromSettings()
    {
        return PreferenceManager.getDefaultSharedPreferences(context_)
                .getFloat(ApplicationSettingsKeys.WAYPOINT_SIZE, 1.0f);
    }

    public void savePcIpAddress(String pcIpAddress)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putString(ApplicationSettingsKeys.PC_IP_ADDRESS, pcIpAddress);
        editor.commit();
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

    public void saveImageTypeToSettings(DJICameraSettingsDef.CameraPhotoFileFormat imageType)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putString(ApplicationSettingsKeys.IMAGE_TYPE, imageType.name());
        editor.commit();
    }

    public void saveWaypointSizeToSettings(float waypointSize)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context_).edit();
        editor.putFloat(ApplicationSettingsKeys.WAYPOINT_SIZE, waypointSize);
        editor.commit();
    }
}
