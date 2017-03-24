package com.dji.sdk.sample.common.entity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-03-23.
 */

public class CameraSettingsEntity
{
    private static final String TAG = "HydraCameraSettingsEntity";

    private boolean isInAutomaticMode_;
    private DJICameraSettingsDef.CameraISO cameraISO_;
    private DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed_;

    private BroadcastReceiver receiver_;

    public CameraSettingsEntity(
            Context context)
    {
        isInAutomaticMode_ = true;
        registerMissionSettingsChangedReceiver(context);
    }

    private void registerMissionSettingsChangedReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.MISSION_SETTINGS_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                cameraSettingsChanged(intent);
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

    private void cameraSettingsChanged(Intent intent)
    {
        isInAutomaticMode_ = intent.getBooleanExtra(
                IntentExtraKeys.IS_CAMERA_AUTOMATIC_MODE, true);
        cameraISO_ = DJICameraSettingsDef.CameraISO.find(
                intent.getIntExtra(IntentExtraKeys.CAMERA_ISO, 0));
        cameraShutterSpeed_ = DJICameraSettingsDef.CameraShutterSpeed.find(
                intent.getIntExtra(IntentExtraKeys.CAMERA_SHUTTER_SPEED, 0));

        Log.d(TAG,
                "isInAutomaticMode=" + isInAutomaticMode_ +
                "cameraISO=" + cameraISO_.name()+
                "cameraShutterSpeed=" + cameraShutterSpeed_.name());
    }
}
