package com.dji.sdk.sample.common.presenter.src;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.dji.sdk.sample.common.activity.FlightControlActivity;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.vision.text.Text;

/**
 * Created by Julia on 2017-02-08.
 */

public class MainMenuPresenter implements View.OnClickListener {
    private Button reconstructLiveButton_;
    private Button reconstructLaterButton_;
    private Context context_;

    public MainMenuPresenter(
            Button reconstructLiveButton,
            Button reconstructLaterButton, Context context) {
        this.reconstructLiveButton_ = reconstructLiveButton;
        this.reconstructLiveButton_.setOnClickListener(this);

        this.reconstructLaterButton_ = reconstructLaterButton;
        this.reconstructLaterButton_.setOnClickListener(this);
        context_ = context;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == reconstructLiveButton_.getId()) {
            // todo with Berni: Add screen with instructions and to test connection
            Intent reconstructLiveIntent = new Intent(context_, FlightControlActivity.class);
            reconstructLiveIntent.putExtra(IntentExtraKeys.IS_LIVE_MODE_ENABLED, new Boolean(true));
            reconstructLiveIntent.putExtra(IntentExtraKeys.PC_IP_ADDRESS, "192.168.1.203");

            context_.startActivity(reconstructLiveIntent);
        } else if (view.getId() == reconstructLaterButton_.getId()) {
            Intent reconstructLaterIntent = new Intent(context_, FlightControlActivity.class);
            reconstructLaterIntent.putExtra(IntentExtraKeys.IS_LIVE_MODE_ENABLED, new Boolean(false));

            context_.startActivity(reconstructLaterIntent);
        }
    }
}
