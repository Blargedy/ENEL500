package com.dji.sdk.sample.common.mission.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.mission.api.I_MissionCancellationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.view.api.I_MapView;
import com.dji.sdk.sample.common.entity.MissionStateEnum;

/**
 * Created by Julia on 2017-03-07.
 */

public class MissionStateManager implements
        I_MissionGenerationCompletionCallback,
        I_MissionCancellationCompletionCallback
{
    private static final String TAG = "MissionStateManager";

    private BroadcastReceiver receiver_;

    private I_MapView mapView_;

    private I_MissionGenerator missionGenerator_;
    private I_MissionController missionController_;
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private MissionStateEntity missionState_;

    public MissionStateManager(
            Context context,
            I_MapView mapView,
            I_MissionGenerator missionGenerator,
            I_MissionController missionController,
            InitialMissionModel initialMissionModel,
            GeneratedMissionModel generatedMissionModel,
            MissionStateEntity missionState)
    {
        mapView_ = mapView;

        missionGenerator_ = missionGenerator;
        missionController_ = missionController;
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        missionState_ = missionState;

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
        switch (currentMissionState)
        {
            case SELECT_AREA:
                mapView_.clearMap();
                break;

            case VIEW_MISSION:
                generateMission();
                break;

            case EXECUTE_MISSION:
                if (previousMissionState == MissionStateEnum.VIEW_MISSION){
                    missionController_.startMission(null);
                } else if (previousMissionState == MissionStateEnum.PAUSE_MISSION){
                    missionController_.resumeMission(null);
                }
                break;

            case PAUSE_MISSION:
                missionController_.pauseMission(null);
                break;

            case CANCEL_MISSION:
                if (previousMissionState == MissionStateEnum.PAUSE_MISSION ||
                        previousMissionState == MissionStateEnum.EXECUTE_MISSION){
                    missionController_.cancelMission(this);
                } else if (previousMissionState == MissionStateEnum.PAUSE_CANCEL_MISSION) {
                    missionController_.resumeGoHome();
                }
                break;

            case PAUSE_CANCEL_MISSION:
                missionController_.pauseGoHome();
                break;

            default:
                break;
        }

        Log.d(TAG, "Mission state changed from "
                + previousMissionState.name() + " to " + currentMissionState.name());
    }

    void generateMission()
    {
        MissionBoundary boundary = mapView_.getSurveyAreaBoundary();
        initialMissionModel_.setMissionBoundary(boundary);
        missionGenerator_.generateMission(this);
    }

    @Override
    public void onMissionGenerationCompletion()
    {
        mapView_.displayMissionWaypoints(generatedMissionModel_.waypoints_);
    }

    @Override
    public void onMissionCancellationCompletion()
    {
        missionState_.setCurrentMissionState(MissionStateEnum.SELECT_AREA);
    }
}
