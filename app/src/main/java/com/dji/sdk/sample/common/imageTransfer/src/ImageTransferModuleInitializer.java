package com.dji.sdk.sample.common.imageTransfer.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_CameraModeChanger;
import com.dji.sdk.sample.common.imageTransfer.api.I_CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneMediaListInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferModuleInitializer implements
        I_ImageTransferModuleInitializer,
        I_CompletionCallback,
        I_CameraMediaListDownloadListener
{
    private static final String TAG = "ImageTransferModuleInitializer";

    private I_CameraModeChanger modeChanger_;
    private I_CameraMediaListFetcher mediaListFetcher_;
    private I_DroneMediaListInitializer mediaListInitializer_;
    private AndroidToPcImageCopier androidToPcImageCopier_;

    private I_CompletionCallback callback_;

    public ImageTransferModuleInitializer(
            I_CameraModeChanger modeChanger,
            I_CameraMediaListFetcher mediaListFetcher,
            I_DroneMediaListInitializer mediaListInitializer,
            AndroidToPcImageCopier androidToPcImageCopier)
    {
        modeChanger_ = modeChanger;
        mediaListFetcher_ = mediaListFetcher;
        mediaListInitializer_ = mediaListInitializer;
        androidToPcImageCopier_ = androidToPcImageCopier;
    }

    @Override
    public void initializeImageTransferModulePriorToFlight(I_CompletionCallback callback)
    {
        callback_ = callback;
        androidToPcImageCopier_.start();
        modeChanger_.changeToMediaDownloadMode(this);
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
            callback_.onResult(error);
        }
    }

    @Override
    public void onSuccess(ArrayList<DJIMedia> mediaList)
    {
        mediaListInitializer_.initializeDroneMediaList(mediaList);
        callback_.onResult(null);
    }

    @Override
    public void onFailure(DJIError error)
    {
        callback_.onResult(error);
    }

    @Override
    public void onStart() {}

    @Override
    public void onRateUpdate(long total, long current, long persize) {}

    @Override
    public void onProgress(long total, long current) {}
}
