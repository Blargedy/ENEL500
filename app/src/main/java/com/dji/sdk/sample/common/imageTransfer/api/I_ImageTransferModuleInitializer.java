package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-02-21.
 */

public interface I_ImageTransferModuleInitializer
{
    void initializeImageTransferModulePriorToFlight(I_CompletionCallback callback);
}
