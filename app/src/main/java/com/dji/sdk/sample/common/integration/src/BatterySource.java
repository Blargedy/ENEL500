package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_Battery;
import com.dji.sdk.sample.common.integration.api.I_BatterySource;

/**
 * Created by Julia on 2017-03-23.
 */

public class BatterySource implements I_BatterySource
{
    @Override
    public I_Battery getBattery()
    {
        return new Battery(DJISampleApplication.getProductInstance().getBattery());
    }
}
