package com.dji.sdk.sample.common.imageTransfer;

import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public class CameraMediaListFetcher implements I_CameraMediaListFetcher
{
    I_MediaManagerSource mediaManagerSource_;
    I_CameraMediaListDownloadListener downloadListener_;

    public CameraMediaListFetcher(
            I_MediaManagerSource mediaManagerSource,
            I_CameraMediaListDownloadListener downloadListener)
    {
        mediaManagerSource_ = mediaManagerSource;
        downloadListener_ = downloadListener;
    }

    @Override
    public ArrayList<DJIMedia> fetchMediaListFromCamera()
    {
        I_MediaManager mediaManager = mediaManagerSource_.getMediaManager();

        mediaManager.fetchMediaList(downloadListener_);

        return null;
    }
}
