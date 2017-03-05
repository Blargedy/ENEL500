package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.I_MissionController;
import com.dji.sdk.sample.common.mission.MissionController;
import com.dji.sdk.sample.common.mission.MissionGenerator;
import com.dji.sdk.sample.common.mission.MissionStepCompletionCallback;
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

    private ImageTransferContainer imageTransferContainer_;

    private MissionStepCompletionCallback missionStepCompletionCallback_;

    private MissionGenerator missionGenerator_;
    private MissionGenerationPresenter missionGenerationPresenter_;

    private MapPresenter mapPresenter_;

    public MissionContainer(
            IntegrationLayerContainer integrationLayerContainer,
            FlightControlView flightControlView,
            I_ApplicationContextManager contextManager,
            String pcIpAddress)
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

        imageTransferContainer_ = new ImageTransferContainer(
                contextManager,
                integrationLayerContainer.mediaManagerSource(),
                integrationLayerContainer.mediaDataFetcher(),
                missionController_,
                flightControlView,
                pcIpAddress);

        missionStepCompletionCallback_ = new MissionStepCompletionCallback(
                missionController_,
                imageTransferContainer_.imageTransferer(),
                contextManager);

        missionGenerator_ = new MissionGenerator(
                contextManager,
                initialMissionModel_,
                generatedMissionModel_,
                integrationLayerContainer.missionManagerSource(),
                integrationLayerContainer.flightControllerSource(),
                missionStepCompletionCallback_,
                imageTransferContainer_.imageTransferModuleInitializer());

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
