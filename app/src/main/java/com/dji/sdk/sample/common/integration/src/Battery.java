package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_Battery;
import com.dji.sdk.sample.common.integration.api.I_BatteryStateUpdateCallback;

import dji.sdk.battery.DJIBattery;

/**
 * Created by Julia on 2017-03-23.
 */

public class Battery implements I_Battery
{
    private DJIBattery battery_;

    public Battery(DJIBattery battery)
    {
        battery_ = battery;
    }

    @Override
    public void setBatteryStateUpdateCallback(I_BatteryStateUpdateCallback callback)
    {
        battery_.setBatteryStateUpdateCallback(callback);
    }
}
