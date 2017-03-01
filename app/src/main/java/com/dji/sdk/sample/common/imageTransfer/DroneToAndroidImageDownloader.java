package com.dji.sdk.sample.common.imageTransfer;

import android.util.Log;

import com.dji.sdk.sample.common.integration.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.I_MediaDownloadListener;
import com.dji.sdk.sample.common.mission.I_MissionController;

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
    private static final String TAG = "DroneToAndroidImageDownloader";

    private I_ImageTransferPathsSource pathSource_;
    private I_MediaDataFetcher mediaDataFetcher_;
    private I_MissionController missionController_;

    private ArrayList<DJIMedia> imagesLeftToDownload_;

    public DroneToAndroidImageDownloader(
            I_ImageTransferPathsSource pathSource,
            I_MediaDataFetcher mediaDataFetcher,
            I_MissionController missionController)
    {
        pathSource_ = pathSource;
        mediaDataFetcher_ = mediaDataFetcher;
        missionController_ = missionController;
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
        else
        {
            missionController_.resumeMission();
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
    public void onFailure(DJIError error)
    {
        Log.e(TAG, "Failed to download an image");
    }
}
