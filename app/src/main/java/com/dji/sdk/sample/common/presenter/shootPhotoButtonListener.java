package com.dji.sdk.sample.common.presenter;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.I_ShootPhotoView;

/**
 * Created by Julia on 2017-01-25.
 */

public class ShootPhotoButtonListener implements View.OnClickListener {
    I_ApplicationContextManager contextManager_;
    I_ShootPhotoView shootPhotoView_;

    public ShootPhotoButtonListener(
            I_ApplicationContextManager contextManager,
            I_ShootPhotoView shootPhotoView)
    {
        shootPhotoView_ = shootPhotoView;
        contextManager_ = contextManager;

        shootPhotoView_.shootPhotoButton().setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(BroadcastIntentNames.SHOOT_PHOTO_BUTTON_PRESSED);
        LocalBroadcastManager.getInstance(contextManager_.getApplicationContext())
                .sendBroadcast(intent);
    }
}
