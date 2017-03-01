package com.dji.sdk.sample.common.imageTransfer;

/**
 * Created by Julia on 2017-03-01.
 */

public class AndroidToPcImageCopier implements I_AndroidToPcImageCopier
{
    private I_ImageTransferPathsSource pathsSource_;

    public AndroidToPcImageCopier(
            I_ImageTransferPathsSource pathsSource)
    {
        pathsSource_ = pathsSource;
    }


    @Override
    public void copyImageToPc(String androidImagePath)
    {

    }
}
