package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferCompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.mission.api.I_MissionPeriodicImageTransferInitiator;

import dji.common.error.DJIError;
import dji.common.util.LocationUtils;

/**
 * Created by Julia on 2017-03-11.
 */

public class MissionPeriodicImageTransferInitiator implements
        I_MissionPeriodicImageTransferInitiator,
        I_ImageTransferCompletionCallback,
        I_CompletionCallback
{
    private static final String TAG = "MissionPeriodicImageTransferInitiator";

    private enum ExpectedCallback { PAUSE, RESUME }

    private ExpectedCallback expectedCallback_;
    private I_MissionManagerSource missionManagerSource_;
    private I_ImageTransferer imageTransferer_;

    public MissionPeriodicImageTransferInitiator(
            I_MissionManagerSource missionManagerSource,
            I_ImageTransferer imageTransferer)
    {
        missionManagerSource_ = missionManagerSource;
        imageTransferer_ = imageTransferer;
    }

    @Override
    public void initiateImageTransfer()
    {
        expectedCallback_ = ExpectedCallback.PAUSE;
        missionManagerSource_.getMissionManager().pauseMissionExecution(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case PAUSE:
                    imageTransferer_.transferNewImagesFromDrone(this);
                    break;
                case RESUME:
                    break;
                default:
                    break;
            }
        }
        else
        {
            Log.e(TAG, error.getDescription());
        }
    }

    @Override
    public void onImageTransferCompletion()
    {
        expectedCallback_ = ExpectedCallback.RESUME;
        missionManagerSource_.getMissionManager().resumeMissionExecution(this);
    }
}
