package com.dji.sdk.sample.common.integration.src;

import android.util.Log;

import com.dji.sdk.sample.common.integration.api.I_MissionManager;
import com.dji.sdk.sample.common.integration.api.I_MissionManagerSource;

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.missionmanager.DJIMissionManager;

/**
 * Created by eric7 on 2017-02-21.
 */

public class MissionManagerSource implements I_MissionManagerSource {

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

                return new MissionManager(missionManager);
            }
        }
        return null;
    }
}
