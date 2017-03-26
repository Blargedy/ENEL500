package com.dji.sdk.sample.common.imageTransfer.api;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-25.
 */

public class InertDroneImageDownloadQueuer implements I_DroneImageDownloadQueuer
{
    @Override
    public void addImageToDownloadQueue(DJIMedia image)
    {
    }

    @Override
    public ArrayList<DJIMedia> getListOfImagesToDownload()
    {
        return null;
    }

    @Override
    public int imageCount()
    {
        return 0;
    }

    @Override
    public void clearQueue()
    {
    }
}
