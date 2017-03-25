package com.dji.sdk.sample.common.entity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.utility.ApplicationSettingsManager;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.values.MissionBoundary;

/**
 * Created by Julia on 2017-02-04.
 */

public class InitialMissionModel
{
    private MissionBoundary missionBoundary_;
    private float altitude_;
    private double minimumPercentImageOverlap_;
    private double minimumPercentSwathOverlap_;

    private BroadcastReceiver receiver_;
    private ApplicationSettingsManager applicationSettingsManager_;

    public InitialMissionModel(
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

    private void retrieveSettingsFromSettingsManager()
    {
        altitude_ = applicationSettingsManager_.getAltitudeFromSettings();
        minimumPercentImageOverlap_ = applicationSettingsManager_.getMinimumPercentImageOverlapFromSettings();
        minimumPercentSwathOverlap_ = applicationSettingsManager_.getMinimumPercentSwathOverlapFromSettings();
    }
}
