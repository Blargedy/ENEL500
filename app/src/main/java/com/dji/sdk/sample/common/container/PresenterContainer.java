package com.dji.sdk.sample.common.container;

import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.presenter.MapPresenter;
import com.dji.sdk.sample.common.presenter.MissionControllerPresenter;
import com.dji.sdk.sample.common.presenter.MissionGenerationPresenter;
import com.dji.sdk.sample.common.presenter.ProductConnectionPresenter;
import com.dji.sdk.sample.common.presenter.TransferImagesPresenter;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.FlightControlView;
import com.dji.sdk.sample.common.view.MapView;

/**
 * Created by Julia on 2017-03-05.
 */

public class PresenterContainer
{
    private MapPresenter mapPresenter_;
    private MissionControllerPresenter missionControllerPresenter_;
    private MissionGenerationPresenter missionGenerationPresenter_;
    private ProductConnectionPresenter productConnectionPresenter_;
    private TransferImagesPresenter transferImagesPresenter_;

    public PresenterContainer(
            MapView mapView,
            FlightControlView flightControlView,
            MissionContainer missionContainer,
            ImageTransferContainer imageTransferContainer,
            I_ApplicationContextManager contextManager,
            FragmentActivity fragmentActivity)
    {
        mapPresenter_ = new MapPresenter(
                mapView,
                fragmentActivity,
                missionContainer.missionGenerator(),
                missionContainer.initialMissionModel(),
                missionContainer.generatedMissionModel());

        missionControllerPresenter_ = new MissionControllerPresenter(
                flightControlView,
                missionContainer.missionController(),
                contextManager);

        missionGenerationPresenter_ = new MissionGenerationPresenter(
                flightControlView,
                missionContainer.missionGenerator(),
                contextManager);

        productConnectionPresenter_ = new ProductConnectionPresenter(
                flightControlView,
                contextManager);

        transferImagesPresenter_ = new TransferImagesPresenter(
                flightControlView,
                contextManager,
                imageTransferContainer.imageTransferer(),
                imageTransferContainer.androidToPcImageCopier(),
                imageTransferContainer.droneMediaListInitializer());
    }

    public MapPresenter mapPresenter()
    {
        return mapPresenter_;
    }
}
