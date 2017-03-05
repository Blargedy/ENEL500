package com.dji.sdk.sample.common.imageTransfer.api;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_DroneToAndroidImageDownloader
{
    void downloadImagesFromDrone(
            ArrayList<DJIMedia> imagesToDownload,
            I_ImageTransferCompletionCallback callback);
}
