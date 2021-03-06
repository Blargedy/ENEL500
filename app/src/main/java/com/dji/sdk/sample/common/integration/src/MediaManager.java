package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MediaManager;

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
}
