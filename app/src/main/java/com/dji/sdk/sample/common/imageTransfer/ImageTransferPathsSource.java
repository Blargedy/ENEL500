package com.dji.sdk.sample.common.imageTransfer;

import android.os.Environment;

import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import java.io.File;

/**
 * Created by Julia on 2017-02-21.
 */

public class ImageTransferPathsSource implements I_ImageTransferPathsSource
{
    private I_ApplicationContextManager contextManager_;

    public ImageTransferPathsSource(
            I_ApplicationContextManager contextManager)
    {
        contextManager_ = contextManager;
    }

    @Override
    public File androidDeviceImagePath()
    {
        return contextManager_.getApplicationContext()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }
}
