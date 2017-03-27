package com.dji.sdk.sample.common.presenter.src;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.activity.FlightControlActivity;
import com.dji.sdk.sample.common.activity.MainMenuActivity;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.integration.src.DJISampleApplication;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.view.api.I_MissionControlsView;

/**
 * Created by Julia on 2017-03-08.
 */

public class MissionControlsPresenter implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private Button acceptAreaButton_;
    private Button startMissionButton_;
    private Button cancelButton_;
    private ToggleButton hoverNowToggleButton_;
    private ProgressBar loadingProgressAnimation_;
    private MissionStateEntity missionState_;
    private BroadcastReceiver receiver_;
    private Context context_;

    public MissionControlsPresenter(
            Context context,
            I_MissionControlsView view,
            MissionStateEntity missionState) {
        acceptAreaButton_ = view.acceptAreaButton();
        startMissionButton_ = view.startMissionButton();
        cancelButton_ = view.cancelButton();
        loadingProgressAnimation_ = view.loadingProgressAnimation();
        hoverNowToggleButton_ = view.hoverNowToggleButton();
        missionState_ = missionState;
        loadingProgressAnimation_.setVisibility(View.INVISIBLE);
        setButtonOnClickListeners();
        registerMissionStateChangedReceiver(context);
        setViewBasedOnMissionState();
        context_ = context;
    }

    private void setButtonOnClickListeners() {
        acceptAreaButton_.setOnClickListener(this);
        startMissionButton_.setOnClickListener(this);
        cancelButton_.setOnClickListener(this);
        hoverNowToggleButton_.setOnCheckedChangeListener(this);
    }

    private void registerMissionStateChangedReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.MISSION_STATE_CHANGED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setViewBasedOnMissionState();
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver_, filter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == acceptAreaButton_.getId()) {
            missionState_.setCurrentMissionState(MissionStateEnum.GENERATE_MISSION_BOUNDARY);
        } else if (v.getId() == startMissionButton_.getId()) {
            if (DJISampleApplication.isAircraftConnected()) {
                missionState_.setCurrentMissionState(MissionStateEnum.INITIALIZE_MISSION);
            } else {
                warnUserThatDroneIsNotConnected();
            }
        } else if (v.getId() == cancelButton_.getId()) {
            cancelButtonPressed();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == hoverNowToggleButton_.getId()) {
            hoverNowToggleButtonPressed();
        }
    }

    private void cancelButtonPressed() {
        Intent mainIntent;
        switch (missionState_.getCurrentMissionState()) {
            case INITIALIZING_MAP:
                mainIntent = new Intent(context_, MainMenuActivity.class);
                context_.startActivity(mainIntent);
                mainIntent = new Intent(context_, FlightControlActivity.class);
                context_.stopService(mainIntent);
                break;
            case SELECT_AREA:
                mainIntent = new Intent(context_, MainMenuActivity.class);
                context_.startActivity(mainIntent);
                mainIntent = new Intent(context_, FlightControlActivity.class);
                context_.stopService(mainIntent);
                break;
            case VIEW_MISSION:
                missionState_.setCurrentMissionState(MissionStateEnum.SELECT_AREA);
                break;
            case MISSION_EXECUTING:
                askUserIfTheyAreSureTheyWantToCancel();
                break;
            case HOVERING:
                askUserIfTheyAreSureTheyWantToCancel();
                break;
            default:
                break;
        }
    }

    private void hoverNowToggleButtonPressed() {
        switch (missionState_.getCurrentMissionState()) {
            case MISSION_EXECUTING:
                missionState_.setCurrentMissionState(MissionStateEnum.HOVER_NOW);
                break;
            case HOVERING:
                missionState_.setCurrentMissionState(MissionStateEnum.RESUME_MISSION);
                break;
            case GO_HOME:
                missionState_.setCurrentMissionState(MissionStateEnum.PAUSE_GO_HOME);
                break;
            case GO_HOME_PAUSED:
                missionState_.setCurrentMissionState(MissionStateEnum.GO_HOME);
                break;
            default:
                break;
        }
    }

    private void setViewBasedOnMissionState() {
        switch (missionState_.getCurrentMissionState()) {
            case INITIALIZING_MAP:
                loadingProgressAnimation_.setVisibility(View.VISIBLE);
                acceptAreaButton_.setVisibility(View.VISIBLE);
                acceptAreaButton_.setEnabled(false);
                cancelButton_.setVisibility(View.VISIBLE);
                startMissionButton_.setVisibility(View.GONE);
                cancelButton_.setEnabled(true);
                hoverNowToggleButton_.setVisibility(View.GONE);
                break;
            case SELECT_AREA:
                loadingProgressAnimation_.setVisibility(View.INVISIBLE);
                acceptAreaButton_.setVisibility(View.VISIBLE);
                acceptAreaButton_.setEnabled(true);
                cancelButton_.setEnabled(true);
                startMissionButton_.setVisibility(View.GONE);
                hoverNowToggleButton_.setVisibility(View.GONE);
                break;
            case GENERATE_MISSION_BOUNDARY:
                loadingProgressAnimation_.setVisibility(View.VISIBLE);
                acceptAreaButton_.setEnabled(false);
                break;
            case VIEW_MISSION:
                loadingProgressAnimation_.setVisibility(View.VISIBLE);
                startMissionButton_.setEnabled(false);
                // if (!startMissionButton_.getText().equals("Demo Mode Activated. Return to the Main Menu to reset.")) {
                //    startMissionButton_.setText("Please wait...");
                // }
                acceptAreaButton_.setEnabled(false);
                cancelButton_.setEnabled(false);
                acceptAreaButton_.setVisibility(View.GONE);
                hoverNowToggleButton_.setVisibility(View.GONE);
                startMissionButton_.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();

                final Runnable enableAfterWaypointsShowing = new Runnable() {
                    public void run() {
                        startMissionButton_.setText("Start Mission");
                        startMissionButton_.setEnabled(true);
                        cancelButton_.setEnabled(true);
                        loadingProgressAnimation_.setVisibility(View.INVISIBLE);
                    }
                };

                handler.postDelayed(enableAfterWaypointsShowing, 3000);

                break;
            case INITIALIZE_MISSION:
                loadingProgressAnimation_.setVisibility(View.VISIBLE);
                startMissionButton_.setEnabled(false);
                cancelButton_.setEnabled(false);
                break;
            case MISSION_EXECUTING:
                loadingProgressAnimation_.setVisibility(View.INVISIBLE);
                hoverNowToggleButton_.setVisibility(View.VISIBLE);
                hoverNowToggleButton_.setEnabled(true);
                cancelButton_.setEnabled(true);
                acceptAreaButton_.setVisibility(View.GONE);
                startMissionButton_.setVisibility(View.GONE);
                break;
            case HOVER_NOW:
                hoverNowToggleButton_.setEnabled(false);
                cancelButton_.setEnabled(true);
                break;
            case HOVERING:
                hoverNowToggleButton_.setEnabled(true);
                cancelButton_.setEnabled(true);
                break;
            case RESUME_MISSION:
                hoverNowToggleButton_.setEnabled(false);
                cancelButton_.setEnabled(true);
                break;
            case GO_HOME:
                hoverNowToggleButton_.setOnCheckedChangeListener(null);
                hoverNowToggleButton_.setChecked(false);
                hoverNowToggleButton_.setOnCheckedChangeListener(this);
                hoverNowToggleButton_.setEnabled(true);
                cancelButton_.setEnabled(false);
                break;
            case PAUSE_GO_HOME:
                hoverNowToggleButton_.setEnabled(false);
                break;
            case GO_HOME_PAUSED:
                hoverNowToggleButton_.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void askUserIfTheyAreSureTheyWantToCancel() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context_);

        alert.setTitle("Cancel Mission");
        alert.setMessage("Once you cancel you will not be able to restart your mission. " +
                "Are you sure you want to cancel?");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                missionState_.setCurrentMissionState(MissionStateEnum.GO_HOME);
            }
        });
        alert.setNegativeButton("Cancel", null);

        alert.show();
    }

    private void warnUserThatDroneIsNotConnected() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context_);

        alert.setTitle("Drone Not Connected");
        alert.setMessage("Please connect the drone before starting the mission");
        alert.setPositiveButton("Ok", null);

        alert.show();
    }
}
