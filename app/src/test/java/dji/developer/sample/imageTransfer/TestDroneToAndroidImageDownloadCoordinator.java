package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.DroneToAndroidImageDownloadCoordinator;
import com.dji.sdk.sample.common.imageTransfer.I_CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.I_NewImageDetector;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Matchers.*;

import org.junit.Test;
import org.mockito.Matchers;

import java.lang.reflect.Array;
import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public class TestDroneToAndroidImageDownloadCoordinator
{
    I_CameraMediaListFetcher mediaListFetcher_ = mock(I_CameraMediaListFetcher.class);
    I_NewImageDetector newImageDetector_ = mock(I_NewImageDetector.class);
    I_DroneToAndroidImageDownloader imageDownloader_ = mock(I_DroneToAndroidImageDownloader.class);


    DroneToAndroidImageDownloadCoordinator patient_ = new DroneToAndroidImageDownloadCoordinator(
            mediaListFetcher_,
            newImageDetector_,
            imageDownloader_);

    @Test
    public void willQueryCameraForListOfMediaOnSdCard()
    {
        patient_.downloadNewImagesFromDrone();

        verify(mediaListFetcher_).fetchMediaListFromCamera();
    }

    @Test
    public void willRequestListOfNewImagesBasedOnTheMediaRetrievedFromCamera()
    {
        DJIMedia media1 = new DJIMedia();
        DJIMedia media2 = new DJIMedia();
        ArrayList<DJIMedia> fullMediaList = new ArrayList<>();
        fullMediaList.add(media1);
        fullMediaList.add(media2);
        when(mediaListFetcher_.fetchMediaListFromCamera()).thenReturn(fullMediaList);

        patient_.downloadNewImagesFromDrone();

        verify(newImageDetector_).determineNewImagesFromFullListOfCameraMedia(fullMediaList);
    }

    @Test
    public void willDownloadListOfNewImagesFromDrone()
    {
        DJIMedia media1 = new DJIMedia();
        DJIMedia media2 = new DJIMedia();
        ArrayList<DJIMedia> mewMediaList = new ArrayList<>();
        mewMediaList.add(media1);
        mewMediaList.add(media2);
        when(newImageDetector_.determineNewImagesFromFullListOfCameraMedia(
                ArgumentMatchers.<ArrayList<DJIMedia>>any())).thenReturn(mewMediaList);

        patient_.downloadNewImagesFromDrone();

        verify(imageDownloader_).downloadImagesFromDrone(mewMediaList);
    }
}
