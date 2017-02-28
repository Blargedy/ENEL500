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
    private int counter_;

    public StepCompletionCallback(
            I_MissionController controller,
            I_ApplicationContextManager contextManager)
    {
        controller_ = controller;
        contextManager_ = contextManager;
        counter_ = 0;
    }

    public void onResult(DJIError error) {
        if (error == null) {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Successfully reached Waypoint", Toast.LENGTH_SHORT).show();

//            Toast.makeText(contextManager_.getApplicationContext(),
//                    "Mission pausable: " + Boolean.toString(missionManager_.getCurrentExecutingMission().isPausable()), Toast.LENGTH_SHORT).show();


            //should handle all below code
            controller_.handleWaypointReached();

//            sendUpdate();
//
//            //pause the mission every 5 waypoints
//            counter_++;
//            if(counter_%5 ==0)
//            {
//                pauseMission();
//            }


        } else {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "could not reach waypoint", Toast.LENGTH_LONG).show();
        }
    }

//    private void sendUpdate()
//    {
//
//    }
//
//    private void pauseMission()
//    {
//        missionManager_.pauseMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
//            @Override
//            public void onResult(DJIError djiError) {
//                if(djiError == null){
//                    Toast.makeText(contextManager_.getApplicationContext(),
//                            "Paused mission to transfer photos", Toast.LENGTH_LONG).show();
//                    imageTransferer_.transferNewImagesFromDrone();
//                }
//                else{
//                    Toast.makeText(contextManager_.getApplicationContext(),
//                            "Could not pause mission to transfer photos: " + djiError.getDescription(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        missionManager_.resumeMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
//            @Override
//            public void onResult(DJIError djiError) {
//                if(djiError == null){
//                    Toast.makeText(contextManager_.getApplicationContext(),
//                            "resumed mission ", Toast.LENGTH_SHORT).show();
//                }
//
//                else{
//                    Toast.makeText(contextManager_.getApplicationContext(),
//                            "Could not resume mission: " + djiError.getDescription(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

}

