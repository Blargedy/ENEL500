package com.dji.sdk.sample.common.mission.src;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferCompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.mission.api.I_WaypointReachedHandler;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-05.
 */

public class WaypointReachedHandler implements
        I_WaypointReachedHandler,
        I_CompletionCallback,
        I_ImageTransferCompletionCallback
{
    private static final String TAG = "WaypointReachedHandler";

    private Context context_;
    private I_MissionController missionController_;
    private I_ImageTransferer imageTransferer_;

    public WaypointReachedHandler(
            Context context,
            I_MissionController missionController,
            I_ImageTransferer imageTransferer)
    {
        context_ = context;
        missionController_ = missionController;
        imageTransferer_ = imageTransferer;
    }

    @Override
    public void handleWaypointReached(int waypointIndex)
    {
        sendIntentNotifyingWaypointHasBeenReached(waypointIndex);
        completeImageTransferIfNecessary(waypointIndex);
    }

    void sendIntentNotifyingWaypointHasBeenReached(int waypointIndex)
    {
        Intent intent = new Intent(BroadcastIntentNames.WAYPOINT_REACHED);
        intent.putExtra(IntentExtraKeys.WAYPOINT_INDEX, waypointIndex);
        LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);
    }

    void completeImageTransferIfNecessary(int waypointIndex)
    {
        if(waypointIndex %5 == 0)
        {
            missionController_.pauseMission(this);
        }
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            imageTransferer_.transferNewImagesFromDrone(this);
        }
        else
        {
            Log.e(TAG, "Unable to pause mission : " + error.getDescription());
        }
    }

    @Override
    public void onImageTransferCompletion()
    {
        missionController_.resumeMission(null);
    }
}
