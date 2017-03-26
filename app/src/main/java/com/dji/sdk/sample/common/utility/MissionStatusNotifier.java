package com.dji.sdk.sample.common.utility;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Julia on 2017-03-22.
 */

public class MissionStatusNotifier implements I_MissionStatusNotifier
{
    private Context context_;

    public MissionStatusNotifier(
            Context context)
    {
        context_ = context;
    }

    @Override
    public void notifyStatusChanged(String error)
    {
        Intent intent = new Intent(BroadcastIntentNames.ERROR_OCCURRED);
        intent.putExtra(IntentExtraKeys.ERROR_MESSAGE, error);
        LocalBroadcastManager.getInstance(context_).sendBroadcast(intent);
    }
}
