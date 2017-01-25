package com.dji.sdk.sample.common.presenter;

import android.view.View;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.utility.DJISampleApplication;
import com.dji.sdk.sample.common.mission.FlightController;
import com.dji.sdk.sample.common.view.SimpleDemoView;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.base.DJIBaseProduct;

/**
 * Created by Julia on 2017-01-18.
 */

public class SimpleDemoPresenter implements
        View.OnClickListener,
        DJICommonCallbacks.DJICompletionCallback
{
    private SimpleDemoView view_;
    private FlightController flightController_;

    public SimpleDemoPresenter(
            SimpleDemoView view,
            FlightController flightController)
    {
        view_ = view;
        flightController_ = flightController;

        registerButtonOnClickListeners();
    }

    private void registerButtonOnClickListeners()
    {
        view_.takeOffButton().setOnClickListener(this);
        view_.landButton().setOnClickListener(this);
        view_.shootPhotoButton().setOnClickListener(this);
    }

    public void productConnectionChanged()
    {
        DJIBaseProduct product = DJISampleApplication.getProductInstance();

        if (product != null && product.isConnected())
        {
            view_.connectionStatusText().setText("Status: Connected");
            view_.takeOffButton().setEnabled(true);
            view_.shootPhotoButton().setEnabled(true);
        }
        else
        {
            view_.connectionStatusText().setText("Status: No Product Connected");
            view_.takeOffButton().setEnabled(false);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_takeoff:
                view_.takeOffButton().setEnabled(false);
                flightController_.takeoff(this);

                break;

            case R.id.btn_land:
                view_.landButton().setEnabled(false);
                flightController_.land(this);

                break;

            case R.id.btn_shoot_photo:

            default:
                break;
        }
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            if (!flightController_.isAltitudeZero())
            {
                view_.post(new Runnable() {
                    @Override
                    public void run() {
                        updateUiForDroneInFlight();
                    }
                });
            }
            else
            {
                view_.post(new Runnable() {
                    @Override
                    public void run() {
                        updateUiForDroneNotInFlight();
                    }
                });

                flightController_.turnOffMotors();
            }
        }
    }

    private void updateUiForDroneInFlight()
    {
        view_.connectionStatusText().setText("Takeoff callback");

        view_.takeOffButton().setVisibility(View.GONE);
        view_.takeOffButton().setEnabled(true);

        view_.landButton().setVisibility(View.VISIBLE);
    }

    private void updateUiForDroneNotInFlight()
    {
        view_.connectionStatusText().setText("Landing callback");

        view_.takeOffButton().setVisibility(View.VISIBLE);

        view_.landButton().setVisibility(View.GONE);
        view_.landButton().setEnabled(true);
    }
}
