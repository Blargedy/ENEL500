package com.dji.sdk.sample.common.imageTransfer;

import android.util.Log;
import android.widget.Toast;

import com.dji.sdk.sample.common.integration.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.I_MediaDownloadListener;
import com.dji.sdk.sample.common.mission.I_MissionController;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

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
    private I_AndroidToPcImageCopier androidToPcImageCopier_;

    private ArrayList<DJIMedia> imagesLeftToDownload_;

    // Temporary for the demo
    private I_ApplicationContextManager contextManager_;

    public DroneToAndroidImageDownloader(
            I_ImageTransferPathsSource pathSource,
            I_MediaDataFetcher mediaDataFetcher,
            I_MissionController missionController,
            I_AndroidToPcImageCopier androidToPcImageCopier,
            I_ApplicationContextManager contextManager)
    {
        pathSource_ = pathSource;
        mediaDataFetcher_ = mediaDataFetcher;
        missionController_ = missionController;
        androidToPcImageCopier_ = androidToPcImageCopier;

        contextManager_ = contextManager;
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
            Toast.makeText(contextManager_.getApplicationContext(), "Success: transferred photos" , Toast.LENGTH_SHORT).show();
            missionController_.resumeMission();
        }
    }

    @Override
    public void onSuccess(String path)
    {
        downloadNextImage();
        androidToPcImageCopier_.addImageToPcCopyQueue(path);
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
