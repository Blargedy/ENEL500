package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleEndCompletionCallback;
import com.dji.sdk.sample.common.imageTransfer.src.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferModuleEnder;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-03-05.
 */

public class TestImageTransferModuleEnder
{
    private I_ImageTransferer droneToAndroidImageTransferer_ = mock(I_ImageTransferer.class);
    private AndroidToPcImageCopier androidToPcImageCopier_ = mock(AndroidToPcImageCopier.class);

    private ImageTransferModuleEnder patient_ = new ImageTransferModuleEnder(
            droneToAndroidImageTransferer_,
            androidToPcImageCopier_);

    private I_ImageTransferModuleEndCompletionCallback callback_ = mock(I_ImageTransferModuleEndCompletionCallback.class);

    @Test
    public void willTransferAnyLastImagesOnTheDroneToTheAndroidDevice()
    {
        patient_.endImageTransfer(callback_);

        verify(droneToAndroidImageTransferer_).transferNewImagesFromDrone(same(patient_));
    }

    @Test
    public void willInterruptAndJoinBackgroundCopierThreadBeforeCallingCallback()
    {
        makeImageTransfererCallOnImageTransferCompletionCallback();

        patient_.endImageTransfer(callback_);

        InOrder inOrder = inOrder(androidToPcImageCopier_, callback_);
        inOrder.verify(androidToPcImageCopier_).interrupt();
        inOrder.verify(callback_).onEndImageTransferCompletion();
    }

    private void makeImageTransfererCallOnImageTransferCompletionCallback()
    {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((I_CompletionCallback)args[0]).onResult(null);
                return null;
            }})
                .when(droneToAndroidImageTransferer_).transferNewImagesFromDrone(same(patient_));
    }
}
