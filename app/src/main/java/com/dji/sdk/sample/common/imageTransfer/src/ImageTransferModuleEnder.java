package com.dji.sdk.sample.common.imageTransfer.src;

import android.util.Log;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferModuleEnder;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferCompletionCallback;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleEndCompletionCallback;

/**
 * Created by Julia on 2017-03-05.
 */

public class ImageTransferModuleEnder implements
        I_ImageTransferModuleEnder,
        I_ImageTransferCompletionCallback
{
    private static final String TAG = "ImageTransferModuleEnder";


    private I_ImageTransferer droneToAndroidImageTransferer_;
    private AndroidToPcImageCopier androidToPcImageCopier_;

    private I_ImageTransferModuleEndCompletionCallback callback_;

    public ImageTransferModuleEnder(
            I_ImageTransferer droneToAndroidImageTransferer,
            AndroidToPcImageCopier androidToPcImageCopier)
    {
        droneToAndroidImageTransferer_ = droneToAndroidImageTransferer;
        androidToPcImageCopier_ = androidToPcImageCopier;
    }

    @Override
    public void endImageTransfer(I_ImageTransferModuleEndCompletionCallback callback)
    {
        callback_ = callback;
        droneToAndroidImageTransferer_.transferNewImagesFromDrone(this);
    }

    @Override
    public void onImageTransferCompletion()
    {
        androidToPcImageCopier_.interrupt();

        try {
            androidToPcImageCopier_.join();
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }

        callback_.onEndImageTransferCompletion();
    }
}