package com.dji.sdk.sample.common.integration;

import android.util.Log;

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.camera.DJIMediaManager;
import dji.sdk.missionmanager.DJIMissionManager;
import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by eric7 on 2017-02-21.
 */

public class MissionManagerSource implements I_MissionManagerSource{

    private static final String TAG = "MissionManagerSource";

    @Override
    public I_MissionManager getMissionManager()
    {
        DJIBaseProduct product = DJISampleApplication.getProductInstance();
        if (product != null)
        {
            Log.d(TAG, "Product not null");

            DJIMissionManager missionManager = product.getMissionManager();

            if(missionManager != null)
            {
                Log.d(TAG, "Mission manager not null");

                //return new MissionManager(missionManager);
            }
        }
        return null;
    }
}
