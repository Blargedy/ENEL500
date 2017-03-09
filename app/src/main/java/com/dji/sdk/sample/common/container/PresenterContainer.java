package com.dji.sdk.sample.common.container;

import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.mission.src.MissionStateManager;
import com.dji.sdk.sample.common.presenter.src.MapPresenter;
import com.dji.sdk.sample.common.presenter.src.MissionControlsPresenter;
import com.dji.sdk.sample.common.presenter.src.ProductConnectionPresenter;
import com.dji.sdk.sample.common.utility.GoogleMapsConnectionHandler;
import com.dji.sdk.sample.common.view.src.FlightControlView;

/**
 * Created by Julia on 2017-03-05.
 */

public class PresenterContainer
{
    private MissionStateEntity missionState_;
    private MapPresenter mapPresenter_;
    private MissionControlsPresenter missionControlsPresenter_;
    private MissionStateManager missionStateManager_;

    private ProductConnectionPresenter productConnectionPresenter_;

    public PresenterContainer(
            FragmentActivity activity,
            FlightControlView flightControlView,
            MissionContainer missionContainer,
            GoogleMapsConnectionHandler googleMapsConnectionHandler)
    {
        missionState_ = new MissionStateEntity(
                activity);
        mapPresenter_ = new MapPresenter(
                activity,
                flightControlView,
                googleMapsConnectionHandler.googleApiClient());
        missionControlsPresenter_ = new MissionControlsPresenter(
                activity,
                flightControlView,
                missionState_);
        missionStateManager_ = new MissionStateManager(
                activity,
                mapPresenter_,
                missionContainer.missionGenerator(),
                missionContainer.missionController(),
                missionContainer.initialMissionModel(),
                missionContainer.generatedMissionModel(),
                missionState_);

        productConnectionPresenter_ = new ProductConnectionPresenter(
                activity,
                flightControlView);
    }
}
