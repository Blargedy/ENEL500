package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.src.CameraModeChanger;
import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_MediaManager;
import com.dji.sdk.sample.common.integration.api.I_MediaManagerSource;

import org.junit.Test;

import dji.common.camera.DJICameraSettingsDef;

import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-02-12.
 */

public class TestCameraModeChanger
{
    private I_MediaManagerSource mediaManagerSource_ = mock(I_MediaManagerSource.class);
    private I_CameraSource cameraSource_ = mock(I_CameraSource.class);

    private CameraModeChanger patient_ = new CameraModeChanger(
            mediaManagerSource_,
            cameraSource_);

    private I_MediaManager mediaManager_ = mock(I_MediaManager.class);
    private I_Camera camera_ = mock(I_Camera.class);
    private I_CompletionCallback callback_ = mock(I_CompletionCallback.class);

    @Test
    public void willRetrieveMediaMangerUsingTheSourceBeforeChangingToMediaDownloadMode()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.changeToMediaDownloadMode(callback_);

        verify(mediaManagerSource_).getMediaManager();
    }

    @Test
    public void willUseMediaManagerToChangeCameraModeToMediaDownload()
    {
        when(mediaManagerSource_.getMediaManager()).thenReturn(mediaManager_);

        patient_.changeToMediaDownloadMode(callback_);

        verify(mediaManager_).setCameraModeMediaDownload(same(callback_));
    }

    @Test
    public void willRetrieveCameraUsingTheSourceBeforeChangingToShootPhotoMode()
    {
        when(cameraSource_.getCamera()).thenReturn(camera_);

        patient_.changeToShootPhotoMode(callback_);

        verify(cameraSource_).getCamera();
    }

    @Test
    public void willUseCameraToChangeCameraModeToShootPhotoMode()
    {
        when(cameraSource_.getCamera()).thenReturn(camera_);

        patient_.changeToShootPhotoMode(callback_);

        verify(camera_).setCameraMode(eq(I_Camera.CameraMode.SHOOT_PHOTO), same(callback_));
    }
}
