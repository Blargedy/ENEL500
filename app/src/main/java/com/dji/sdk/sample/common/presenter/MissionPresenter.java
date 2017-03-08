package com.dji.sdk.sample.common.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.api.I_MissionCancellationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.mission.src.MissionBoundary;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.view.api.I_MapView;
import com.dji.sdk.sample.common.view.api.I_MissionView;
import com.dji.sdk.sample.common.view.src.MissionState;

/**
 * Created by Julia on 2017-03-07.
 */

public class MissionPresenter implements
        I_MissionGenerationCompletionCallback,
        I_MissionCancellationCompletionCallback
{
    private static final String TAG = "MissionPresenter";

    private BroadcastReceiver receiver_;
    private MissionState oldMissionState_;

    private I_MissionView missionView_;
    private I_MapView mapView_;

    private I_MissionGenerator missionGenerator_;
    private I_MissionController missionController_;
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;

    public MissionPresenter(
            Context context,
            I_MissionView missionView,
            I_MapView mapView,
            I_MissionGenerator missionGenerator,
            I_MissionController missionController,
            InitialMissionModel initialMissionModel,
            GeneratedMissionModel generatedMissionModel)
    {
        missionView_ = missionView;
        mapView_ = mapView;
        missionGenerator_ = missionGenerator;
        missionController_ = missionController;
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;

        registerMissionStateChangedReceiver(context);
        oldMissionState_ = missionView_.currentMissionState();
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
        MissionState newMissionState = missionView_.currentMissionState();
        Log.d(TAG, "Mission state changed: " + newMissionState.name());

        switch (newMissionState)
        {
            case SELECT_AREA:
                mapView_.clearMap();
                break;

            case VIEW_MISSION:
                generateMission();
                break;

            case EXECUTE_MISSION:
                if (oldMissionState_ == MissionState.VIEW_MISSION){
                    missionController_.startMission(null);
                } else if (oldMissionState_ == MissionState.PAUSE_MISSION){
                    missionController_.resumeMission(null);
                }
                break;

            case PAUSE_MISSION:
                missionController_.pauseMission(null);
                break;

            case CANCEL_MISSION:
                if (oldMissionState_ == MissionState.PAUSE_MISSION ||
                        oldMissionState_ == MissionState.EXECUTE_MISSION){
                    missionController_.cancelMission(this);
                } else if (oldMissionState_ == MissionState.PAUSE_CANCEL_MISSION) {
                    missionController_.resumeGoHome();
                }
                break;

            case PAUSE_CANCEL_MISSION:
                missionController_.pauseGoHome();
                break;

            default:
                break;
        }

        oldMissionState_ = newMissionState;
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
        missionView_.setCurrentMissionState(MissionState.SELECT_AREA);
    }
}
