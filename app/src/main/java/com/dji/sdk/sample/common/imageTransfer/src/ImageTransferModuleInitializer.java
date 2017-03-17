package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferModuleInitializer implements
        I_ImageTransferModuleInitializer
{
    private static final String TAG = "ImageTransferModuleInitializer";

    private AndroidToPcImageCopier androidToPcImageCopier_;


    public ImageTransferModuleInitializer(
            AndroidToPcImageCopier androidToPcImageCopier)
    {
        androidToPcImageCopier_ = androidToPcImageCopier;
    }

    @Override
    public void initializeImageTransferModulePriorToFlight(I_CompletionCallback callback)
    {
        androidToPcImageCopier_.start();
    }
}
