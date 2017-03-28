package com.dji.sdk.sample.common.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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

        if (isLargeDevice(getBaseContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        Boolean isLiveModeEnabled = getIntent().getBooleanExtra(IS_LIVE_MODE_ENABLED, false);

        flightControlView_ = new FlightControlView(this);

        utilityContainer_ = new UtilityContainer(this);
        integrationLayerContainer_ = new IntegrationLayerContainer();
        imageTransferContainer_ = new ImageTransferContainer(
                utilityContainer_.contextManager(),
                utilityContainer_.missionStatusNotifier(),
                integrationLayerContainer_,
                utilityContainer_,
                isLiveModeEnabled);
        missionContainer_ = new MissionContainer(
                this,
                utilityContainer_.missionStatusNotifier(),
                utilityContainer_.applicationSettingsManager(),
                integrationLayerContainer_,
                imageTransferContainer_);

        presenterContainer_ = new PresenterContainer(
                this,
                flightControlView_,
                missionContainer_,
                utilityContainer_.googleMapsConnectionHandler());

        setContentView(flightControlView_);
    }


    private boolean isLargeDevice(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return false;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return true;
            default:
                return false;
        }
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
