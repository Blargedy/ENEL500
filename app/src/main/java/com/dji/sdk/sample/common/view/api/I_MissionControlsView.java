package com.dji.sdk.sample.common.view.api;

import android.widget.Button;
import android.widget.ToggleButton;

/**
 * Created by Julia on 2017-03-08.
 */

public interface I_MissionControlsView
{
    Button acceptAreaButton();
    Button startMissionButton();
    Button cancelButton();
    ToggleButton hoverNowToggleButton();
    Button settingsButton();
}
