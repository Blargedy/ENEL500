package com.dji.sdk.sample.common.integration;

import dji.sdk.camera.DJIMediaManager;

/**
 * Created by Julia on 2017-02-12.
 */

public class MediaManager implements I_MediaManager {
    DJIMediaManager mediaManager_;

    MediaManager(DJIMediaManager mediaManager)
    {
        mediaManager_ = mediaManager;
    }

    @Override
    public void fetchMediaList(I_CameraMediaListDownloadListener downloadListener)
    {
        mediaManager_.fetchMediaList(downloadListener);
    }

    @Override
    public void setCameraModeMediaDownload(I_CompletionCallback callback)
    {
        mediaManager_.setCameraModeMediaDownload(callback);
    }
}
