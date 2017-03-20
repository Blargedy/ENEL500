package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.src.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferModuleInitializer;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-02-21.
 */

public class TestImageTransferModuleInitializer
{
    private AndroidToPcImageCopier androidToPcImageCopier_ = mock(AndroidToPcImageCopier.class);

    private ImageTransferModuleInitializer patient_ = new ImageTransferModuleInitializer(
            androidToPcImageCopier_);

    private I_CompletionCallback callback_ = mock(I_CompletionCallback.class);

//    @Test
//    public void willStartTheAndroidToPCImageCopierBackgroundThread()
//    {
//        patient_.initializeImageTransferModulePriorToFlight(callback_);
//
//        verify(androidToPcImageCopier_).start();
//    }
}
