package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadQueuer;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadSelector;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-17.
 */

public class DroneImageDownloadQueuer implements
        I_DroneImageDownloadQueuer,
        I_DroneImageDownloadSelector
{
    private ArrayList<DJIMedia> imagesToDownload_;

    public DroneImageDownloadQueuer()
    {
        imagesToDownload_ = new ArrayList<>();
    }

    @Override
    public void addImageToDownloadQueue(DJIMedia image)
    {
        imagesToDownload_.add(image);
    }

    @Override
    public void clearQueue()
    {
        imagesToDownload_.clear();
    }

    @Override
    public ArrayList<DJIMedia> determineImagesForDownloadFromMediaList(ArrayList<DJIMedia> currentMediaList)
    {
        return imagesToDownload_;
    }
}
