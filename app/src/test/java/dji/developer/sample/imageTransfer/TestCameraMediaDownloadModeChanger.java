package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.CameraMediaDownloadModeChanger;
import com.dji.sdk.sample.common.imageTransfer.I_CameraMediaListFetcher;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import dji.common.error.DJIError;

import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-02-12.
 */

public class TestCameraMediaDownloadModeChanger
{
    private I_MediaManagerSource mediaManagerSource_ = mock(I_MediaManagerSource.class);
    private I_CameraMediaListFetcher mediaListFetcher_ = mock(I_CameraMediaListFetcher.class);

    private CameraMediaDownloadModeChanger patient_ = new CameraMediaDownloadModeChanger(
            mediaManagerSource_,
            mediaListFetcher_);

    private I_MediaManager mediaManager_ = mock(I_MediaManager.class);

    @Test
    public void willRetrieveMediaMangerUsingTheSource()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.changeCameraModeForMediaDownload();

        verify(mediaManagerSource_).getMediaManager();
    }

    @Test
    public void willUseMediaManagerToChangeCameraModeToMediaDownload()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.changeCameraModeForMediaDownload();

        verify(mediaManager_).setCameraModeMediaDownload(patient_);
    }

    @Test
    public void willTriggerFetchOfCameraMediaListIfCameraModeWasChangedSuccessfully()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((I_CompletionCallback)args[0]).onResult(null);
                return null;
            }})
                .when(mediaManager_).setCameraModeMediaDownload(patient_);

        patient_.changeCameraModeForMediaDownload();

        verify(mediaListFetcher_).fetchMediaListFromCamera();
    }
}
