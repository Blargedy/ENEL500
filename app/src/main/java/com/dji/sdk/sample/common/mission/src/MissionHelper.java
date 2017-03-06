package com.dji.sdk.sample.common.mission.src;

import android.widget.Toast;

import com.dji.sdk.sample.common.integration.api.I_CompletionCallback;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.common.error.DJIError;

/**
 * Created by Matthew on 2017-02-27.
 */

public class MissionHelper{

    public static I_CompletionCallback completionCallback(final I_ApplicationContextManager context, final String success, final String failure)
    {
        return new I_CompletionCallback()
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
