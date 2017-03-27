package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleEnder;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferModuleInitializer implements
        I_ImageTransferModuleInitializer,
        I_ImageTransferModuleEnder
{
    private AndroidToPcImageCopier androidToPcImageCopier_;
    private Thread imageCopierThread_;

    public ImageTransferModuleInitializer(
            AndroidToPcImageCopier androidToPcImageCopier)
    {
        androidToPcImageCopier_ = androidToPcImageCopier;
    }

    @Override
    public void initializeImageTransferModule()
    {
        imageCopierThread_ = new Thread(androidToPcImageCopier_);
        imageCopierThread_.start();
    }

    @Override
    public void endImageTransfer(I_CompletionCallback callback)
    {
        imageCopierThread_.interrupt();

        if(callback != null)
        {
            callback.onResult(null);
        }
    }
}
