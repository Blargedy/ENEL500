package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.I_CameraMediaDownloadModeChanger;
import com.dji.sdk.sample.common.imageTransfer.I_CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.I_DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferCompletionCallback;
import com.dji.sdk.sample.common.imageTransfer.ImageTransferCoordinator;
import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-03-05.
 */

public class TestImageTransferCoordinator
{
    private I_CameraMediaDownloadModeChanger modeChanger_ = mock(I_CameraMediaDownloadModeChanger.class);
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

        verify(modeChanger_).changeCameraModeForMediaDownload(patient_);
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
                same(completionCallback_));
    }

    private void makeModeChangerCallOnResultCallback()
    {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((I_CompletionCallback)args[0]).onResult(null);
                return null;
            }})
                .when(modeChanger_).changeCameraModeForMediaDownload(same(patient_));
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
