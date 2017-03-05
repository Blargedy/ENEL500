package com.dji.sdk.sample.common.mission.api;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-03-05.
 */

public interface I_MissionPreparer
{
    void prepareMission(I_CompletionCallback callback);
}
