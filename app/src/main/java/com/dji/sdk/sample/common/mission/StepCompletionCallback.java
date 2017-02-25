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
    private I_ApplicationContextManager contextManager_;
    private DJIMissionManager missionManager_;
    private I_ImageTransferer imageTransferer_;

    public StepCompletionCallback(I_ApplicationContextManager contextManager, DJIMissionManager missionManager, I_ImageTransferer imageTransferer) {
        contextManager_ = contextManager;
        missionManager_ = missionManager;
        imageTransferer_ = imageTransferer;
    }

    public void onResult(DJIError error) {
        if (error == null) {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Successfully reached Waypoint", Toast.LENGTH_SHORT).show();
            missionManager_.pauseMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if(djiError == null){
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Paused mission to transfer photos", Toast.LENGTH_SHORT).show();
                        imageTransferer_.transferNewImagesFromDrone();
                    }
                    else{
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Could not pause mission to transfer photos: " + djiError.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            missionManager_.resumeMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if(djiError == null){
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "resumed mission ", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Could not resume mission: " + djiError.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "could not reach waypoint", Toast.LENGTH_LONG).show();
        }
    }

}

