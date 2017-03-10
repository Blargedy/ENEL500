package com.dji.sdk.sample.common.presenter.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.mission.src.MissionBoundary;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;

/**
 * Created by Julia on 2017-03-10.
 */

public class MissionMapDisplayPresenter
{
    private MissionStateEntity missionState_;
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private BroadcastReceiver receiver_;
    private MapPresenter mapPresenter_;

    public MissionMapDisplayPresenter(
            Context context,
            MissionStateEntity missionState,
            InitialMissionModel initialMissionModel,
            GeneratedMissionModel generatedMissionModel,
            MapPresenter mapPresenter)
    {
        missionState_ = missionState;
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        mapPresenter_ = mapPresenter;

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
        switch (currentMissionState) {
            case SELECT_AREA:
                mapPresenter_.clearMap();
                mapPresenter_.enableAllControls();
                break;

            case GENERATE_MISSION_BOUNDARY:
                mapPresenter_.disableAllControls();
                MissionBoundary boundary = mapPresenter_.getSurveyAreaBoundary();
                initialMissionModel_.setMissionBoundary(boundary);
                missionState_.setCurrentMissionState(MissionStateEnum.GENERATE_MISSION);
                break;

            case VIEW_MISSION:
                mapPresenter_.displayMissionWaypoints(generatedMissionModel_.waypoints_);
                break;

            default:
                break;
        }
    }
}
