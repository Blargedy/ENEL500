package com.dji.sdk.sample.common.container;

import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.presenter.src.MapPresenter;
import com.dji.sdk.sample.common.presenter.src.MissionControlsPresenter;
import com.dji.sdk.sample.common.presenter.src.MissionMapDisplayPresenter;
import com.dji.sdk.sample.common.presenter.src.ProductConnectionPresenter;
import com.dji.sdk.sample.common.utility.GoogleMapsConnectionHandler;
import com.dji.sdk.sample.common.view.src.FlightControlView;

/**
 * Created by Julia on 2017-03-05.
 */

public class PresenterContainer
{
    private MapPresenter mapPresenter_;
    private MissionControlsPresenter missionControlsPresenter_;
    private MissionMapDisplayPresenter missionMapDisplayPresenter_;

    private ProductConnectionPresenter productConnectionPresenter_;

    public PresenterContainer(
            FragmentActivity activity,
            FlightControlView flightControlView,
            MissionContainer missionContainer,
            GoogleMapsConnectionHandler googleMapsConnectionHandler)
    {
        mapPresenter_ = new MapPresenter(
                activity,
                flightControlView,
                googleMapsConnectionHandler.googleApiClient());
        missionControlsPresenter_ = new MissionControlsPresenter(
                activity,
                flightControlView,
                missionContainer.missionState());
        missionMapDisplayPresenter_ = new MissionMapDisplayPresenter(
                activity,
                missionContainer.missionState(),
                missionContainer.initialMissionModel(),
                missionContainer.generatedMissionModel(),
                mapPresenter_);

        productConnectionPresenter_ = new ProductConnectionPresenter(
                activity,
                flightControlView);
    }
}
