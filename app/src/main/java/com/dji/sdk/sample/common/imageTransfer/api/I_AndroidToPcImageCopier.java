package com.dji.sdk.sample.common.imageTransfer.api;

/**
 * Created by Julia on 2017-03-01.
 */

public interface I_AndroidToPcImageCopier extends Runnable
{
    void addImageToPcCopyQueue(String androidImagePath);
}
