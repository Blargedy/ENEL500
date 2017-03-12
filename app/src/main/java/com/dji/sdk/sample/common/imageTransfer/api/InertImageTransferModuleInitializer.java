package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleInitializationCallback;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-03-05.
 */

public class InertImageTransferModuleInitializer implements I_ImageTransferModuleInitializer
{
    @Override
    public void initializeImageTransferModulePriorToFlight(I_CompletionCallback callback)
    {
        if (callback != null)
        {
            callback.onResult(null);
        }
    }
}
