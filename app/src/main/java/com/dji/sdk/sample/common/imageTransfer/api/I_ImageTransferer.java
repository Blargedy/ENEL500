package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferCompletionCallback;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_ImageTransferer
{
    void transferNewImagesFromDrone(I_ImageTransferCompletionCallback callback);
}
