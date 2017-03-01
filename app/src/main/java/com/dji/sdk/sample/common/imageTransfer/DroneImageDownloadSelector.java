package com.dji.sdk.sample.common.imageTransfer;


import java.util.ArrayList;
import java.util.HashSet;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-17.
 */

public class DroneImageDownloadSelector implements
        I_DroneImageDownloadSelector,
        I_DroneMediaListInitializer
{
    private HashSet<String> pastMediaFileNames_;

    public DroneImageDownloadSelector()
    {
        pastMediaFileNames_ = new HashSet<>();
    }

    @Override
    public void initializeDroneMediaList(ArrayList<DJIMedia> mediaList)
    {
        for (DJIMedia media : mediaList)
        {
            pastMediaFileNames_.add(media.getFileName());
        }
    }

    @Override
    public ArrayList<DJIMedia> determineImagesForDownloadFromMediaList(ArrayList<DJIMedia> currentMediaList)
    {
        ArrayList<DJIMedia> imagesToDownload = new ArrayList<>();
        for(DJIMedia currentMedia : currentMediaList)
        {
            if (!pastMediaFileNames_.contains(currentMedia.getFileName()))
            {
                imagesToDownload.add(currentMedia);
                pastMediaFileNames_.add(currentMedia.getFileName());
            }
        }
        return imagesToDownload;
    }
}
