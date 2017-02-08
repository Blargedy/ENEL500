package com.dji.sdk.sample.common.container;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.mission.MissionGenerator;
import com.dji.sdk.sample.common.presenter.MissionGenerationPresenter;
import com.dji.sdk.sample.common.presenter.MissionOperationPresenter;
import com.dji.sdk.sample.common.presenter.ShootPhotoPresenter;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.SimpleDemoView;

/**
 * Created by Julia on 2017-02-04.
 */

public class MissionContainer
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;

    private MissionGenerator missionGenerator_;
    private MissionGenerationPresenter missionGenerationPresenter_;
    private MissionOperationPresenter missionOperationPresenter_;
    private ShootPhotoPresenter shootPhotoPresenter_;

    public MissionContainer(
            SimpleDemoView simpleDemoView,
            I_ApplicationContextManager contextManager)
    {
        initialMissionModel_ = new InitialMissionModel();
        generatedMissionModel_ = new GeneratedMissionModel();

        missionGenerator_ = new MissionGenerator(initialMissionModel_, generatedMissionModel_);
        missionGenerationPresenter_ = new MissionGenerationPresenter(
                simpleDemoView.generateMissionButton(),
                simpleDemoView.startMissionButton(),
                missionGenerator_);

        missionOperationPresenter_ = new MissionOperationPresenter(
                simpleDemoView.startMissionButton());

        shootPhotoPresenter_ = new ShootPhotoPresenter(
                contextManager,
                simpleDemoView.shootPhotoButton());
    }
}
