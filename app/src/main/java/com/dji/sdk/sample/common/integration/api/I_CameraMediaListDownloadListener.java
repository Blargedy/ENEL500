package com.dji.sdk.sample.common.integration.api;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;
import dji.sdk.camera.DJIMediaManager;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_CameraMediaListDownloadListener extends
        DJIMediaManager.CameraDownloadListener<ArrayList<DJIMedia>>
{
    void onStart();
    void onRateUpdate(long total, long current, long persize);
    void onProgress(long total, long current);
    void onSuccess(ArrayList<DJIMedia> mediaList);
    void onFailure(DJIError error);
}
