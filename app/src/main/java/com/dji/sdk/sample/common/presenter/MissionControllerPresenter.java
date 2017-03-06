package com.dji.sdk.sample.common.presenter;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.FlightControlView;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionControllerPresenter implements
        View.OnClickListener,
        I_CompletionCallback
{
    private Button startMissionButton_;
    private I_MissionController controller_;
    private I_ApplicationContextManager contextManager_;

    public MissionControllerPresenter(
            FlightControlView view,
            I_MissionController controller,
            I_ApplicationContextManager contextManager)
    {
        startMissionButton_ = view.startMissionButton();
        startMissionButton_.setOnClickListener(this);

        controller_ = controller;
        contextManager_ = contextManager;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == startMissionButton_.getId())
        {
            controller_.startMission(this);
        }
    }

    @Override
    public void onResult(DJIError error)
    {
        Toast.makeText(contextManager_.getApplicationContext(), "Success: started mission" , Toast.LENGTH_SHORT).show();
    }
}
