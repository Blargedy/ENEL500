package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.api.I_CameraMediaDownloadModeChanger;
import com.dji.sdk.sample.common.imageTransfer.api.I_CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializationCallback;
import com.dji.sdk.sample.common.imageTransfer.src.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneMediaListInitializer;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-02-21.
 */

public class TestImageTransferModuleInitializer
{
    private I_CameraMediaDownloadModeChanger modeChanger_ = mock(I_CameraMediaDownloadModeChanger.class);
    private I_CameraMediaListFetcher mediaListFetcher_ = mock(I_CameraMediaListFetcher.class);
    private I_DroneMediaListInitializer mediaListInitializer_ = mock(I_DroneMediaListInitializer.class);
    private AndroidToPcImageCopier androidToPcImageCopier_ = mock(AndroidToPcImageCopier.class);

    private ImageTransferModuleInitializer patient_ = new ImageTransferModuleInitializer(
            modeChanger_,
            mediaListFetcher_,
            mediaListInitializer_,
            androidToPcImageCopier_);

    private I_ImageTransferModuleInitializationCallback callback_ = mock(I_ImageTransferModuleInitializationCallback.class);

    @Test
    public void willStartTheAndroidToPCImageCopierBackgroundThread()
    {
        patient_.initializeImageTransferModulePriorToFlight(callback_);

        verify(androidToPcImageCopier_).start();
    }

    @Test
    public void willChangeCameraModeToMediaDownloadMode()
    {
        patient_.initializeImageTransferModulePriorToFlight(callback_);

        verify(modeChanger_).changeCameraModeForMediaDownload(patient_);
    }

    @Test
    public void willFetchMediaListAfterChangingCameraMode()
    {
        makeModeChangerCallOnResultCallback();

        patient_.initializeImageTransferModulePriorToFlight(callback_);

        verify(mediaListFetcher_).fetchMediaListFromCamera(same(patient_));
    }

    @Test
    public void willInitializeDroneMediaListAfterMediaListHasBeenFetched()
    {
        final ArrayList<DJIMedia> mediaList = new ArrayList<>();
        makeModeChangerCallOnResultCallback();
        makeMediaListFetcherCallOnSuccessCallback(mediaList);

        patient_.initializeImageTransferModulePriorToFlight(callback_);

        verify(mediaListInitializer_).initializeDroneMediaList(same(mediaList));
    }

    @Test
    public void willCallInitializationCompletionCallback()
    {
        final ArrayList<DJIMedia> mediaList = new ArrayList<>();
        makeModeChangerCallOnResultCallback();
        makeMediaListFetcherCallOnSuccessCallback(mediaList);

        patient_.initializeImageTransferModulePriorToFlight(callback_);

        verify(callback_).onImageTransferModuleInitializationCompletion();
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
}
