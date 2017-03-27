package com.dji.sdk.sample.common.presenter.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.mission.api.I_MissionStateResetter;
import com.dji.sdk.sample.common.values.MissionBoundary;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;

/**
 * Created by Julia on 2017-03-10.
 */

public class MissionMapDisplayPresenter {
    private MissionStateEntity missionState_;
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private BroadcastReceiver receiver_;
    private MapPresenter mapPresenter_;
    private MissionStateEnum stateBeforeChange;

    public MissionMapDisplayPresenter(
            Context context,
            MissionStateEntity missionState,
            InitialMissionModel initialMissionModel,
            GeneratedMissionModel generatedMissionModel,
            MapPresenter mapPresenter) {
        missionState_ = missionState;
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        mapPresenter_ = mapPresenter;
        stateBeforeChange = missionState.getCurrentMissionState();
        registerMissionStateChangedReceiver(context);
    }

    private void registerMissionStateChangedReceiver(Context context) {
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

    private void missionStateChanged() {

        MissionStateEnum currentMissionState = missionState_.getCurrentMissionState();

        switch (currentMissionState) {
            case INITIALIZING_MAP:
                mapPresenter_.resetPercentageText();
                break;
            case SELECT_AREA:
                mapPresenter_.resetPercentageText();
                mapPresenter_.pbarsShow();
                if (stateBeforeChange == MissionStateEnum.VIEW_MISSION) {
                    generatedMissionModel_.clearWaypointMissions();
                    mapPresenter_.clearMap();
                    mapPresenter_.resetPercentageText();
                }
                break;
            case GENERATE_MISSION_BOUNDARY:
                mapPresenter_.resetPercentageText();
                MissionBoundary boundary = mapPresenter_.getSurveyAreaBoundary();
                initialMissionModel_.setMissionBoundary(boundary);
                missionState_.setCurrentMissionState(MissionStateEnum.GENERATE_MISSION);
                break;
            case VIEW_MISSION:
                mapPresenter_.resetPercentageText();
                final Handler handler = new Handler();

                final Runnable drawWaypoints = new Runnable() {
                    public void run() {
                        mapPresenter_.displayMissionWaypoints(generatedMissionModel_.waypoints());
                    }
                };
                handler.post(drawWaypoints);

                break;
            case MISSION_EXECUTING:
                mapPresenter_.pbarsHide();
                break;
            case GO_HOME:
                mapPresenter_.resetPercentageText();
                mapPresenter_.clearMap();
                break;
            case GO_HOME_PAUSED:
                mapPresenter_.resetPercentageText();
                mapPresenter_.clearMap();
                break;
            case PAUSE_GO_HOME:
                mapPresenter_.resetPercentageText();
                mapPresenter_.clearMap();
                break;
            default:
                break;
        }
        this.stateBeforeChange = currentMissionState;
    }
}