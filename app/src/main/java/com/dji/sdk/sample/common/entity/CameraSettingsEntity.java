package com.dji.sdk.sample.common.entity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.utility.ApplicationSettingsManager;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-03-23.
 */

public class CameraSettingsEntity
{
    private boolean isInAutomaticMode_;
    private DJICameraSettingsDef.CameraISO cameraISO_;
    private DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed_;
    private DJICameraSettingsDef.CameraPhotoFileFormat imageType_;

    private BroadcastReceiver receiver_;
    private ApplicationSettingsManager applicationSettingsManager_;

    public CameraSettingsEntity(
            Context context,
            ApplicationSettingsManager applicationSettingsManager)
    {
        applicationSettingsManager_ = applicationSettingsManager;
        retrieveSettingsFromSettingsManager();
        registerMissionSettingsChangedReceiver(context);
    }

    private void registerMissionSettingsChangedReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.MISSION_SETTINGS_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                retrieveSettingsFromSettingsManager();
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver_, filter);
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

    public DJICameraSettingsDef.CameraPhotoFileFormat imageType()
    {
        return imageType_;
    }

    private void retrieveSettingsFromSettingsManager()
    {
        isInAutomaticMode_ = applicationSettingsManager_.getIsCameraInAutomaticModeFromSettings();

        cameraISO_ = DJICameraSettingsDef.CameraISO.valueOf(
                applicationSettingsManager_.getCameraIsoFromSettings());

        cameraShutterSpeed_ = DJICameraSettingsDef.CameraShutterSpeed.valueOf(
                applicationSettingsManager_.getCameraShutterSpeedFromSettings());

        imageType_ = DJICameraSettingsDef.CameraPhotoFileFormat.valueOf(
                applicationSettingsManager_.getImageTypeFromSettings());
    }
}
