package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.imageTransfer.api.InertImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.InertImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.src.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.src.CameraMediaDownloadModeChanger;
import com.dji.sdk.sample.common.imageTransfer.src.CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.src.DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.src.DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferCoordinator;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferPathsSource;
import com.dji.sdk.sample.common.integration.api.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.api.I_MediaManagerSource;
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

    private I_ImageTransferer imageTransferer_;

    private I_ImageTransferModuleInitializer imageTransferModuleInitializer_;

    private TransferImagesPresenter transferImagesPresenter_;

    public ImageTransferContainer(
            I_ApplicationContextManager contextManager,
            IntegrationLayerContainer integrationLayerContainer,
            FlightControlView flightControlView,
            String pcIpAddress,
            boolean isLiveModeEnabled)
    {
        cameraModeChanger_ = new CameraMediaDownloadModeChanger(
                integrationLayerContainer.mediaManagerSource());
        mediaListFetcher_ = new CameraMediaListFetcher(
                integrationLayerContainer.mediaManagerSource(),
                downloadSelector_,
                droneToAndroidImageDownloader_);
        downloadSelector_ = new DroneImageDownloadSelector();

        pathsSource_ = new ImageTransferPathsSource(
                contextManager);
        androidToPcImageCopier_ = new AndroidToPcImageCopier(
                pcIpAddress);
        droneToAndroidImageDownloader_ = new DroneToAndroidImageDownloader(
                pathsSource_,
                integrationLayerContainer.mediaDataFetcher(),
                androidToPcImageCopier_);

        if(isLiveModeEnabled)
        {
            imageTransferer_ = new ImageTransferCoordinator(
                    cameraModeChanger_,
                    mediaListFetcher_,
                    downloadSelector_,
                    droneToAndroidImageDownloader_);

            imageTransferModuleInitializer_ = new ImageTransferModuleInitializer(
                    cameraModeChanger_,
                    mediaListFetcher_,
                    downloadSelector_,
                    androidToPcImageCopier_);
        }
        else
        {
            imageTransferer_ = new InertImageTransferer();
            imageTransferModuleInitializer_ = new InertImageTransferModuleInitializer();
        }

        transferImagesPresenter_ = new TransferImagesPresenter(
                flightControlView.transferImagesButton(),
                contextManager,
                imageTransferer_,
                downloadSelector_);
    }

    public I_ImageTransferer imageTransferer()
    {
        return imageTransferer_;
    }

    public I_ImageTransferModuleInitializer imageTransferModuleInitializer()
    {
        return imageTransferModuleInitializer_;
    }
}
