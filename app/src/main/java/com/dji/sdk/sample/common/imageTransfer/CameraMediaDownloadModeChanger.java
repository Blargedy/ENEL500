package com.dji.sdk.sample.common.imageTransfer;

import android.util.Log;

import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-02-12.
 */

public class CameraMediaDownloadModeChanger implements
        I_CameraMediaDownloadModeChanger,
        I_CompletionCallback
{
    private static final String TAG = "CameraMediaDownloadModeChanger";

    private I_MediaManagerSource mediaManagerSource_;
    private I_CameraMediaListFetcher mediaListFetcher_;

    public CameraMediaDownloadModeChanger(
            I_MediaManagerSource mediaManagerSource,
            I_CameraMediaListFetcher mediaListFetcher)
    {
        mediaManagerSource_ = mediaManagerSource;
        mediaListFetcher_ = mediaListFetcher;
    }

    @Override
    public void changeCameraModeForMediaDownload()
    {
        I_MediaManager mediaManager = mediaManagerSource_.getMediaManager();
        mediaManager.setCameraModeMediaDownload(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            mediaListFetcher_.fetchMediaListFromCamera();
        }
        else
        {
            Log.e(TAG, "Failed to changed camera mode");
        }
    }
}
