package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferPathsSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.api.I_MediaDownloadListener;

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
    private I_ImageTransferPathsSource pathSource_;
    private I_MediaDataFetcher mediaDataFetcher_;
    private I_AndroidToPcImageCopier androidToPcImageCopier_;

    private ArrayList<DJIMedia> imagesLeftToDownload_;
    private I_CompletionCallback completionCallback_;

    public DroneToAndroidImageDownloader(
            I_ImageTransferPathsSource pathSource,
            I_MediaDataFetcher mediaDataFetcher,
            I_AndroidToPcImageCopier androidToPcImageCopier)
    {
        pathSource_ = pathSource;
        mediaDataFetcher_ = mediaDataFetcher;
        androidToPcImageCopier_ = androidToPcImageCopier;
    }

    @Override
    public void downloadImagesFromDrone(
            ArrayList<DJIMedia> imagesToDownload,
            I_CompletionCallback callback)
    {
        imagesLeftToDownload_ = imagesToDownload;
        completionCallback_ = callback;
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
        else
        {
            completionCallback_.onResult(null);
        }
    }

    @Override
    public void onSuccess(String path)
    {
        androidToPcImageCopier_.addImageToPcCopyQueue(path);
        downloadNextImage();
    }

    @Override
    public void onStart() {}

    @Override
    public void onRateUpdate(long total, long current, long persize) {}

    @Override
    public void onProgress(long total, long current) {}

    @Override
    public void onFailure(DJIError error)
    {
        completionCallback_.onResult(error);
    }
}
