package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.I_MissionController;
import com.dji.sdk.sample.common.mission.MissionController;
import com.dji.sdk.sample.common.mission.MissionGenerator;
import com.dji.sdk.sample.common.mission.StepCompletionCallback;
import com.dji.sdk.sample.common.presenter.MapPresenter;
import com.dji.sdk.sample.common.presenter.MissionGenerationPresenter;
import com.dji.sdk.sample.common.presenter.MissionControllerPresenter;
import com.dji.sdk.sample.common.presenter.ShootPhotoPresenter;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.FlightControlView;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionContainer
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;

    private MissionController missionController_;
    private MissionControllerPresenter missionControllerPresenter_;

    private StepCompletionCallback stepCompletionCallback_;

    private MissionGenerator missionGenerator_;
    private MissionGenerationPresenter missionGenerationPresenter_;



    private ShootPhotoPresenter shootPhotoPresenter_;

    private MapPresenter mapPresenter_;

    public MissionContainer(
            FlightControlView flightControlView,
            I_ApplicationContextManager contextManager)
    {
        initialMissionModel_ = new InitialMissionModel();
        generatedMissionModel_ = new GeneratedMissionModel();

        missionController_ = new MissionController(
                contextManager,
                generatedMissionModel_);
        missionControllerPresenter_ = new MissionControllerPresenter(
                flightControlView.startMissionButton(),
                missionController_);

        stepCompletionCallback_ = new StepCompletionCallback(missionController_, contextManager);

        missionGenerator_ = new MissionGenerator(
                contextManager,
                initialMissionModel_,
                generatedMissionModel_,
                stepCompletionCallback_);

        missionGenerationPresenter_ = new MissionGenerationPresenter(
                flightControlView.generateMissionButton(),
                flightControlView.startMissionButton(),
                missionGenerator_);



        mapPresenter_ = new MapPresenter(
                contextManager,
                flightControlView.goToMapButton());
    }

    public I_MissionController missionController()
    {
        return missionController_;
    }
}
