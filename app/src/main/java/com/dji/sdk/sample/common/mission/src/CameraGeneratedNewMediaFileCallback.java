package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadQueuer;
import com.dji.sdk.sample.common.mission.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionPeriodicImageTransferInitiator;

import dji.sdk.camera.DJIMedia;

import static dji.sdk.camera.DJIMedia.MediaType.JPG;

/**
 * Created by Julia on 2017-03-16.
 */

public class CameraGeneratedNewMediaFileCallback implements I_CameraGeneratedNewMediaFileCallback
{
    private static final int IMAGE_TRANSFER_DELAY = 5;
    private int imageCount_;

    private I_MissionPeriodicImageTransferInitiator imageTransferInitiator_;
    private I_DroneImageDownloadQueuer imageDownloadQueuer_;

    public CameraGeneratedNewMediaFileCallback(
            I_DroneImageDownloadQueuer imageDownloadQueuer,
            I_MissionPeriodicImageTransferInitiator imageTransferInitiator)
    {
        imageDownloadQueuer_ = imageDownloadQueuer;
        imageTransferInitiator_ = imageTransferInitiator;

        imageCount_ = 0;
    }

    @Override
    public void onResult(DJIMedia media)
    {
        if (media.getMediaType() == JPG)
        {
            imageDownloadQueuer_.addImageToDownloadQueue(media);
            imageCount_++;
        }

        if (imageCount_ % IMAGE_TRANSFER_DELAY == 0)
        {
            imageTransferInitiator_.initiateImageTransfer();
        }
    }
}
