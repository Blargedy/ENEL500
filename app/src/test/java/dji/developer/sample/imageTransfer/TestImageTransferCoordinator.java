package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadQueuer;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferCoordinator;
import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import org.junit.Test;
import org.mockito.InOrder;
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
    private I_CameraSource cameraSource_ = mock(I_CameraSource.class);
    private I_DroneImageDownloadQueuer downloadQueuer_ = mock(I_DroneImageDownloadQueuer.class);
    private I_DroneToAndroidImageDownloader imageDownloader_ = mock(I_DroneToAndroidImageDownloader.class);

    private ImageTransferCoordinator patient_ = new ImageTransferCoordinator(
            cameraSource_,
            downloadQueuer_,
            imageDownloader_);

    private I_CompletionCallback completionCallback_ = mock(I_CompletionCallback.class);
    private I_Camera camera_ = mock(I_Camera.class);

    @Test
    public void willChangeCameraModeToMediaDownloadMode()
    {
        when(cameraSource_.getCamera()).thenReturn(camera_);

        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(camera_).setCameraMode(eq(I_Camera.CameraMode.MEDIA_DOWNLOAD), same(patient_));
    }

    @Test
    public void willDetermineWhichImagesNeedToBeDownloaded()
    {
        when(cameraSource_.getCamera()).thenReturn(camera_);
        makeCameraCallOnResultCallback();

        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(downloadQueuer_).getListOfImagesToDownload();
    }

    @Test
    public void willDownloadSelectedImages()
    {
        ArrayList<DJIMedia> imagesToDownload = makeMediaList(3);
        when(cameraSource_.getCamera()).thenReturn(camera_);
        makeCameraCallOnResultCallback();
        when(downloadQueuer_.getListOfImagesToDownload()).thenReturn(imagesToDownload);

        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(imageDownloader_).downloadImagesFromDrone(
                same(imagesToDownload),
                same(patient_));
    }

    @Test
    public void willChangeCameraModeBackToShootPhotoMode()
    {
        ArrayList<DJIMedia> imagesToDownload = makeMediaList(3);
        when(cameraSource_.getCamera()).thenReturn(camera_);
        makeCameraCallOnResultCallback();
        when(downloadQueuer_.getListOfImagesToDownload()).thenReturn(imagesToDownload);
        makeImageDownloaderCallOnResultCallback();

        patient_.transferNewImagesFromDrone(completionCallback_);

        verify(camera_).setCameraMode(eq(I_Camera.CameraMode.SHOOT_PHOTO), same(patient_));
    }

    @Test
    public void willCallEachServiceInOrder()
    {
        ArrayList<DJIMedia> imagesToDownload = makeMediaList(3);
        when(cameraSource_.getCamera()).thenReturn(camera_);
        makeCameraCallOnResultCallback();
        when(downloadQueuer_.getListOfImagesToDownload()).thenReturn(imagesToDownload);
        makeImageDownloaderCallOnResultCallback();

        patient_.transferNewImagesFromDrone(completionCallback_);

        InOrder inOrder = inOrder(camera_, downloadQueuer_, imageDownloader_);
        inOrder.verify(camera_).setCameraMode(eq(I_Camera.CameraMode.MEDIA_DOWNLOAD), same(patient_));
        inOrder.verify(downloadQueuer_).getListOfImagesToDownload();
        inOrder.verify(imageDownloader_).downloadImagesFromDrone(any(ArrayList.class),same(patient_));
        inOrder.verify(camera_).setCameraMode(eq(I_Camera.CameraMode.SHOOT_PHOTO),same(patient_));
    }

    private void makeCameraCallOnResultCallback()
    {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((I_CompletionCallback)args[1]).onResult(null);
                return null;
            }})
                .when(camera_).setCameraMode(
                        any(I_Camera.CameraMode.class),
                        any(I_CompletionCallback.class));
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
