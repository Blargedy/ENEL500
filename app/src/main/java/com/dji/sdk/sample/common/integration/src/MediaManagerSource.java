package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_MediaManager;
import com.dji.sdk.sample.common.integration.api.I_MediaManagerSource;

/**
 * Created by Julia on 2017-02-21.
 */

public class MediaManagerSource implements I_MediaManagerSource
{
    @Override
    public I_MediaManager getMediaManager()
    {
        return new MediaManager(DJISampleApplication.getProductInstance().getCamera().getMediaManager());
    }
}
