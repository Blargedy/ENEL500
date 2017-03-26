package com.dji.sdk.sample.common.mission.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.mission.api.I_MissionStateResetter;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.I_MissionStatusNotifier;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-09.
 */

public class MissionCanceller implements I_CompletionCallback
{
    private static final String TAG = "HydraMissionCanceller";

    private I_MissionStatusNotifier missionStatusNotifier_;
    private MissionStateEntity missionState_;
    private I_FlightControllerSource flightControllerSource_;
    private I_MissionStateResetter missionStateResetter_;

    private BroadcastReceiver missionStateChangedReceiver_;
    private BroadcastReceiver droneReachedHomeReceiver_;

    public MissionCanceller(
            Context context,
            I_MissionStatusNotifier missionStatusNotifier,
            MissionStateEntity missionState,
            I_FlightControllerSource flightControllerSource,
            I_MissionStateResetter missionStateResetter)
    {
        missionStatusNotifier_ = missionStatusNotifier;
        missionState_ = missionState;
        flightControllerSource_ = flightControllerSource;
        missionStateResetter_ = missionStateResetter;

        registerMissionStateChangedReceiver(context);
        registerDroneReachedHomeReceiver(context);
    }

    private void registerMissionStateChangedReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.MISSION_STATE_CHANGED);

        missionStateChangedReceiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                missionStateChanged();
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(missionStateChangedReceiver_, filter);
    }

    private void registerDroneReachedHomeReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.DRONE_REACHED_HOME);

        droneReachedHomeReceiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                droneReachedHome();
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(droneReachedHomeReceiver_, filter);
    }

    private void missionStateChanged()
    {
        switch (missionState_.getCurrentMissionState())
        {
            case GO_HOME:
                missionStatusNotifier_.notifyStatusChanged("Going home...");
                missionStateResetter_.resetMissionState();
                flightControllerSource_.getFlightController().goHome(this);
                break;

            case PAUSE_GO_HOME:
                missionStatusNotifier_.notifyStatusChanged("Hovering");
                flightControllerSource_.getFlightController().cancelGoHome(this);
                break;

            default:
                break;
        }
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            if (missionState_.getCurrentMissionState() == MissionStateEnum.PAUSE_GO_HOME)
            {
                missionState_.setCurrentMissionState(MissionStateEnum.GO_HOME_PAUSED);
            }
        }
        else
        {
            Log.e(TAG, error.getDescription());
            missionStatusNotifier_.notifyStatusChanged(error.getDescription());
        }
    }

    private void droneReachedHome()
    {
        missionStatusNotifier_.notifyStatusChanged("Arrived home - Select another mission");
        missionState_.setCurrentMissionState(MissionStateEnum.SELECT_AREA);
    }
}
