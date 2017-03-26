package com.dji.sdk.sample.common.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.container.ImageTransferContainer;
import com.dji.sdk.sample.common.container.IntegrationLayerContainer;
import com.dji.sdk.sample.common.container.MissionContainer;
import com.dji.sdk.sample.common.container.PresenterContainer;
import com.dji.sdk.sample.common.container.UtilityContainer;
import com.dji.sdk.sample.common.view.src.FlightControlView;

import static com.dji.sdk.sample.common.utility.IntentExtraKeys.*;

public class FlightControlActivity extends FragmentActivity
{
    private FlightControlView flightControlView_;

    private UtilityContainer utilityContainer_;
    private IntegrationLayerContainer integrationLayerContainer_;
    private ImageTransferContainer imageTransferContainer_;
    private MissionContainer missionContainer_;
    private PresenterContainer presenterContainer_;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        Boolean isLiveModeEnabled = getIntent().getBooleanExtra(IS_LIVE_MODE_ENABLED, false);
        String pcIpAddress = getIntent().getStringExtra(PC_IP_ADDRESS);

        flightControlView_ = new FlightControlView(this);

        utilityContainer_ = new UtilityContainer(this);
        integrationLayerContainer_ = new IntegrationLayerContainer();
        imageTransferContainer_ = new ImageTransferContainer(
                utilityContainer_.contextManager(),
                utilityContainer_.missionStatusNotifier(),
                integrationLayerContainer_,
                pcIpAddress,
                isLiveModeEnabled);
        missionContainer_ = new MissionContainer(
                this,
                utilityContainer_.missionStatusNotifier(),
                utilityContainer_.applicationSettingsManager(),
                integrationLayerContainer_,
                imageTransferContainer_,
                isLiveModeEnabled);

        presenterContainer_ = new PresenterContainer(
                this,
                flightControlView_,
                missionContainer_,
                utilityContainer_.googleMapsConnectionHandler());

        setContentView(flightControlView_);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        utilityContainer_.googleMapsConnectionHandler().startConnection();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        utilityContainer_.googleMapsConnectionHandler().endConnection();
    }
}
