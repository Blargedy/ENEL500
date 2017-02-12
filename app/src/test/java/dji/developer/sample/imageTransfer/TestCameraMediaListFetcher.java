package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.CameraMediaListFetcher;
import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

import static org.mockito.Mockito.*;
import org.junit.Test;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public class TestCameraMediaListFetcher
{
    I_MediaManagerSource mediaManagerSource_ = mock(I_MediaManagerSource.class);
    I_CameraMediaListDownloadListener downloadListener_ = mock(I_CameraMediaListDownloadListener.class);

    I_MediaManager mediaManager_ = mock(I_MediaManager.class);

    CameraMediaListFetcher patient_ = new CameraMediaListFetcher(
            mediaManagerSource_,
            downloadListener_);

    @Test
    public void willRetrieveMediaMangerUsingTheSource()
    {
        ArrayList<DJIMedia> mediaList = patient_.fetchMediaListFromCamera();

        verify(mediaManagerSource_).getMediaManager();
    }

    @Test
    public void willRequestMediaListFromMediaManager()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        ArrayList<DJIMedia> mediaList = patient_.fetchMediaListFromCamera();

        verify(mediaManager_).fetchMediaList(downloadListener_);
    }
}
