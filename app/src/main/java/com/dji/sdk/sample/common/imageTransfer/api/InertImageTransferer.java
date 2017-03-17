package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-03-05.
 */

public class InertImageTransferer implements I_ImageTransferer
{
    @Override
    public void transferNewImagesFromDrone(I_CompletionCallback callback)
    {
        callback.onResult(null);
    }
}
