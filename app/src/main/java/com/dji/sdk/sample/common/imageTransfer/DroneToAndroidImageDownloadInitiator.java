package com.dji.sdk.sample.common.imageTransfer;

import android.util.Log;

/**
 * Created by Julia on 2017-02-12.
 */

public class DroneToAndroidImageDownloadInitiator implements I_ImageTransferer
{
    private I_CameraMediaDownloadModeChanger modeChanger_;

    public DroneToAndroidImageDownloadInitiator(I_CameraMediaDownloadModeChanger modeChanger)
    {
        modeChanger_ = modeChanger;
    }

    @Override
    public void transferNewImagesFromDrone()
    {
        modeChanger_.changeCameraModeForMediaDownload();
    }
}
