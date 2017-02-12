package com.dji.sdk.sample.common.imageTransfer;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public class DroneToAndroidImageDownloadCoordinator implements
        I_DroneToAndroidImageDownloadCoordinator
{
    I_CameraMediaListFetcher mediaListFetcher_;
    I_NewImageDetector newImageDetector_;
    I_DroneToAndroidImageDownloader imageDownloader_;

    public DroneToAndroidImageDownloadCoordinator(
            I_CameraMediaListFetcher mediaListFetcher,
            I_NewImageDetector newImageDetector,
            I_DroneToAndroidImageDownloader imageDownloader)

    {
        mediaListFetcher_ = mediaListFetcher;
        newImageDetector_ = newImageDetector;
        imageDownloader_ = imageDownloader;
    }

    @Override
    public void downloadNewImagesFromDrone()
    {
        ArrayList<DJIMedia> fullMediaList = mediaListFetcher_.fetchMediaListFromCamera();
        ArrayList<DJIMedia> newMediaList =
                newImageDetector_.determineNewImagesFromFullListOfCameraMedia(fullMediaList);
        imageDownloader_.downloadImagesFromDrone(newMediaList);
    }


}
