package com.dji.sdk.sample.common.presenter.src;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.activity.FlightControlActivity;
import com.dji.sdk.sample.common.container.IntegrationLayerContainer;
import com.dji.sdk.sample.common.imageTransfer.src.ImageTransferPathsSource;
import com.dji.sdk.sample.common.integration.api.I_Camera;
import com.dji.sdk.sample.common.mission.src.InvestigativeCameraSystemState;
import com.dji.sdk.sample.common.mission.src.InvestigativeWaypointReachedHandler;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;

/**
 * Created by Julia on 2017-02-08.
 */

public class MainMenuPresenter implements View.OnClickListener
{
    private Button reconstructLiveButton_;
    private Button reconstructLaterButton_;
    private Context context_;

    private IntegrationLayerContainer integrationLayerContainer_;
    private InvestigativeCameraSystemState cameraSystemState_;
    private ImageTransferPathsSource transferPathsSource_;
    private InvestigativeWaypointReachedHandler testing_;

    public MainMenuPresenter(
            Button reconstructLiveButton,
            Button reconstructLaterButton,
            Context context)
    {
        reconstructLiveButton_ = reconstructLiveButton;
        reconstructLiveButton_.setOnClickListener(this);

        reconstructLaterButton_ = reconstructLaterButton;
        reconstructLaterButton_.setOnClickListener(this);

        context_ = context;


        integrationLayerContainer_ = new IntegrationLayerContainer();
        cameraSystemState_ = new InvestigativeCameraSystemState();
        transferPathsSource_ = new ImageTransferPathsSource(new ApplicationContextManager(context));
        testing_ = new InvestigativeWaypointReachedHandler(
                integrationLayerContainer_.missionManagerSource(),
                integrationLayerContainer_.cameraSource(),
                cameraSystemState_,
                transferPathsSource_);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == reconstructLiveButton_.getId()) {
            // todo with Berni: Add screen with instructions and to test connection
//            Intent reconstructLiveIntent = new Intent(context_, FlightControlActivity.class);
//            reconstructLiveIntent.putExtra(IntentExtraKeys.IS_LIVE_MODE_ENABLED, new Boolean(true));
//            reconstructLiveIntent.putExtra(IntentExtraKeys.PC_IP_ADDRESS, "192.168.1.203");
//
//            context_.startActivity(reconstructLiveIntent);


            integrationLayerContainer_.cameraSource().getCamera().setCameraGeneratedNewMediaFileCallback(testing_);
            integrationLayerContainer_.cameraSource().getCamera().setDJICameraUpdatedSystemStateCallback(cameraSystemState_);


            testing_.notifyWaypointAtIndexHasBeenReached(0);

        }
        else if (view.getId() == reconstructLaterButton_.getId())
        {
            Intent reconstructLaterIntent = new Intent(context_, FlightControlActivity.class);
            reconstructLaterIntent.putExtra(IntentExtraKeys.IS_LIVE_MODE_ENABLED, new Boolean(false));

            context_.startActivity(reconstructLaterIntent);
        }
    }
}
