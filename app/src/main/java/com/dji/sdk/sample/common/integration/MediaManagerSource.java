package com.dji.sdk.sample.common.integration;

import dji.sdk.camera.DJICamera;
import dji.sdk.camera.DJIMediaManager;

/**
 * Created by Julia on 2017-02-21.
 */

public class MediaManagerSource implements I_MediaManagerSource
{
    @Override
    public I_MediaManager getMediaManager()
    {
        DJIMediaManager mediaManager =
                DJISampleApplication.getProductInstance().getCamera().getMediaManager();
        return new MediaManager(mediaManager);
    }
}
