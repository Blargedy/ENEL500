package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.integration.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;
import com.dji.sdk.sample.common.integration.MediaDataFetcher;
import com.dji.sdk.sample.common.integration.MediaManagerSource;

/**
 * Created by Julia on 2017-02-21.
 */

public class IntegrationLayerContainer
{
    MediaManagerSource mediaManagerSource_;
    MediaDataFetcher mediaDataFetcher_;

    public IntegrationLayerContainer()
    {
        mediaManagerSource_ = new MediaManagerSource();

        mediaDataFetcher_ = new MediaDataFetcher();
    }

    public I_MediaManagerSource mediaManagerSource()
    {
        return mediaManagerSource_;
    }

    public I_MediaDataFetcher mediaDataFetcher()
    {
        return mediaDataFetcher_;
    }
}
