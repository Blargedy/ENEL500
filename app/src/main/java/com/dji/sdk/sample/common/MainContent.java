package com.dji.sdk.sample.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dji.sdk.sample.R;

import dji.common.error.DJIError;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.products.DJIAircraft;
import dji.sdk.flightcontroller.DJIFlightController;
import dji.common.util.DJICommonCallbacks.DJICompletionCallback;
import dji.thirdparty.eventbus.EventBus;

/**
 * Created by dji on 15/12/18.
 */
public class MainContent extends RelativeLayout
{
    public static final String TAG = MainContent.class.getName();

    private TextView mTextConnectionStatus;
    private Button mButtonHoverNow;
    private Button mButtonLand;
    private DJIBaseProduct mProduct;

    public MainContent(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        initUI();
    }

    private void initUI()
    {
        Log.v(TAG, "initUI");

        mTextConnectionStatus = (TextView) findViewById(R.id.text_connection_status);
        mButtonHoverNow = (Button) findViewById(R.id.btn_hover);
        mButtonLand = (Button) findViewById(R.id.btn_land);

//        mButtonHoverNow.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mProduct = DJISampleApplication.getProductInstance();
//                DJIFlightController flightController = ((DJIAircraft) mProduct).getFlightController();
//                flightController.takeOff(null);
//            }
//        });
//
//        mButtonLand.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mProduct = DJISampleApplication.getProductInstance();
//                final DJIFlightController flightController = ((DJIAircraft) mProduct).getFlightController();
//                flightController.autoLanding(new DJICompletionCallback() {
//                    @Override
//                    public void onResult(DJIError error) {
//                        flightController.turnOffMotors(null);
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "Comes into the onAttachedToWindow");
        refreshSDKRelativeUI();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DJISampleApplication.FLAG_CONNECTION_CHANGE);
        getContext().registerReceiver(mReceiver, filter);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        getContext().unregisterReceiver(mReceiver);
        super.onDetachedFromWindow();
    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Comes into the BroadcastReceiver");
            refreshSDKRelativeUI();
        }

    };

    private void refreshSDKRelativeUI()
    {
        mProduct = DJISampleApplication.getProductInstance();
        Log.d(TAG, "mProduct: " + (mProduct == null? "null" : "unnull") );

        if (mProduct != null && mProduct.isConnected())
        {
            mButtonHoverNow.setEnabled(true);
            mTextConnectionStatus.setText("Status: Connected");
        }
        else
        {
            mButtonHoverNow.setEnabled(false);
            mButtonLand.setEnabled(false);
            mTextConnectionStatus.setText(R.string.connection_loose);
        }
    }
}