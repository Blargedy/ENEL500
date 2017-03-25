package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_Gimbal;
import com.dji.sdk.sample.common.integration.api.I_GimbalSource;

/**
 * Created by Julia on 2017-03-25.
 */

public class GimbalSource implements I_GimbalSource
{
    @Override
    public I_Gimbal getGimbal()
    {
        return new Gimbal(DJISampleApplication.getProductInstance().getGimbal());
    }
}
