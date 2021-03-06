package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.api.I_MediaDownloadListener;

import java.io.File;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-17.
 */

public class MediaDataFetcher implements I_MediaDataFetcher
{
    @Override
    public void fetchMediaData(
            DJIMedia media,
            File destinationDirectory,
            I_MediaDownloadListener listener)
    {
        media.fetchMediaData(destinationDirectory, null, listener);
    }
}
