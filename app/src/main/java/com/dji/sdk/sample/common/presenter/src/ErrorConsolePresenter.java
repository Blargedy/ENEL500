package com.dji.sdk.sample.common.presenter.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.StringDef;
import android.support.v4.content.LocalBroadcastManager;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;
import com.dji.sdk.sample.common.view.api.I_ErrorConsoleView;

import static com.dji.sdk.sample.common.utility.IntentExtraKeys.WAYPOINT_INDEX;

/**
 * Created by Julia on 2017-03-22.
 */

public class ErrorConsolePresenter
{
    private I_ErrorConsoleView view_;
    private BroadcastReceiver receiver_;

    public ErrorConsolePresenter(
            Context context,
            I_ErrorConsoleView view)
    {
        view_ = view;

        registerMissionErrorOccurredReceiver(context);
    }

    private void registerMissionErrorOccurredReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.ERROR_OCCURRED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String errorMessage = intent.getStringExtra(IntentExtraKeys.ERROR_MESSAGE);
                displayErrorMessageInConsole(errorMessage);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver_, filter);
    }

    private void displayErrorMessageInConsole(String errorMessage)
    {
        view_.errorConsoleText().setText(errorMessage);
    }
}
