package com.dji.sdk.sample.common.integration;

import android.util.Log;

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.camera.DJIMediaManager;

/**
 * Created by Julia on 2017-02-21.
 */

public class MediaManagerSource implements I_MediaManagerSource
{
    private static final String TAG = "MediaManagerSource";

    @Override
    public I_MediaManager getMediaManager()
    {
        DJIBaseProduct product = DJISampleApplication.getProductInstance();
        if (product != null)
        {
            Log.d(TAG, "Product not null");

            DJICamera camera = product.getCamera();

            if(camera != null)
            {
                Log.d(TAG, "Camera not null");

                DJIMediaManager mediaManager = camera.getMediaManager();

                if(mediaManager != null)
                {
                    Log.d(TAG, "Media manager not null");

                    return new MediaManager(mediaManager);
                }
            }
        }
        return null;
    }
}
