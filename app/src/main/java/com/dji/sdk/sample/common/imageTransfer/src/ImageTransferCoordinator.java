package com.dji.sdk.sample.common.imageTransfer.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_CameraModeChanger;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
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

    private I_CameraModeChanger modeChanger_;
    private I_DroneImageDownloadSelector downloadSelector_;
    private I_DroneToAndroidImageDownloader imageDownloader_;

    private I_CompletionCallback completionCallback_;

    public ImageTransferCoordinator(
            I_CameraModeChanger modeChanger,
            I_DroneImageDownloadSelector downloadSelector,
            I_DroneToAndroidImageDownloader imageDownloader)
    {
        modeChanger_ = modeChanger;
        downloadSelector_ = downloadSelector;
        imageDownloader_ = imageDownloader;
    }

    @Override
    public void transferNewImagesFromDrone(I_CompletionCallback callback)
    {
        completionCallback_ = callback;

        nextCallback_ = ExpectedCallback.SWITCH_TO_DOWNLOAD;
        modeChanger_.changeToMediaDownloadMode(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (nextCallback_)
            {
                case SWITCH_TO_DOWNLOAD:
                    ArrayList<DJIMedia> imagesToDownload = downloadSelector_
                            .determineImagesForDownloadFromMediaList(null);

                    nextCallback_ = ExpectedCallback.DOWNLOAD_PHOTOS;
                    imageDownloader_.downloadImagesFromDrone(imagesToDownload, this);

                    break;
                case DOWNLOAD_PHOTOS:
                    nextCallback_ = ExpectedCallback.SWITCH_TO_SHOOT_PHOTO;
                    modeChanger_.changeToShootPhotoMode(this);
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
