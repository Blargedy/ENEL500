package com.dji.sdk.sample.common.integration.src;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.integration.api.I_Gimbal;

import dji.common.gimbal.DJIGimbalAngleRotation;
import dji.common.gimbal.DJIGimbalRotateAngleMode;
import dji.common.gimbal.DJIGimbalRotateDirection;
import dji.sdk.gimbal.DJIGimbal;

/**
 * Created by Julia on 2017-03-25.
 */

public class Gimbal implements I_Gimbal
{
    private DJIGimbal gimbal_;

    public Gimbal(
            DJIGimbal gimbal)
    {
        gimbal_ = gimbal;
    }

    @Override
    public void pointGimbalDown(I_CompletionCallback callback)
    {
        DJIGimbalAngleRotation pitchRotation = new DJIGimbalAngleRotation(
                true, -90.0f, DJIGimbalRotateDirection.Clockwise);
        DJIGimbalAngleRotation yawAndRollRotation = new DJIGimbalAngleRotation(
                false, 0.0f, DJIGimbalRotateDirection.Clockwise);

        gimbal_.rotateGimbalByAngle(
                DJIGimbalRotateAngleMode.AbsoluteAngle,
                pitchRotation,
                yawAndRollRotation,
                yawAndRollRotation,
                callback);
    }
}
