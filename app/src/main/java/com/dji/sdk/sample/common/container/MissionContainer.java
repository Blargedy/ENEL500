package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.src.CustomMissionBuilder;
import com.dji.sdk.sample.common.mission.api.I_MissionController;
import com.dji.sdk.sample.common.mission.src.MissionController;
import com.dji.sdk.sample.common.mission.src.MissionGenerator;
import com.dji.sdk.sample.common.mission.src.MissionStepCompletionCallback;
import com.dji.sdk.sample.common.mission.src.MissionPreparer;
import com.dji.sdk.sample.common.presenter.MapPresenter;
import com.dji.sdk.sample.common.presenter.MissionGenerationPresenter;
import com.dji.sdk.sample.common.presenter.MissionControllerPresenter;
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

    private MissionStepCompletionCallback missionStepCompletionCallback_;

    private CustomMissionBuilder customMissionBuilder_;
    private MissionPreparer missionPreparer_;
    private MissionGenerator missionGenerator_;
    private MissionGenerationPresenter missionGenerationPresenter_;

    private MapPresenter mapPresenter_;

    public MissionContainer(
            IntegrationLayerContainer integrationLayerContainer,
            ImageTransferContainer imageTransferContainer,
            FlightControlView flightControlView,
            I_ApplicationContextManager contextManager)
    {

        initialMissionModel_ = new InitialMissionModel();
        generatedMissionModel_ = new GeneratedMissionModel();

        missionController_ = new MissionController(
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                contextManager);
        missionControllerPresenter_ = new MissionControllerPresenter(
                flightControlView.startMissionButton(),
                missionController_);

        missionStepCompletionCallback_ = new MissionStepCompletionCallback(
                missionController_,
                imageTransferContainer.imageTransferer(),
                contextManager);

        customMissionBuilder_ = new CustomMissionBuilder(
                initialMissionModel_,
                generatedMissionModel_,
                contextManager,
                missionStepCompletionCallback_);
        missionPreparer_ = new MissionPreparer(
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                generatedMissionModel_);
        missionGenerator_ = new MissionGenerator(
                customMissionBuilder_,
                missionPreparer_,
                imageTransferContainer.imageTransferModuleInitializer());

        missionGenerationPresenter_ = new MissionGenerationPresenter(
                flightControlView.generateMissionButton(),
                flightControlView.startMissionButton(),
                missionGenerator_,
                contextManager);

        mapPresenter_ = new MapPresenter(
                contextManager,
                flightControlView.goToMapButton());
    }

    public I_MissionController missionController()
    {
        return missionController_;
    }
}
