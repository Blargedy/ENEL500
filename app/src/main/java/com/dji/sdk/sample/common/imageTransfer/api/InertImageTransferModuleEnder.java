package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleEndCompletionCallback;

/**
 * Created by Julia on 2017-03-05.
 */

public class InertImageTransferModuleEnder implements I_ImageTransferModuleEnder
{
    @Override
    public void endImageTransfer(I_ImageTransferModuleEndCompletionCallback callback)
    {
        if (callback != null)
        {
            callback.onEndImageTransferCompletion();
        }
    }
}
