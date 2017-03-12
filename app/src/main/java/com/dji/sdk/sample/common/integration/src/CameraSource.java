package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;

/**
 * Created by Julia on 2017-03-12.
 */

public class CameraSource implements I_CameraSource
{
    @Override
    public I_Camera getCamera()
    {
        return new Camera(DJISampleApplication.getProductInstance().getCamera());
    }
}
