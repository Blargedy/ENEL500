package com.dji.sdk.sample.common.imageTransfer;

import com.dji.sdk.sample.common.integration.I_CameraMediaListDownloadListener;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_ImageTransferer
{
    void transferNewImagesFromDrone(I_ImageTransferCompletionCallback callback);
}
