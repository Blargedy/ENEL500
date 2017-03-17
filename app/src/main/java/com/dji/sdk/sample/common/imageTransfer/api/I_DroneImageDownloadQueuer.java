package com.dji.sdk.sample.common.imageTransfer.api;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-17.
 */

public interface I_DroneImageDownloadQueuer
{
    void addImageToDownloadQueue(DJIMedia image);
    ArrayList<DJIMedia> getListOfImagesToDownload();
    void clearQueue();
}
