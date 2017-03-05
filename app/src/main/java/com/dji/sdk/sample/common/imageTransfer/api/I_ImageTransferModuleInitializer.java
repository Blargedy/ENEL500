package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleInitializationCallback;

/**
 * Created by Julia on 2017-02-21.
 */

public interface I_ImageTransferModuleInitializer
{
    void initializeImageTransferModulePriorToFlight(
            I_ImageTransferModuleInitializationCallback callback);
}
