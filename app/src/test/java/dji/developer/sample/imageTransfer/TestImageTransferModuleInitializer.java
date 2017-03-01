package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.I_DroneMediaListInitializer;
import com.dji.sdk.sample.common.imageTransfer.ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;
import com.dji.sdk.sample.common.integration.I_MediaManager;
import com.dji.sdk.sample.common.integration.I_MediaManagerSource;

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
    private I_MediaManagerSource mediaManagerSource_ = mock(I_MediaManagerSource.class);
    private I_DroneMediaListInitializer mediaListInitializer_ = mock(I_DroneMediaListInitializer.class);

    private ImageTransferModuleInitializer patient_ = new ImageTransferModuleInitializer(
            mediaManagerSource_,
            mediaListInitializer_);

    private I_MediaManager mediaManager_ = mock(I_MediaManager.class);

    @Test
    public void willFetchMediaListFromCamera()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.initalizeImageTransferModulePriorToFlight();

        verify(mediaManager_).fetchMediaList(patient_);
    }

    @Test
    public void willInitializeDroneMediaListAfterMediaListHasBeenFetched()
    {
        final ArrayList<DJIMedia> mediaList = new ArrayList<>();
        makeMediaManagerCallOnSuccessCallbackWithMediaList(mediaList);
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.initalizeImageTransferModulePriorToFlight();

        verify(mediaListInitializer_).initializeDroneMediaList(same(mediaList));
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
}
