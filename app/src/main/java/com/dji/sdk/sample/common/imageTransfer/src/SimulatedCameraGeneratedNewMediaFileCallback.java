package com.dji.sdk.sample.common.imageTransfer.src;

import com.dji.sdk.sample.common.imageTransfer.api.I_AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferPathsSource;
import com.dji.sdk.sample.common.integration.api.I_CameraGeneratedNewMediaFileCallback;

import java.io.File;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-03-26.
 */

public class SimulatedCameraGeneratedNewMediaFileCallback
        implements I_CameraGeneratedNewMediaFileCallback
{
    static private final int STARTING_INDEX = 263;
    static private final int FINAL_INDEX = 359;
    static private final String FILENAME_PREFIX = "DJI_0";

    private I_ImageTransferPathsSource pathsSource_;
    private I_AndroidToPcImageCopier androidToPcImageCopier_;

    private int currentIndex_;

    public SimulatedCameraGeneratedNewMediaFileCallback(
            I_ImageTransferPathsSource pathsSource,
            I_AndroidToPcImageCopier androidToPcImageCopier)
    {
        pathsSource_ = pathsSource;
        androidToPcImageCopier_ = androidToPcImageCopier;
        reset();
    }

    @Override
    public void onResult(DJIMedia media)
    {
        if (currentIndex_ <= FINAL_INDEX)
        {
            String filename = FILENAME_PREFIX + currentIndex_ + ".jpg";
            File image = new File(pathsSource_.androidDeviceImagePath(), filename);
            androidToPcImageCopier_.addImageToPcCopyQueue(image.getAbsolutePath());
            currentIndex_++;
        }
    }

    @Override
    public void reset()
    {
        currentIndex_ = STARTING_INDEX;
    }
}
