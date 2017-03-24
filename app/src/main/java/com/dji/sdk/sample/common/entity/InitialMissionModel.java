package com.dji.sdk.sample.common.entity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;
import com.dji.sdk.sample.common.values.MissionBoundary;

/**
 * Created by Julia on 2017-02-04.
 */

public class InitialMissionModel
{
    private static final String TAG = "HydraInitialMissionModel";

    private MissionBoundary missionBoundary_;
    private float altitude_ = 20.0f;
    private double minimumPercentImageOverlap_ = 0.80; // number between 0 and 1 e.g. 0.80
    private double minimumPercentSwathOverlap_ = 0.50; // number between 0 and 1 e.g. 0.50

    private BroadcastReceiver receiver_;

    public InitialMissionModel(
            Context context)
    {
        registerMissionSettingsChangedReceiver(context);
    }

    private void registerMissionSettingsChangedReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.MISSION_SETTINGS_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                missionSettingsChanged(intent);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver_, filter);
    }

    public MissionBoundary missionBoundary()
    {
        return missionBoundary_;
    }

    public void setMissionBoundary(MissionBoundary missionBoundary)
    {
        missionBoundary_ = missionBoundary;
    }

    public float altitude()
    {
        return altitude_;
    }

    public double minimumPercentImageOverlap()
    {
        return minimumPercentImageOverlap_;
    }

    public double minimumPercentSwathOverlap()
    {
        return minimumPercentSwathOverlap_;
    }

    private void missionSettingsChanged(Intent intent)
    {
        altitude_ = intent.getFloatExtra(IntentExtraKeys.ALTITUDE, 20.0f);
        minimumPercentImageOverlap_ = intent.getDoubleExtra(
                IntentExtraKeys.MINIMUM_PERCENT_IMAGE_OVERLAP, 0.80);
        minimumPercentSwathOverlap_ = intent.getDoubleExtra(
                IntentExtraKeys.MINIMUM_PERCENT_SWATH_OVERLAP, 0.50);

        Log.d(TAG,
                "altitude=" + altitude_ +
                "imageOverlap=" + minimumPercentImageOverlap_+
                "swathOverlap=" + minimumPercentSwathOverlap_);
    }
}
