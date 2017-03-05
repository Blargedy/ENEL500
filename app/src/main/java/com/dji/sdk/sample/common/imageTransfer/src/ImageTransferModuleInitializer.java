package com.dji.sdk.sample.common.imageTransfer.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_DroneMediaListInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.api.I_MediaManager;
import com.dji.sdk.sample.common.integration.api.I_MediaManagerSource;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferModuleInitializer implements
        I_ImageTransferModuleInitializer,
        I_CameraMediaListDownloadListener
{
    private static final String TAG = "ImageTransferModuleInitializer";

    private I_MediaManagerSource mediaManagerSource_;
    private I_DroneMediaListInitializer mediaListInitializer_;
    private AndroidToPcImageCopier androidToPcImageCopier_;


    public ImageTransferModuleInitializer(
            I_MediaManagerSource mediaManagerSource,
            I_DroneMediaListInitializer mediaListInitializer,
            AndroidToPcImageCopier androidToPcImageCopier)
    {
        mediaManagerSource_ = mediaManagerSource;
        mediaListInitializer_ = mediaListInitializer;
        androidToPcImageCopier_ = androidToPcImageCopier;
    }

    @Override
    public void initializeImageTransferModulePriorToFlight()
    {
        androidToPcImageCopier_.start();
        I_MediaManager mediaManager = mediaManagerSource_.getMediaManager();
        mediaManager.fetchMediaList(this);
    }

    @Override
    public void onSuccess(ArrayList<DJIMedia> mediaList)
    {
        mediaListInitializer_.initializeDroneMediaList(mediaList);
    }

    @Override
    public void onFailure(DJIError error)
    {
        Log.e(TAG, "Failed to fetched media list");
    }

    @Override
    public void onStart() {}

    @Override
    public void onRateUpdate(long total, long current, long persize) {}

    @Override
    public void onProgress(long total, long current) {}
}
