package com.dji.sdk.sample.common.integration;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;
import dji.sdk.camera.DJIMediaManager;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_MediaManager
{
    void fetchMediaList(I_CameraMediaListDownloadListener downloadListener);
}
