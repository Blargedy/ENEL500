package com.dji.sdk.sample.common.imageTransfer.api;

/**
 * Created by Julia on 2017-03-05.
 */

public class InertImageTransferer implements I_ImageTransferer
{
    @Override
    public void transferNewImagesFromDrone(I_ImageTransferCompletionCallback callback)
    {
        callback.onImageTransferCompletion();
    }
}
