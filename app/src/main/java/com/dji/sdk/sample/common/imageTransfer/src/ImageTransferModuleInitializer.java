package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;

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
    public void initializeImageTransferModule()
    {
        androidToPcImageCopier_.start();
    }
}
