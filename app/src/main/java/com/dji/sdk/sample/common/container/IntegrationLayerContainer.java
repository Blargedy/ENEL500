package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.integration.src.FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.api.I_MediaManagerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.integration.src.MediaDataFetcher;
import com.dji.sdk.sample.common.integration.src.MediaManagerSource;
import com.dji.sdk.sample.common.integration.src.MissionManagerSource;

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
