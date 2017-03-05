package com.dji.sdk.sample.common.integration.api;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMediaManager;

/**
 * Created by Julia on 2017-02-17.
 */

public interface I_MediaDownloadListener extends
        DJIMediaManager.CameraDownloadListener<String>
{
    void onStart();
    void onRateUpdate(long total, long current, long persize);
    void onProgress(long total, long current);
    void onSuccess(String path);
    void onFailure(DJIError error);
}
