package com.dji.sdk.sample.common.integration.api;

import java.io.File;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-17.
 */

public interface I_MediaDataFetcher
{
    void fetchMediaData(
            DJIMedia media,
            File destinationDirectory,
            I_MediaDownloadListener listener);
}
