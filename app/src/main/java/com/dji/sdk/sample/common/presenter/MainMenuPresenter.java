package com.dji.sdk.sample.common.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.activity.FlightControlActivity;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

/**
 * Created by Julia on 2017-02-08.
 */

public class MainMenuPresenter implements View.OnClickListener
{
    private Button reconstructLiveButton_;
    private Button reconstructLaterButton_;
    private I_ApplicationContextManager contextManager_;

    public MainMenuPresenter(
            Button reconstructLiveButton,
            Button reconstructLaterButton,
            I_ApplicationContextManager contextManager)
    {
        reconstructLiveButton_ = reconstructLiveButton;
        reconstructLiveButton_.setOnClickListener(this);

        reconstructLaterButton_ = reconstructLaterButton;
        reconstructLaterButton_.setOnClickListener(this);

        contextManager_ = contextManager;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == reconstructLiveButton_.getId())
        {

        }
        else if (view.getId() == reconstructLaterButton_.getId())
        {
            Context context = contextManager_.getApplicationContext();
            Intent reconstructLaterIntent = new Intent(
                    context,FlightControlActivity.class);
            reconstructLaterIntent.putExtra("isLiveModeEnabled", new Boolean(false));

            context.startActivity(reconstructLaterIntent);
        }
    }
}
