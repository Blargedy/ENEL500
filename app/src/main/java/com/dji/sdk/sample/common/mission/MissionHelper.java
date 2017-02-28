package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;

/**
 * Created by Matthew on 2017-02-27.
 */

public class MissionHelper {

    public static DJICommonCallbacks.DJICompletionCallback completionCallback(final I_ApplicationContextManager context, final String success, final String failure)
    {
        return new DJICommonCallbacks.DJICompletionCallback()
        {
            @Override
            public void onResult(DJIError error) {
                if (error == null)
                {
                    Toast.makeText(context.getApplicationContext(), success, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context.getApplicationContext(), failure + error.getDescription(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}
