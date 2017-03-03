package com.dji.sdk.sample.common.imageTransfer;

import android.util.Log;

import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public class CameraMediaListFetcher implements
        I_CameraMediaListFetcher,
        I_CameraMediaListDownloadListener
{
    private static final String TAG = "CameraMediaListFetcher";

    private I_MediaManagerSource mediaManagerSource_;
    private I_DroneImageDownloadSelector downloadSelector_;
    private I_DroneToAndroidImageDownloader imageDownloader_;

    public CameraMediaListFetcher(
            I_MediaManagerSource mediaManagerSource,
            I_DroneImageDownloadSelector downloadSelector,
            I_DroneToAndroidImageDownloader imageDownloader)
    {
        mediaManagerSource_ = mediaManagerSource;
        downloadSelector_ = downloadSelector;
        imageDownloader_ = imageDownloader;
    }

    @Override
    public void fetchMediaListFromCamera()
    {
        I_MediaManager mediaManager = mediaManagerSource_.getMediaManager();
        mediaManager.fetchMediaList(this);
    }

    @Override
    public void onSuccess(ArrayList<DJIMedia> currentMediaList)
    {
        ArrayList<DJIMedia> imagesToDownload = downloadSelector_
                .determineImagesForDownloadFromMediaList(currentMediaList);
        imageDownloader_.downloadImagesFromDrone(imagesToDownload);
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
        Log.e(TAG, "Failed to fetched media list: " + error.toString());
    }
}
