package com.dji.sdk.sample.common.presenter.src;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.view.api.I_MissionControlsView;

/**
 * Created by Julia on 2017-03-15.
 */

public class MissionSettingsPresenter implements
        View.OnClickListener,
        DialogInterface.OnClickListener
{
    public Button settingsButton_;

    private MissionStateEntity missionState_;
    private BroadcastReceiver receiver_;
    private Context context_;

    public MissionSettingsPresenter(
            I_MissionControlsView view,
            MissionStateEntity missionState,
            Context context)
    {
        settingsButton_ = view.settingsButton();
        settingsButton_.setOnClickListener(this);

        missionState_ = missionState;
        context_ = context;

        registerMissionStateChangedReceiver(context);
    }

    private void registerMissionStateChangedReceiver(Context context)
    {
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
    public void onClick(View v)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(context_);

        alert.setTitle("Mission Settings");
        alert.setPositiveButton("Ok", );
        alert.setNegativeButton("Cancel", null);
        alert.setView(R.layout.settings_dialog_screen);

        alert.show();
    }

    private void setViewBasedOnMissionState()
    {
        switch (missionState_.getCurrentMissionState())
        {
            case SELECT_AREA:
                settingsButton_.setVisibility(View.VISIBLE);
                settingsButton_.setEnabled(true);
                break;

            case GENERATE_MISSION_BOUNDARY:
                settingsButton_.setEnabled(false);
                break;

            case VIEW_MISSION:
                settingsButton_.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
