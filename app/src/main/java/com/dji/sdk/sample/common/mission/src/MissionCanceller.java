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
import com.dji.sdk.sample.common.integration.api.I_FlightController;
import com.dji.sdk.sample.common.integration.api.I_FlightControllerSource;
import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-09.
 */

public class MissionCanceller implements I_CompletionCallback
{
    private static final String TAG = "MissionCanceller";

    private enum ExpectedCallback {
        STOP_MISSION,
        GO_HOME,
        CANCEL_GO_HOME
    }

    private BroadcastReceiver receiver_;
    private MissionStateEntity missionState_;
    private I_MissionManagerSource missionManagerSource_;
    private I_FlightControllerSource flightControllerSource_;
    private ExpectedCallback expectedCallback_;

    public MissionCanceller(
            Context context,
            MissionStateEntity missionState,
            I_MissionManagerSource missionManagerSource,
            I_FlightControllerSource flightControllerSource)
    {
        missionState_ = missionState;
        missionManagerSource_ = missionManagerSource;
        flightControllerSource_ = flightControllerSource;

        registerMissionStateChangedReceiver(context);
    }

    private void registerMissionStateChangedReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.MISSION_STATE_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                missionStateChanged();
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver_, filter);
    }

    private void missionStateChanged()
    {
        MissionStateEnum currentMissionState = missionState_.getCurrentMissionState();
        MissionStateEnum previousMissionState = missionState_.getPreviousMissionState();

        Log.e(TAG, "Mission state changed");

        if (currentMissionState == MissionStateEnum.CANCEL_MISSION)
        {
            Log.e(TAG, "CANCEL_MISSION");

            if (previousMissionState == MissionStateEnum.PAUSE_MISSION ||
                    previousMissionState == MissionStateEnum.EXECUTE_MISSION){
                cancelMission();
            } else if (previousMissionState == MissionStateEnum.PAUSE_CANCEL_MISSION) {
                goHome();
            }
        }
        else if (currentMissionState == MissionStateEnum.PAUSE_CANCEL_MISSION)
        {
            cancelGoHome();
        }
    }

    private void cancelMission()
    {
        Log.e(TAG, "cancelMission");

        expectedCallback_ = ExpectedCallback.STOP_MISSION;
        missionManagerSource_.getMissionManager().stopMissionExecution(this);
    }

    private void cancelGoHome()
    {
        Log.e(TAG, "cancelGoHome");

        expectedCallback_ = ExpectedCallback.CANCEL_GO_HOME;
        flightControllerSource_.getFlightController().cancelGoHome(this);
    }

    private void goHome()
    {
        expectedCallback_ = ExpectedCallback.GO_HOME;
        flightControllerSource_.getFlightController().goHome(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error == null)
        {
            switch (expectedCallback_)
            {
                case STOP_MISSION:
                    Log.e(TAG, "stop mission callback");

                    goHome();
                    break;
                case GO_HOME:
                    missionState_.setCurrentMissionState(MissionStateEnum.SELECT_AREA);
                    break;
                case CANCEL_GO_HOME:
                    break;
                default:
                    break;
            }
        }
        else
        {
            Log.e(TAG, error.getDescription());
        }
    }
}
