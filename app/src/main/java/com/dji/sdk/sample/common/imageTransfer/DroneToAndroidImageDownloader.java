package com.dji.sdk.sample.common.imageTransfer;

import com.dji.sdk.sample.common.integration.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.I_MediaDownloadListener;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-17.
 */

public class DroneToAndroidImageDownloader implements
        I_DroneToAndroidImageDownloader,
        I_MediaDownloadListener
{
    private I_ImageTransferPathSource pathSource_;
    private I_MediaDataFetcher mediaDataFetcher_;
    private ArrayList<DJIMedia> imagesLeftToDownload_;

    public DroneToAndroidImageDownloader(
            I_ImageTransferPathSource pathSource,
            I_MediaDataFetcher mediaDataFetcher)
    {
        pathSource_ = pathSource;
        mediaDataFetcher_ = mediaDataFetcher;
    }

    @Override
    public void downloadImagesFromDrone(ArrayList<DJIMedia> imagesToDownload)
    {
        imagesLeftToDownload_ = imagesToDownload;
        downloadNextImage();
    }

    private void downloadNextImage()
    {
        if (!imagesLeftToDownload_.isEmpty())
        {
            mediaDataFetcher_.fetchMediaData(
                    imagesLeftToDownload_.remove(0),
                    pathSource_.androidDeviceImagePath(),
                    this);
        }
    }

    @Override
    public void onSuccess(String path)
    {
        downloadNextImage();
    }

    @Override
    public void onStart() {}

    @Override
    public void onRateUpdate(long total, long current, long persize) {}

    @Override
    public void onProgress(long total, long current) {}

    @Override
    public void onFailure(DJIError error) {}
}
