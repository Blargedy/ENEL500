package com.dji.sdk.sample.common.integration.api;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_CompletionCallback extends DJICommonCallbacks.DJICompletionCallback
{
    void onResult(DJIError error);
}
