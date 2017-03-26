package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadQueuer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionPeriodicImageTransferInitiator;

import dji.sdk.camera.DJIMedia;

import static com.dji.sdk.sample.common.utility.MissionParameters.WAYPOINTS_PER_MISSION;
import static dji.sdk.camera.DJIMedia.MediaType.JPG;

/**
 * Created by Julia on 2017-03-16.
 */

public class CameraGeneratedNewMediaFileCallback implements I_CameraGeneratedNewMediaFileCallback
{
    private I_DroneImageDownloadQueuer imageDownloadQueuer_;
    private I_ImageTransferer imageTransferer_;

    public CameraGeneratedNewMediaFileCallback(
            I_DroneImageDownloadQueuer imageDownloadQueuer,
            I_ImageTransferer imageTransferer)
    {
        imageDownloadQueuer_ = imageDownloadQueuer;
        imageTransferer_ = imageTransferer;
    }

    @Override
    public void onResult(DJIMedia media)
    {
        if (media.getMediaType() == JPG)
        {
            imageDownloadQueuer_.addImageToDownloadQueue(media);
        }

        if (imageDownloadQueuer_.imageCount() % WAYPOINTS_PER_MISSION == 0)
        {
            imageTransferer_.transferNewImagesFromDrone(null);
        }
    }
}
