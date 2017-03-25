package com.dji.sdk.sample.common.integration.api;

import dji.common.battery.DJIBatteryState;
import dji.sdk.battery.DJIBattery;

/**
 * Created by Julia on 2017-03-23.
 */

public interface I_BatteryStateUpdateCallback extends DJIBattery.DJIBatteryStateUpdateCallback
{
    void onResult(DJIBatteryState state);
    void resetIfWarningHasBeenShown();
}
