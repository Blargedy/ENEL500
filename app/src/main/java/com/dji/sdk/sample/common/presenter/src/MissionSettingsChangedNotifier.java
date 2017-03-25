package com.dji.sdk.sample.common.presenter.src;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;

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

    public void notifyMissionSettingsChanged()
    {
        Intent intent = new Intent(BroadcastIntentNames.MISSION_SETTINGS_CHANGED);
        LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);
    }
}
