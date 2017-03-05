package com.dji.sdk.sample.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dji.sdk.sample.common.container.ImageTransferContainer;
import com.dji.sdk.sample.common.container.IntegrationLayerContainer;
import com.dji.sdk.sample.common.container.MissionContainer;
import com.dji.sdk.sample.common.presenter.ProductConnectionPresenter;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.FlightControlView;

import static com.dji.sdk.sample.common.utility.IntentExtraKeys.*;

public class FlightControlActivity extends AppCompatActivity
{
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;

    private FlightControlView flightControlView_;

    private IntegrationLayerContainer integrationLayerContainer_;

    private ImageTransferContainer imageTransferContainer_;
    private MissionContainer missionContainer_;

    private ProductConnectionPresenter productConnectionPresenter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean isLiveModeEnabled = getIntent().getBooleanExtra(IS_LIVE_MODE_ENABLED, false);
        String pcIpAddress = getIntent().getStringExtra(PC_IP_ADDRESS);

        permissionRequester_ = new UserPermissionRequester();
        contextManager_ = new ApplicationContextManager(this);

        flightControlView_ = new FlightControlView(this);

        integrationLayerContainer_ = new IntegrationLayerContainer();

        imageTransferContainer_ = new ImageTransferContainer(
                contextManager_,
                integrationLayerContainer_,
                flightControlView_,
                pcIpAddress,
                isLiveModeEnabled);

        missionContainer_ = new MissionContainer(
                integrationLayerContainer_,
                imageTransferContainer_,
                flightControlView_,
                contextManager_);

        productConnectionPresenter_ = new ProductConnectionPresenter(
                flightControlView_.connectionStatusText(),
                contextManager_);

        permissionRequester_.requestPermissions(this);

        setContentView(flightControlView_);
    }
}
