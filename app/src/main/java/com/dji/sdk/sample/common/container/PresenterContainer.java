package com.dji.sdk.sample.common.container;

import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.presenter.MapPresenter;
import com.dji.sdk.sample.common.presenter.MissionControllerPresenter;
import com.dji.sdk.sample.common.presenter.MissionGenerationPresenter;
import com.dji.sdk.sample.common.presenter.ProductConnectionPresenter;
import com.dji.sdk.sample.common.presenter.TransferImagesPresenter;
import com.dji.sdk.sample.common.utility.GoogleMapsConnectionHandler;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.src.MapView;

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
            MissionContainer missionContainer,
            ImageTransferContainer imageTransferContainer,
            I_ApplicationContextManager contextManager,
            GoogleMapsConnectionHandler googleMapsConnectionHandler,
            FragmentActivity fragmentActivity)
    {
        mapPresenter_ = new MapPresenter(
                mapView,
                googleMapsConnectionHandler.googleApiClient(),
                fragmentActivity,
                missionContainer.missionGenerator(),
                missionContainer.initialMissionModel(),
                missionContainer.generatedMissionModel());

        missionControllerPresenter_ = new MissionControllerPresenter(
                mapView,
                missionContainer.missionController(),
                contextManager);

        missionGenerationPresenter_ = new MissionGenerationPresenter(
                mapView,
                missionContainer.missionGenerator(),
                contextManager);

        productConnectionPresenter_ = new ProductConnectionPresenter(
                mapView,
                contextManager);

        transferImagesPresenter_ = new TransferImagesPresenter(
                mapView,
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
