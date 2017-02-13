package com.dji.sdk.sample.common.imageTransfer;

import com.dji.sdk.sample.common.integration.I_CompletionCallback;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_CameraMediaListFetcher
{
    void fetchMediaListFromCamera();
}
