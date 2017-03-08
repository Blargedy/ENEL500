package com.dji.sdk.sample.common.testClasses;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dji.sdk.sample.common.mission.api.I_MissionGenerationCompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_MissionGenerator;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionGenerationPresenterTest implements
        View.OnClickListener,
        I_MissionGenerationCompletionCallback
{
    private Button generateMissionButton_;
    private Button startMissionButton_;
    private I_MissionGenerator missionGenerator_;
    private I_ApplicationContextManager contextManager_;

    public MissionGenerationPresenterTest(
            I_MissionControlTestView view,
            I_MissionGenerator missionGenerator,
            I_ApplicationContextManager contextManager)
    {
        generateMissionButton_ = view.generateMissionButton();
        generateMissionButton_.setOnClickListener(this);

        startMissionButton_ = view.startMissionButton();
        startMissionButton_.setEnabled(false);

        missionGenerator_ = missionGenerator;
        contextManager_ = contextManager;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == generateMissionButton_.getId())
        {
            missionGenerator_.generateMission(this);
            startMissionButton_.setEnabled(true);
        }
    }

    @Override
    public void onMissionGenerationCompletion()
    {
        Toast.makeText(contextManager_.getApplicationContext(), "Success: generated mission" , Toast.LENGTH_SHORT).show();
    }
}
