package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.imageTransfer.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.CameraMediaDownloadModeChanger;
import com.dji.sdk.sample.common.imageTransfer.CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.ImageTransferCoordinator;
import com.dji.sdk.sample.common.imageTransfer.ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.ImageTransferPathsSource;
import com.dji.sdk.sample.common.integration.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;
import com.dji.sdk.sample.common.mission.I_MissionController;
import com.dji.sdk.sample.common.presenter.TransferImagesPresenter;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.FlightControlView;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferContainer
{
    private static final String TAG = "ImageTransferContainer";

    private CameraMediaDownloadModeChanger cameraModeChanger_;
    private CameraMediaListFetcher mediaListFetcher_;
    private DroneImageDownloadSelector downloadSelector_;

    private ImageTransferPathsSource pathsSource_;
    private AndroidToPcImageCopier androidToPcImageCopier_;
    private DroneToAndroidImageDownloader droneToAndroidImageDownloader_;

    private ImageTransferCoordinator imageTransferCoordinator_;

    private ImageTransferModuleInitializer imageTransferModuleInitializer_;

    private TransferImagesPresenter transferImagesPresenter_;

    public ImageTransferContainer(
            I_ApplicationContextManager contextManager,
            I_MediaManagerSource mediaManagerSource,
            I_MediaDataFetcher mediaDataFetcher,
            FlightControlView flightControlView,
            String pcIpAddress)
    {
        cameraModeChanger_ = new CameraMediaDownloadModeChanger(
                mediaManagerSource);
        mediaListFetcher_ = new CameraMediaListFetcher(
                mediaManagerSource,
                downloadSelector_,
                droneToAndroidImageDownloader_);
        downloadSelector_ = new DroneImageDownloadSelector();

        pathsSource_ = new ImageTransferPathsSource(
                contextManager);
        androidToPcImageCopier_ = new AndroidToPcImageCopier(
                pcIpAddress);
        droneToAndroidImageDownloader_ = new DroneToAndroidImageDownloader(
                pathsSource_,
                mediaDataFetcher,
                androidToPcImageCopier_);

        imageTransferCoordinator_ = new ImageTransferCoordinator(
                cameraModeChanger_,
                mediaListFetcher_,
                downloadSelector_,
                droneToAndroidImageDownloader_);

        imageTransferModuleInitializer_ = new ImageTransferModuleInitializer(
                mediaManagerSource,
                downloadSelector_,
                androidToPcImageCopier_);

        transferImagesPresenter_ = new TransferImagesPresenter(
                flightControlView.transferImagesButton(),
                contextManager,
                imageTransferCoordinator_,
                downloadSelector_);
    }

    public I_ImageTransferer imageTransferer()
    {
        return imageTransferCoordinator_;
    }

    public I_ImageTransferModuleInitializer imageTransferModuleInitializer()
    {
        return imageTransferModuleInitializer_;
    }
}
