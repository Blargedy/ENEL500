package com.dji.sdk.sample.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dji.sdk.sample.common.container.ImageTransferContainer;
import com.dji.sdk.sample.common.container.IntegrationLayerContainer;
import com.dji.sdk.sample.common.container.MissionContainer;
import com.dji.sdk.sample.common.presenter.ProductConnectionPresenter;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.FlightControlView;

public class FlightControlActivity extends AppCompatActivity
{
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;

    private FlightControlView flightControlView_;

    private IntegrationLayerContainer integrationLayerContainer_;

    private MissionContainer missionContainer_;

    private ImageTransferContainer imageTransferContainer_;

    private ProductConnectionPresenter productConnectionPresenter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionRequester_ = new UserPermissionRequester();
        contextManager_ = new ApplicationContextManager(this);
        sendLogsToFile();

        flightControlView_ = new FlightControlView(this);

        integrationLayerContainer_ = new IntegrationLayerContainer();

        missionContainer_ = new MissionContainer(
                flightControlView_,
                contextManager_);

        imageTransferContainer_ = new ImageTransferContainer(
                contextManager_,
                integrationLayerContainer_.mediaManagerSource(),
                integrationLayerContainer_.mediaDataFetcher(),
                missionContainer_.missionController(),
                flightControlView_);

        productConnectionPresenter_ = new ProductConnectionPresenter(
                flightControlView_.connectionStatusText(),
                contextManager_);

        permissionRequester_.requestPermissions(this);

        setContentView(flightControlView_);


    }

    private void sendLogsToFile()
    {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            process = Runtime.getRuntime().exec( "logcat -f " + "/storage/emulated/0/"+"Logging.txt");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
