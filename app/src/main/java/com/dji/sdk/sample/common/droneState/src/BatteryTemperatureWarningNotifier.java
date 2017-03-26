package com.dji.sdk.sample.common.droneState.src;

import com.dji.sdk.sample.common.integration.api.I_BatteryStateUpdateCallback;
import com.dji.sdk.sample.common.utility.I_MissionStatusNotifier;

import dji.common.battery.DJIBatteryState;

/**
 * Created by Julia on 2017-03-23.
 */

public class BatteryTemperatureWarningNotifier implements I_BatteryStateUpdateCallback
{
    private static final int BATTERY_TEMPERATURE_THRESHOLD = 15;
    private static final String BATTERY_TEMPERATURE_WARNING_MESSAGE = "Warning: Battery temperature is below 15 degrees celsius";
    private I_MissionStatusNotifier missionStatusNotifier_;
    private boolean hasErrorBeenShown_;

    public BatteryTemperatureWarningNotifier(
            I_MissionStatusNotifier missionStatusNotifier)
    {
        missionStatusNotifier_ = missionStatusNotifier;
        hasErrorBeenShown_ = false;
    }

    @Override
    public void onResult(DJIBatteryState state)
    {
        if(!hasErrorBeenShown_)
        {
            if (state.getBatteryTemperature() < BATTERY_TEMPERATURE_THRESHOLD)
            {
                missionStatusNotifier_.notifyStatusChanged(BATTERY_TEMPERATURE_WARNING_MESSAGE);
                hasErrorBeenShown_ = true;
            }
        }
    }

    @Override
    public void resetIfWarningHasBeenShown()
    {
        hasErrorBeenShown_ = false;
    }
}
