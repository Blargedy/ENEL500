package com.dji.sdk.sample.common.imageTransfer;

import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

/**
 * Created by Julia on 2017-02-12.
 */

public class CameraMediaDownloadModeChanger implements
        I_CameraMediaDownloadModeChanger
{
    private I_MediaManagerSource mediaManagerSource_;

    public CameraMediaDownloadModeChanger(
            I_MediaManagerSource mediaManagerSource)
    {
        mediaManagerSource_ = mediaManagerSource;
    }

    @Override
    public void changeCameraModeForMediaDownload(I_CompletionCallback callback)
    {
        I_MediaManager mediaManager = mediaManagerSource_.getMediaManager();
        mediaManager.setCameraModeMediaDownload(callback);
    }
}
