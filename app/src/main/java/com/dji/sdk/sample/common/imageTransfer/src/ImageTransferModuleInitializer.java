package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_CameraModeChanger;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferModuleInitializer implements
        I_ImageTransferModuleInitializer,
        I_CompletionCallback
{
    private static final String TAG = "ImageTransferModuleInitializer";

    private I_CameraModeChanger modeChanger_;
    private AndroidToPcImageCopier androidToPcImageCopier_;

    private I_CompletionCallback callback_;

    public ImageTransferModuleInitializer(
            I_CameraModeChanger modeChanger,
            AndroidToPcImageCopier androidToPcImageCopier)
    {
        modeChanger_ = modeChanger;
        androidToPcImageCopier_ = androidToPcImageCopier;
    }

    @Override
    public void initializeImageTransferModulePriorToFlight(I_CompletionCallback callback)
    {
        callback_ = callback;
        androidToPcImageCopier_.start();
        modeChanger_.changeToMediaDownloadMode(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error != null)
        {
            callback_.onResult(error);
        }
    }
}
