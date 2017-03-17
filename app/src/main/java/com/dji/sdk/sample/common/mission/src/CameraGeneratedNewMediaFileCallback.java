package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.mission.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionPeriodicImageTransferInitiator;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-16.
 */

public class CameraGeneratedNewMediaFileCallback implements I_CameraGeneratedNewMediaFileCallback
{
    private int imageCount_;
    private static final int IMAGE_TRANSFER_DELAY = 5;
    private I_MissionPeriodicImageTransferInitiator imageTransferInitiator_;

    public CameraGeneratedNewMediaFileCallback(
            I_MissionPeriodicImageTransferInitiator imageTransferInitiator)
    {
        imageTransferInitiator_ = imageTransferInitiator;
        imageCount_ = 0;
    }

    @Override
    public void onResult(DJIMedia media)
    {
        imageCount_++;
        if (imageCount_ % IMAGE_TRANSFER_DELAY == 0)
        {
            imageTransferInitiator_.initiateImageTransfer();
        }
    }
}
