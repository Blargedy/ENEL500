package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.DroneToAndroidImageDownloadInitiator;
import com.dji.sdk.sample.common.imageTransfer.I_CameraMediaDownloadModeChanger;

import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-02-12.
 */

public class TestDroneToAndroidImageDownloadInitiator
{
    private I_CameraMediaDownloadModeChanger modeChanger_ = mock(I_CameraMediaDownloadModeChanger.class);

    private DroneToAndroidImageDownloadInitiator patient_ = new DroneToAndroidImageDownloadInitiator(
            modeChanger_);

    @Test
    public void willInitiateImageDownloadByChangingTheCameraMode()
    {
        patient_.transferNewImagesFromDrone();

        verify(modeChanger_).changeCameraModeForMediaDownload();
    }
}
