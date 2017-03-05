package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.src.CameraMediaListFetcher;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneImageDownloadSelector;
import com.dji.sdk.sample.common.imageTransfer.api.I_DroneToAndroidImageDownloader;
import com.dji.sdk.sample.common.integration.api.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.api.I_MediaManager;
import com.dji.sdk.sample.common.integration.api.I_MediaManagerSource;

import static org.mockito.Mockito.*;
import org.junit.Test;

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
    private I_CameraMediaListDownloadListener listener = mock(I_CameraMediaListDownloadListener.class);

    @Test
    public void willRetrieveMediaMangerUsingMediaManagerSource()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.fetchMediaListFromCamera(listener);

        verify(mediaManagerSource_).getMediaManager();
    }

    @Test
    public void willFetchMediaListUsingMediaManager()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.fetchMediaListFromCamera(listener);

        verify(mediaManager_).fetchMediaList(same(listener));
    }
}
