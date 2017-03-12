package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.imageTransfer.api.I_CameraModeChanger;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneMediaListInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleEnder;
import com.dji.sdk.sample.common.imageTransfer.api.InertImageTransferModuleEnder;
import com.dji.sdk.sample.common.imageTransfer.api.InertImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.InertImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.src.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.src.CameraModeChanger;
import com.dji.sdk.sample.common.imageTransfer.src.CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.src.DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.src.DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferCoordinator;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferModuleEnder;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferPathsSource;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferContainer
{
    private CameraModeChanger cameraModeChanger_;
    private CameraMediaListFetcher mediaListFetcher_;
    private DroneImageDownloadSelector downloadSelector_;

    private ImageTransferPathsSource pathsSource_;
    private AndroidToPcImageCopier androidToPcImageCopier_;
    private DroneToAndroidImageDownloader droneToAndroidImageDownloader_;

    private I_ImageTransferer imageTransferer_;

    private I_ImageTransferModuleInitializer imageTransferModuleInitializer_;
    private I_ImageTransferModuleEnder imageTransferModuleEnder_;

    public ImageTransferContainer(
            I_ApplicationContextManager contextManager,
            IntegrationLayerContainer integrationLayerContainer,
            String pcIpAddress,
            boolean isLiveModeEnabled)
    {
        cameraModeChanger_ = new CameraModeChanger(
                integrationLayerContainer.mediaManagerSource(),
                integrationLayerContainer.cameraSource());
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

            imageTransferModuleEnder_ = new ImageTransferModuleEnder(
                    imageTransferer_,
                    androidToPcImageCopier_);
        }
        else
        {
            imageTransferer_ = new InertImageTransferer();
            imageTransferModuleInitializer_ = new InertImageTransferModuleInitializer();
            imageTransferModuleEnder_ = new InertImageTransferModuleEnder();
        }
    }

    public I_ImageTransferer imageTransferer()
    {
        return imageTransferer_;
    }

    public I_ImageTransferModuleInitializer imageTransferModuleInitializer()
    {
        return imageTransferModuleInitializer_;
    }

    public I_ImageTransferModuleEnder imageTransferModuleEnder()
    {
        return imageTransferModuleEnder_;
    }

    public I_DroneMediaListInitializer droneMediaListInitializer()
    {
        return downloadSelector_;
    }

    public I_CameraModeChanger cameraModeChanger()
    {
        return cameraModeChanger_;
    }
}
