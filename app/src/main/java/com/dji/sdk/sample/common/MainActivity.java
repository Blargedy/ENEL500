package com.dji.sdk.sample.common;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.mission.MissionGenerator;
import dji.sdk.base.DJIBaseProduct;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getName();

    private TextView connectionStatusText_;
    private Button hoverNowButton_;
    private Button executeMissionButton_;

    private MissionGenerator missionGenerator_;

    protected BroadcastReceiver receiver_;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestPermissions();
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DJISampleApplication.FLAG_CONNECTION_CHANGE);

        receiver_  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onProductConnectionChange();
            }
        };

        registerReceiver(receiver_, filter);

        initUI();
    }

    private void requestPermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);
        }
    }

    private void onProductConnectionChange()
    {
        updateUiOnConnectionChange();
//        initMissionManager();
//        initFlightController();
    }

    private void initUI()
    {
        Log.v(TAG, "initUI");

        connectionStatusText_ = (TextView) findViewById(R.id.text_connection_status);

        hoverNowButton_ = (Button) findViewById(R.id.btn_hover);
        hoverNowButton_.setEnabled(false);
        hoverNowButton_.setOnClickListener(this);

        executeMissionButton_ = (Button) findViewById(R.id.btn_execute_mission);
        executeMissionButton_.setEnabled(false);
        executeMissionButton_.setOnClickListener(this);
    }

    private void updateUiOnConnectionChange()
    {
        DJIBaseProduct product = DJISampleApplication.getProductInstance();

        if (product != null && product.isConnected())
        {
            connectionStatusText_.setText("Status: Connected");
            executeMissionButton_.setEnabled(true);
        }
        else
        {
            connectionStatusText_.setText("Status: No Product Connected");
            executeMissionButton_.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_hover:
            {

                break;
            }
            case R.id.btn_execute_mission:
            {
//                missionGenerator_.generateMissionWithOneWaypoint(
//                        DJISampleApplication.getProductInstance().getMissionManager());


                break;
            }
            default:
                break;
        }
    }
}
