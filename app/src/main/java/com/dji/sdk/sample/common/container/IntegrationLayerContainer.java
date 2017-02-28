package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.integration.FlightControllerSource;
import com.dji.sdk.sample.common.integration.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;
import com.dji.sdk.sample.common.integration.I_MissionManager;
import com.dji.sdk.sample.common.integration.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.MediaDataFetcher;
import com.dji.sdk.sample.common.integration.MediaManagerSource;
import com.dji.sdk.sample.common.integration.MissionManagerSource;

/**
 * Created by Julia on 2017-02-21.
 */

public class IntegrationLayerContainer
{
    MediaManagerSource mediaManagerSource_;
    MediaDataFetcher mediaDataFetcher_;
    MissionManagerSource missionManagerSource_;
    FlightControllerSource flightControllerSource_;

    public IntegrationLayerContainer()
    {
        mediaManagerSource_ = new MediaManagerSource();

        missionManagerSource_ = new MissionManagerSource();

        flightControllerSource_ = new FlightControllerSource();

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

    public I_MissionManagerSource missionManagerSource() {return missionManagerSource_;}

    public I_FlightControllerSource flightControllerSource() {return flightControllerSource_;}
}
