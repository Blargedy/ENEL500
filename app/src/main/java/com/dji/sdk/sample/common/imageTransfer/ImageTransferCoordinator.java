package com.dji.sdk.sample.common.imageTransfer;


import android.util.Log;

import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-05.
 */

public class ImageTransferCoordinator implements
        I_ImageTransferer,
        I_CompletionCallback,
        I_CameraMediaListDownloadListener
{
    private static final String TAG = "ImageTransferCoordinator";

    private I_CameraMediaDownloadModeChanger modeChanger_;
    private I_CameraMediaListFetcher mediaListFetcher_;
    private I_DroneImageDownloadSelector downloadSelector_;
    private I_DroneToAndroidImageDownloader imageDownloader_;

    private I_ImageTransferCompletionCallback completionCallback_;

    public ImageTransferCoordinator(
            I_CameraMediaDownloadModeChanger modeChanger,
            I_CameraMediaListFetcher mediaListFetcher,
            I_DroneImageDownloadSelector downloadSelector,
            I_DroneToAndroidImageDownloader imageDownloader)
    {
        modeChanger_ = modeChanger;
        mediaListFetcher_ = mediaListFetcher;
        downloadSelector_ = downloadSelector;
        imageDownloader_ = imageDownloader;
    }

    @Override
    public void transferNewImagesFromDrone(I_ImageTransferCompletionCallback callback)
    {
        completionCallback_ = callback;
        modeChanger_.changeCameraModeForMediaDownload(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            mediaListFetcher_.fetchMediaListFromCamera(this);
        }
        else
        {
            Log.e(TAG, "Failed to change camera mode : " + error.getDescription());
        }
    }

    @Override
    public void onSuccess(ArrayList<DJIMedia> currentMediaList)
    {
        ArrayList<DJIMedia> imagesToDownload = downloadSelector_
                .determineImagesForDownloadFromMediaList(currentMediaList);
        imageDownloader_.downloadImagesFromDrone(imagesToDownload, completionCallback_);
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
        Log.e(TAG, "Failed to fetched media list: " + error.getDescription());
    }
}
