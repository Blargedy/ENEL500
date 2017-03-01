package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferer;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.missionmanager.DJIMissionManager;

/**
 * Created by eric7 on 2017-02-21.
 */

public class StepCompletionCallback implements I_CompletionCallback{
    private I_MissionController controller_;
    private I_ApplicationContextManager contextManager_;
    private DJIMissionManager missionManager_;
    private I_ImageTransferer imageTransferer_;
    private int waypointCounter_;

    public StepCompletionCallback(
            I_MissionController controller,
            I_ApplicationContextManager contextManager)
    {
        controller_ = controller;
        contextManager_ = contextManager;
        waypointCounter_ = 0;
    }

    public void onResult(DJIError error) {
        if (error == null) {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Successfully reached Waypoint "+Integer.toString(waypointCounter_), Toast.LENGTH_SHORT).show();
            controller_.handleWaypointReached(waypointCounter_);
            waypointCounter_++;

        } else {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "could not reach waypoint "+Integer.toString(waypointCounter_)+". "+error.getDescription(), Toast.LENGTH_LONG).show();
        }
    }

}

