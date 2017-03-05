package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleEndCompletionCallback;

/**
 * Created by Julia on 2017-03-05.
 */

public interface I_ImageTransferModuleEnder
{
    void endImageTransfer(I_ImageTransferModuleEndCompletionCallback callback);
}
