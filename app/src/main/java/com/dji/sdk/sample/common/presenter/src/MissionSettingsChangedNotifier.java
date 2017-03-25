package com.dji.sdk.sample.common.presenter.src;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Julia on 2017-03-23.
 */

public class MissionSettingsChangedNotifier
{
    private Context context_;

    public MissionSettingsChangedNotifier(
            Context context)
    {
        context_ = context;
    }

    public void notifySettingsChanged(
            float altitude,
            double minimumPercentImageOverlap,
            double minimumPercentSwathOverlap,
            boolean isInAutomaticMode,
            DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed,
            DJICameraSettingsDef.CameraISO cameraISO)
    {
        Intent intent = new Intent(BroadcastIntentNames.MISSION_SETTINGS_CHANGED);

        intent.putExtra(IntentExtraKeys.ALTITUDE, altitude);
        intent.putExtra(IntentExtraKeys.MINIMUM_PERCENT_IMAGE_OVERLAP, minimumPercentImageOverlap/100.0);
        intent.putExtra(IntentExtraKeys.MINIMUM_PERCENT_SWATH_OVERLAP, minimumPercentSwathOverlap/100.0);
        intent.putExtra(IntentExtraKeys.IS_CAMERA_AUTOMATIC_MODE, isInAutomaticMode);
        intent.putExtra(IntentExtraKeys.CAMERA_ISO, cameraISO.value());
        intent.putExtra(IntentExtraKeys.CAMERA_SHUTTER_SPEED, cameraShutterSpeed.value());

        LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);
    }
}
