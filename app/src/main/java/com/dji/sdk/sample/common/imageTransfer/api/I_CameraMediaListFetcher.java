package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.integration.api.I_CameraMediaListDownloadListener;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_CameraMediaListFetcher
{
    void fetchMediaListFromCamera(I_CameraMediaListDownloadListener listener);
}
