package com.dji.sdk.sample.common.integration;


import dji.sdk.missionmanager.DJIMissionManager;

/**
 * Created by eric7 on 2017-02-21.
 */

public class MissionManager {
    DJIMissionManager missionManager_;

    MissionManager(DJIMissionManager missionManager)
    {
        missionManager_ = missionManager;
    }

//    @Override
//    public void fetchMediaList(I_CameraMediaListDownloadListener downloadListener)
//    {
//        mediaManager_.fetchMediaList(downloadListener);
//    }
//
//    @Override
//    public void setCameraModeMediaDownload(I_CompletionCallback callback)
//    {
//        mediaManager_.setCameraModeMediaDownload(callback);
//    }
}
