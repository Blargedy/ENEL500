package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.I_DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public class TestCameraMediaListFetcher
{
    private I_MediaManagerSource mediaManagerSource_ = mock(I_MediaManagerSource.class);
    private I_DroneImageDownloadSelector downloadSelector_ =
            mock(I_DroneImageDownloadSelector.class);
    private I_DroneToAndroidImageDownloader imageDownloader_ =
            mock(I_DroneToAndroidImageDownloader.class);

    private CameraMediaListFetcher patient_ = new CameraMediaListFetcher(
            mediaManagerSource_,
            downloadSelector_,
            imageDownloader_);

    private I_MediaManager mediaManager_ = mock(I_MediaManager.class);

    @Test
    public void willRetrieveMediaMangerUsingMediaManagerSource()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.fetchMediaListFromCamera();

        verify(mediaManagerSource_).getMediaManager();
    }

    @Test
    public void willFetchMediaListUsingMediaManager()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.fetchMediaListFromCamera();

        verify(mediaManager_).fetchMediaList(patient_);
    }

    @Test
    public void willDetermineWhichNeedToBeDownloadedAfterMediaListHasBeenFetchedSuccessfully()
    {
        final ArrayList<DJIMedia> mediaList = new ArrayList<>();
        makeMediaManagerCallOnSuccessCallbackWithMediaList(mediaList);
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.fetchMediaListFromCamera();

        verify(downloadSelector_).determineImagesForDownloadFromMediaList(mediaList);
    }

    @Test
    public void willDownloadSelectedImages()
    {
        final ArrayList<DJIMedia> mediaList = makeMediaList(2);
        ArrayList<DJIMedia> imagesToDownload = makeMediaList(3);
        makeMediaManagerCallOnSuccessCallbackWithMediaList(mediaList);
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);
        when(downloadSelector_.determineImagesForDownloadFromMediaList(mediaList))
                .thenReturn(imagesToDownload);

        patient_.fetchMediaListFromCamera();

        verify(imageDownloader_).downloadImagesFromDrone(imagesToDownload);
    }

    private void makeMediaManagerCallOnSuccessCallbackWithMediaList(
            final ArrayList<DJIMedia> mediaList)
    {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((I_CameraMediaListDownloadListener)args[0]).onSuccess(mediaList);
                return null;
            }})
                .when(mediaManager_).fetchMediaList(patient_);
    }

    private ArrayList<DJIMedia> makeMediaList(int size)
    {
        ArrayList<DJIMedia> currentMediaList = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            currentMediaList.add(new DJIMedia());
        }
        return currentMediaList;
    }
}
