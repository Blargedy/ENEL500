package com.dji.sdk.sample.common.imageTransfer;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_CameraMediaListFetcher
{
    ArrayList<DJIMedia> fetchMediaListFromCamera();
}
