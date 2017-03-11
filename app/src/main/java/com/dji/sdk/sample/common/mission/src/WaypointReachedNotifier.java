package com.dji.sdk.sample.common.mission.src;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.mission.api.I_WaypointReachedNotifier;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;

/**
 * Created by Julia on 2017-03-11.
 */

public class WaypointReachedNotifier implements I_WaypointReachedNotifier
{
    private Context context_;

    public WaypointReachedNotifier(
            Context context)
    {
        context_ = context;
    }

    @Override
    public void notifyWaypointAtIndexHasBeenReached(int waypointIndex)
    {
        Intent intent = new Intent(BroadcastIntentNames.WAYPOINT_REACHED);
        intent.putExtra(IntentExtraKeys.WAYPOINT_INDEX, waypointIndex);
        LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);
    }
}
