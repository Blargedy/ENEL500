package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.CameraMediaDownloadModeChanger;
import com.dji.sdk.sample.common.integration.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-02-12.
 */

public class TestCameraMediaDownloadModeChanger
{
    private I_MediaManagerSource mediaManagerSource_ = mock(I_MediaManagerSource.class);

    private CameraMediaDownloadModeChanger patient_ = new CameraMediaDownloadModeChanger(
            mediaManagerSource_);

    private I_MediaManager mediaManager_ = mock(I_MediaManager.class);
    private I_CompletionCallback callback_ = mock(I_CompletionCallback.class);

    @Test
    public void willRetrieveMediaMangerUsingTheSource()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.changeCameraModeForMediaDownload(callback_);

        verify(mediaManagerSource_).getMediaManager();
    }

    @Test
    public void willUseMediaManagerToChangeCameraModeToMediaDownload()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.changeCameraModeForMediaDownload(callback_);

        verify(mediaManager_).setCameraModeMediaDownload(same(callback_));
    }
}
