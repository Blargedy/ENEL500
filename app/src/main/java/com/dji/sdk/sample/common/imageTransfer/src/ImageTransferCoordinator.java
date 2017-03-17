package com.dji.sdk.sample.common.imageTransfer.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadQueuer;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-05.
 */

public class ImageTransferCoordinator implements
        I_ImageTransferer,
        I_CompletionCallback
{
    private static final String TAG = "ImageTransferCoordinator";

    private enum ExpectedCallback {
        SWITCH_TO_DOWNLOAD,
        DOWNLOAD_PHOTOS,
        SWITCH_TO_SHOOT_PHOTO }
    ExpectedCallback nextCallback_;

    private I_CameraSource cameraSource_;
    private I_DroneImageDownloadQueuer downloadQueuer_;
    private I_DroneToAndroidImageDownloader imageDownloader_;

    private I_CompletionCallback completionCallback_;

    public ImageTransferCoordinator(
            I_CameraSource cameraSource,
            I_DroneImageDownloadQueuer downloadQueuer,
            I_DroneToAndroidImageDownloader imageDownloader)
    {
        cameraSource_ = cameraSource;
        downloadQueuer_ = downloadQueuer;
        imageDownloader_ = imageDownloader;
    }

    @Override
    public void transferNewImagesFromDrone(I_CompletionCallback callback)
    {
        completionCallback_ = callback;

        nextCallback_ = ExpectedCallback.SWITCH_TO_DOWNLOAD;
        cameraSource_.getCamera().setCameraMode(I_Camera.CameraMode.MEDIA_DOWNLOAD, this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (nextCallback_)
            {
                case SWITCH_TO_DOWNLOAD:
                    nextCallback_ = ExpectedCallback.DOWNLOAD_PHOTOS;
                    ArrayList<DJIMedia> imagesToDownload = downloadQueuer_.getListOfImagesToDownload();
                    imageDownloader_.downloadImagesFromDrone(imagesToDownload, this);
                    break;
                case DOWNLOAD_PHOTOS:
                    nextCallback_ = ExpectedCallback.SWITCH_TO_SHOOT_PHOTO;
                    cameraSource_.getCamera().setCameraMode(I_Camera.CameraMode.SHOOT_PHOTO, this);
                    break;
                case SWITCH_TO_SHOOT_PHOTO:
                    completionCallback_.onResult(null);
                    break;
                default:
                    break;
            }
        }
        else
        {
            Log.e(TAG, "Failed while expecting " + nextCallback_.name() +
                    " callback : " + error.getDescription());

            completionCallback_.onResult(error);
        }
    }
}
