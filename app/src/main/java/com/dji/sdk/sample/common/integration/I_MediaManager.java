package com.dji.sdk.sample.common.integration;

import dji.common.util.DJICommonCallbacks;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_MediaManager
{
    void fetchMediaList(I_CameraMediaListDownloadListener downloadListener);
    void setCameraModeMediaDownload(I_CompletionCallback callback);
}
