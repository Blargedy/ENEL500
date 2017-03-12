package com.dji.sdk.sample.common.presenter.src;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dji.sdk.sample.common.activity.FlightControlActivity;
import com.dji.sdk.sample.common.container.ImageTransferContainer;
import com.dji.sdk.sample.common.container.IntegrationLayerContainer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferCompletionCallback;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferModuleInitializationCallback;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;

import java.util.ArrayList;

import dji.common.error.DJIError;
import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-02-08.
 */

public class MainMenuPresenter implements View.OnClickListener
{
    private Button reconstructLiveButton_;
    private Button reconstructLaterButton_;
    private I_ApplicationContextManager contextManager_;

    public MainMenuPresenter(
            Button reconstructLiveButton,
            Button reconstructLaterButton,
            I_ApplicationContextManager contextManager)
    {
        reconstructLiveButton_ = reconstructLiveButton;
        reconstructLiveButton_.setOnClickListener(this);

        reconstructLaterButton_ = reconstructLaterButton;
        reconstructLaterButton_.setOnClickListener(this);

        contextManager_ = contextManager;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == reconstructLiveButton_.getId()) {
            // todo with Berni: Add screen with instructions and to test connection
//            Context context = contextManager_.getApplicationContext();
//            Intent reconstructLiveIntent = new Intent(
//                    context, FlightControlActivity.class);
//            reconstructLiveIntent.putExtra(IntentExtraKeys.IS_LIVE_MODE_ENABLED, new Boolean(true));
//            reconstructLiveIntent.putExtra(IntentExtraKeys.PC_IP_ADDRESS, "192.168.1.203");
//
//            context.startActivity(reconstructLiveIntent);

            IntegrationLayerContainer integrationLayerContainer = new IntegrationLayerContainer();
            final ImageTransferContainer imageTransferContainer = new ImageTransferContainer(
                    contextManager_,
                    integrationLayerContainer,
                    "192.168.1.203",
                    true);

            imageTransferContainer
                    .imageTransferModuleInitializer().initializeImageTransferModulePriorToFlight(
                    new I_CompletionCallback() {
                        @Override
                        public void onResult(DJIError error) {
                            imageTransferContainer.droneMediaListInitializer().initializeDroneMediaList(new ArrayList<DJIMedia>());
                            imageTransferContainer.imageTransferer().transferNewImagesFromDrone(new I_ImageTransferCompletionCallback() {
                                @Override
                                public void onImageTransferCompletion() {
                                    Toast.makeText(contextManager_.getApplicationContext(), "Success: Image transfer complete",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
        }
        else if (view.getId() == reconstructLaterButton_.getId())
        {
            Intent reconstructLaterIntent = new Intent(
                    contextManager_.getApplicationContext(), FlightControlActivity.class);
            reconstructLaterIntent.putExtra(IntentExtraKeys.IS_LIVE_MODE_ENABLED, new Boolean(false));

            contextManager_.getApplicationContext().startActivity(reconstructLaterIntent);
        }
    }
}
