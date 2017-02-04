package com.dji.sdk.sample.common.presenter;

import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.mission.MissionGenerator;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionOperationPresenter implements View.OnClickListener
{
    private Button startMissionButton_;

    public MissionOperationPresenter(
            Button startMissionButton)
    {
        startMissionButton_ = startMissionButton;
        startMissionButton_.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == startMissionButton_.getId())
        {

        }
    }
}
