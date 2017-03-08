package com.dji.sdk.sample.common.container;

import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.testClasses.MapPresenterTest;
import com.dji.sdk.sample.common.testClasses.MissionControllerPresenterTest;
import com.dji.sdk.sample.common.testClasses.MissionGenerationPresenterTest;
import com.dji.sdk.sample.common.presenter.src.ProductConnectionPresenter;
import com.dji.sdk.sample.common.testClasses.TransferImagesPresenterTest;
import com.dji.sdk.sample.common.utility.GoogleMapsConnectionHandler;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.testClasses.MapViewTest;

/**
 * Created by Julia on 2017-03-05.
 */

public class PresenterContainer
{
    private MapPresenterTest mapPresenter_;
    private MissionControllerPresenterTest missionControllerPresenter_;
    private MissionGenerationPresenterTest missionGenerationPresenter_;
    private ProductConnectionPresenter productConnectionPresenter_;
    private TransferImagesPresenterTest transferImagesPresenter_;

    public PresenterContainer(
            MapViewTest mapView,
            MissionContainer missionContainer,
            ImageTransferContainer imageTransferContainer,
            I_ApplicationContextManager contextManager,
            GoogleMapsConnectionHandler googleMapsConnectionHandler,
            FragmentActivity fragmentActivity)
    {
        mapPresenter_ = new MapPresenterTest(
                mapView,
                googleMapsConnectionHandler.googleApiClient(),
                fragmentActivity);

        missionControllerPresenter_ = new MissionControllerPresenterTest(
                mapView,
                missionContainer.missionController(),
                contextManager);

        missionGenerationPresenter_ = new MissionGenerationPresenterTest(
                mapView,
                missionContainer.missionGenerator(),
                contextManager);

        productConnectionPresenter_ = new ProductConnectionPresenter(
                mapView,
                contextManager);

        transferImagesPresenter_ = new TransferImagesPresenterTest(
                mapView,
                contextManager,
                imageTransferContainer.imageTransferer(),
                imageTransferContainer.androidToPcImageCopier(),
                imageTransferContainer.droneMediaListInitializer());
    }

    public MapPresenterTest mapPresenter()
    {
        return mapPresenter_;
    }
}
