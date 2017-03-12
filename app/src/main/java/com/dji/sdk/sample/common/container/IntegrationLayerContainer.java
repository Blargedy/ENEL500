package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.src.CameraSource;
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
    CameraSource cameraSource_;
    MissionManagerSource missionManagerSource_;
    FlightControllerSource flightControllerSource_;

    public IntegrationLayerContainer()
    {
        mediaManagerSource_ = new MediaManagerSource();
        mediaDataFetcher_ = new MediaDataFetcher();
        cameraSource_ = new CameraSource();

        missionManagerSource_ = new MissionManagerSource();
        flightControllerSource_ = new FlightControllerSource();
    }

    public I_MediaManagerSource mediaManagerSource()
    {
        return mediaManagerSource_;
    }

    public I_MediaDataFetcher mediaDataFetcher()
    {
        return mediaDataFetcher_;
    }

    public I_CameraSource cameraSource()
    {
        return cameraSource_;
    }

    public I_MissionManagerSource missionManagerSource() {return missionManagerSource_;}

    public I_FlightControllerSource flightControllerSource() {return flightControllerSource_;}

}
