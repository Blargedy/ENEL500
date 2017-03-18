package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-03-05.
 */

public class InertImageTransferModuleEnder implements I_ImageTransferModuleEnder
{
    @Override
    public void endImageTransfer(I_CompletionCallback callback)
    {
        if (callback != null)
        {
            callback.onResult(null);
        }
    }
}
