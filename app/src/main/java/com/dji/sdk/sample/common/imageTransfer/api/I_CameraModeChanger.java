package com.dji.sdk.sample.common.imageTransfer.api;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

/**
 * Created by Julia on 2017-02-12.
 */

public interface I_CameraModeChanger
{
    void changeToMediaDownloadMode(I_CompletionCallback callback);
    void changeToShootPhotoMode(I_CompletionCallback callback);
}
