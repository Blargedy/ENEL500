package com.dji.sdk.sample.common.mission.src;

import android.util.Log;

import com.dji.sdk.sample.common.droneState.src.CameraState;
import com.dji.sdk.sample.common.integration.api.I_CameraSource;
import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.mission.api.I_WaypointImageShooter;
import com.dji.sdk.sample.common.utility.I_MissionStatusNotifier;

import dji.common.error.DJIError;

/**
 * Created by Julia on 2017-03-25.
 */

public class WaypointImageShooter implements
        I_WaypointImageShooter,
        I_CompletionCallback
{
    private static final String TAG = "HydraWaypointImageShooter";

    private I_MissionStatusNotifier missionStatusNotifier_;
    private I_CameraSource cameraSource_;
    private CameraState cameraState_;

    private int waypointIndex_;

    public WaypointImageShooter(
            I_MissionStatusNotifier missionStatusNotifier,
        I_CameraSource cameraSource,
        CameraState cameraState)
    {
        missionStatusNotifier_ = missionStatusNotifier;
        cameraSource_ = cameraSource;
        cameraState_ = cameraState;
    }

    @Override
    synchronized public void shootPhotoOnWaypoint(int waypointIndex)
    {
        while (cameraState_.cameraSystemState().isShootingSinglePhoto() ||
                cameraState_.cameraSystemState().isStoringPhoto())
        {
        }

        waypointIndex_ = waypointIndex;
        cameraSource_.getCamera().shootSinglePhoto(this);
    }

    @Override
    public void onResult(DJIError error)
    {
        if (error != null)
        {
            String message = "Failed to take photo on waypoint " + waypointIndex_ + " : " + error.getDescription();
            Log.d(TAG, message);
            missionStatusNotifier_.notifyStatusChanged(message);
        }
    }
}
