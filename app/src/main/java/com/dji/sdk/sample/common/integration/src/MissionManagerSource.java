package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;

/**
 * Created by eric7 on 2017-02-21.
 */

public class MissionManagerSource implements I_MissionManagerSource
{
    @Override
    public I_MissionManager getMissionManager()
    {
        return new MissionManager(DJISampleApplication.getProductInstance().getMissionManager());
    }
}
