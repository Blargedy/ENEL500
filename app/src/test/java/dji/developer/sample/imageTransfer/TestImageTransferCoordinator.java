package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.api.I_CameraModeChanger;
import com.dji.sdk.sample.common.imageTransfer.api.I_CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferCompletionCallback;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferCoordinator;
import com.dji.sdk.sample.common.integration.api.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.regex.Matcher;

import dji.sdk.camera.DJIMedia;

import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-03-05.
 */

public class TestImageTransferCoordinator
{
    private I_CameraModeChanger modeChanger_ = mock(I_CameraModeChanger.class);
    private I_CameraMediaListFetcher mediaListFetcher_ = mock(I_CameraMediaListFetcher.class);
    private I_DroneImageDownloadSelector downloadSelector_ = mock(I_DroneImageDownloadSelector.class);
    private I_DroneToAndroidImageDownloader imageDownloader_ = mock(I_DroneToAndroidImageDownloader.class);

    private ImageTransferCoordinator patient_ = new ImageTransferCoordinator(
            modeChanger_,
            mediaListFetcher_,
            downloadSelector_,
            imageDownloader_);

    private I_ImageTransferCompletionCallback completionCallback_ = mock(I_ImageTransferCompletionCallback.class);

    @Test
    public void willChangeCameraModeToMediaDownloadMode()
    {
        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(modeChanger_).changeToMediaDownloadMode(patient_);
    }

    @Test
    public void willFetchMediaListAfterChangingCameraMode()
    {
        makeModeChangerCallOnResultCallback();

        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(mediaListFetcher_).fetchMediaListFromCamera(same(patient_));
    }

    @Test
    public void willDetermineWhichImagesNeedToBeDownloaded()
    {
        final ArrayList<DJIMedia> mediaList = new ArrayList<>();
        makeModeChangerCallOnResultCallback();
        makeMediaListFetcherCallOnSuccessCallback(mediaList);

        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(downloadSelector_).determineImagesForDownloadFromMediaList(same(mediaList));
    }

    @Test
    public void willDownloadSelectedImages()
    {
        final ArrayList<DJIMedia> mediaList = makeMediaList(3);
        ArrayList<DJIMedia> imagesToDownload = makeMediaList(3);
        makeModeChangerCallOnResultCallback();
        makeMediaListFetcherCallOnSuccessCallback(mediaList);
        when(downloadSelector_.determineImagesForDownloadFromMediaList(same(mediaList)))
                .thenReturn(imagesToDownload);

        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(imageDownloader_).downloadImagesFromDrone(
                same(imagesToDownload),
                same(patient_));
    }

    @Test
    public void willChangeCameraModeBackToShootPhotoMode()
    {
        final ArrayList<DJIMedia> mediaList = makeMediaList(3);
        ArrayList<DJIMedia> imagesToDownload = makeMediaList(3);
        makeModeChangerCallOnResultCallback();
        makeMediaListFetcherCallOnSuccessCallback(mediaList);
        when(downloadSelector_.determineImagesForDownloadFromMediaList(any(ArrayList.class)))
                .thenReturn(imagesToDownload);
        makeImageDownloaderCallOnResultCallback();

        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(modeChanger_).changeToShootPhotoMode(same(patient_));
    }

    @Test
    public void willCallEachServiceInOrder()
    {
        final ArrayList<DJIMedia> mediaList = makeMediaList(3);
        ArrayList<DJIMedia> imagesToDownload = makeMediaList(3);
        makeModeChangerCallOnResultCallback();
        makeMediaListFetcherCallOnSuccessCallback(mediaList);
        when(downloadSelector_.determineImagesForDownloadFromMediaList(any(ArrayList.class)))
                .thenReturn(imagesToDownload);
        makeImageDownloaderCallOnResultCallback();

        patient_.transferNewImagesFromDrone(completionCallback_);

        InOrder inOrder = inOrder(modeChanger_, mediaListFetcher_, downloadSelector_, imageDownloader_);
        inOrder.verify(modeChanger_).changeToMediaDownloadMode(same(patient_));
        inOrder.verify(mediaListFetcher_).fetchMediaListFromCamera(same(patient_));
        inOrder.verify(downloadSelector_).determineImagesForDownloadFromMediaList(any(ArrayList.class));
        inOrder.verify(imageDownloader_).downloadImagesFromDrone(any(ArrayList.class),same(patient_));
        inOrder.verify(modeChanger_).changeToShootPhotoMode(same(patient_));
    }

    private void makeModeChangerCallOnResultCallback()
    {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((I_CompletionCallback)args[0]).onResult(null);
                return null;
            }})
                .when(modeChanger_).changeToMediaDownloadMode(same(patient_));
    }

    private void makeMediaListFetcherCallOnSuccessCallback(
            final ArrayList<DJIMedia> mediaList)
    {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((I_CameraMediaListDownloadListener)args[0]).onSuccess(mediaList);
                return null;
            }})
                .when(mediaListFetcher_).fetchMediaListFromCamera(same(patient_));
    }

    private void makeImageDownloaderCallOnResultCallback()
    {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((I_CompletionCallback)args[1]).onResult(null);
                return null;
            }})
                .when(imageDownloader_).downloadImagesFromDrone(any(ArrayList.class), same(patient_));
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
