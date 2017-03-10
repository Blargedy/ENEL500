package com.dji.sdk.sample.common.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.container.ImageTransferContainer;
import com.dji.sdk.sample.common.container.IntegrationLayerContainer;
import com.dji.sdk.sample.common.container.MissionContainer;
import com.dji.sdk.sample.common.container.PresenterContainer;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.GoogleMapsConnectionHandler;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.src.FlightControlView;

import static com.dji.sdk.sample.common.utility.IntentExtraKeys.*;

public class FlightControlActivity extends FragmentActivity
{
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;
    private GoogleMapsConnectionHandler googleMapsConnectionHandler_;

    private FlightControlView flightControlView_;

    private IntegrationLayerContainer integrationLayerContainer_;
    private ImageTransferContainer imageTransferContainer_;
    private MissionContainer missionContainer_;
    private PresenterContainer presenterContainer_;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Boolean isLiveModeEnabled = getIntent().getBooleanExtra(IS_LIVE_MODE_ENABLED, false);
        String pcIpAddress = getIntent().getStringExtra(PC_IP_ADDRESS);

        permissionRequester_ = new UserPermissionRequester();
        permissionRequester_.requestPermissions(this);
        contextManager_ = new ApplicationContextManager(this);
        googleMapsConnectionHandler_ = new GoogleMapsConnectionHandler(this);

        flightControlView_ = new FlightControlView(this);

        integrationLayerContainer_ = new IntegrationLayerContainer();
        imageTransferContainer_ = new ImageTransferContainer(
                contextManager_,
                integrationLayerContainer_,
                pcIpAddress,
                isLiveModeEnabled);
        missionContainer_ = new MissionContainer(
                integrationLayerContainer_,
                imageTransferContainer_,
                this);

        presenterContainer_ = new PresenterContainer(
                this,
                flightControlView_,
                missionContainer_,
                googleMapsConnectionHandler_);

        setContentView(flightControlView_);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        googleMapsConnectionHandler_.startConnection();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        googleMapsConnectionHandler_.endConnection();
    }
}
