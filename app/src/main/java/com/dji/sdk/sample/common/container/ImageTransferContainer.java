package com.dji.sdk.sample.common.container;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.CameraMediaDownloadModeChanger;
import com.dji.sdk.sample.common.imageTransfer.CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.DroneToAndroidImageDownloadInitiator;
import com.dji.sdk.sample.common.imageTransfer.DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.ImageTransferPathsSource;
import com.dji.sdk.sample.common.integration.I_MediaDataFetcher;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;
import com.dji.sdk.sample.common.mission.I_MissionController;
import com.dji.sdk.sample.common.presenter.ShootPhotoPresenter;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.FlightControlView;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferContainer
{
    private static final String TAG = "ImageTransferContainer";

    private ImageTransferPathsSource pathsSource_;
    private AndroidToPcImageCopier androidToPcImageCopier_;
    private DroneToAndroidImageDownloader droneToAndroidImageDownloader_;
    private DroneImageDownloadSelector downloadSelector_;
    private CameraMediaListFetcher mediaListFetcher_;
    private CameraMediaDownloadModeChanger cameraModeChanger_;

    private DroneToAndroidImageDownloadInitiator downloadInitiator_;

    private ShootPhotoPresenter shootPhotoPresenter_;

    public ImageTransferContainer(
            I_ApplicationContextManager contextManager,
            I_MediaManagerSource mediaManagerSource,
            I_MediaDataFetcher mediaDataFetcher,
            I_MissionController missionController,
            FlightControlView flightControlView)
    {
        pathsSource_ = new ImageTransferPathsSource(
                contextManager);

        androidToPcImageCopier_ = new AndroidToPcImageCopier(
                pathsSource_);

        droneToAndroidImageDownloader_ = new DroneToAndroidImageDownloader(
                pathsSource_,
                mediaDataFetcher,
                missionController,
                androidToPcImageCopier_);

        downloadSelector_ = new DroneImageDownloadSelector();

        mediaListFetcher_ = new CameraMediaListFetcher(
                mediaManagerSource,
                downloadSelector_,
                droneToAndroidImageDownloader_);

        cameraModeChanger_ = new CameraMediaDownloadModeChanger(
                mediaManagerSource,
                mediaListFetcher_);

        downloadInitiator_ = new DroneToAndroidImageDownloadInitiator(
                cameraModeChanger_);

        shootPhotoPresenter_ = new ShootPhotoPresenter(
                contextManager,
                flightControlView.shootPhotoButton(),
                downloadInitiator_);
    }
}
