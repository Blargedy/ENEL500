package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadQueuer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleEnder;
import com.dji.sdk.sample.common.imageTransfer.api.InertCameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.imageTransfer.api.InertDroneImageDownloadQueuer;
import com.dji.sdk.sample.common.imageTransfer.api.InertImageTransferModuleEnder;
import com.dji.sdk.sample.common.imageTransfer.api.InertImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.InertImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.src.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.src.DroneImageDownloadQueuer;
import com.dji.sdk.sample.common.imageTransfer.src.DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferCoordinator;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferPathsSource;
import com.dji.sdk.sample.common.imageTransfer.src.SimulatedCameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.utility.I_MissionStatusNotifier;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferContainer
{
    private ImageTransferPathsSource pathsSource_;
    private AndroidToPcImageCopier androidToPcImageCopier_;
    private DroneToAndroidImageDownloader droneToAndroidImageDownloader_;

    private I_DroneImageDownloadQueuer droneImageDownloadQueuer_;
    private I_ImageTransferer imageTransferer_;
    private I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback_;

    private I_ImageTransferModuleInitializer imageTransferModuleInitializer_;

    public ImageTransferContainer(
            I_ApplicationContextManager contextManager,
            I_MissionStatusNotifier missionStatusNotifier,
            IntegrationLayerContainer integrationLayerContainer,
            UtilityContainer utilityContainer,
            boolean isLiveModeEnabled)
    {
        pathsSource_ = new ImageTransferPathsSource(
                contextManager);
        androidToPcImageCopier_ = new AndroidToPcImageCopier(
                utilityContainer.applicationSettingsManager());
        droneToAndroidImageDownloader_ = new DroneToAndroidImageDownloader(
                pathsSource_,
                integrationLayerContainer.mediaDataFetcher(),
                androidToPcImageCopier_);

        if(isLiveModeEnabled)
        {
            droneImageDownloadQueuer_ = new DroneImageDownloadQueuer();

            imageTransferer_ = new ImageTransferCoordinator(
                    missionStatusNotifier,
                    integrationLayerContainer.cameraSource(),
                    droneImageDownloadQueuer_,
                    droneToAndroidImageDownloader_,
                    integrationLayerContainer.cameraState());

//            cameraGeneratedNewMediaFileCallback_ = new CameraGeneratedNewMediaFileCallback(
//                    droneImageDownloadQueuer_,
//                    imageTransferer_);

            cameraGeneratedNewMediaFileCallback_ = new SimulatedCameraGeneratedNewMediaFileCallback(
                    pathsSource_,
                    androidToPcImageCopier_);

            imageTransferModuleInitializer_ = new ImageTransferModuleInitializer(
                    androidToPcImageCopier_);
        }
        else
        {
            droneImageDownloadQueuer_ = new InertDroneImageDownloadQueuer();
            imageTransferer_ = new InertImageTransferer();
            cameraGeneratedNewMediaFileCallback_ = new InertCameraGeneratedNewMediaFileCallback();
            imageTransferModuleInitializer_ = new InertImageTransferModuleInitializer();
        }
    }

    public I_ImageTransferModuleInitializer imageTransferModuleInitializer()
    {
        return imageTransferModuleInitializer_;
    }

    public I_ImageTransferModuleEnder imageTransferModuleEnder()
    {
        return new InertImageTransferModuleEnder();
    }

    public I_CameraGeneratedNewMediaFileCallback cameraGeneratedNewMediaFileCallback()
    {
        return cameraGeneratedNewMediaFileCallback_;
    }
}
