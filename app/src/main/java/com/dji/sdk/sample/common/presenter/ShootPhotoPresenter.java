package com.dji.sdk.sample.common.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.DJISampleApplication;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.view.I_ShootPhotoView;

import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;

/**
 * Created by Julia on 2017-01-25.
 */

public class ShootPhotoPresenter extends BroadcastReceiver
{
    I_ApplicationContextManager contextManager_;
    I_ShootPhotoView view_;

    public ShootPhotoPresenter(
            I_ApplicationContextManager contextManager,
            I_ShootPhotoView view)
    {
        LocalBroadcastManager.getInstance(contextManager.getApplicationContext()).registerReceiver(
                this, new IntentFilter(BroadcastIntentNames.SHOOT_PHOTO_BUTTON_PRESSED));
        contextManager_ = contextManager;
        view_ = view;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        view_.shootPhotoButton().setEnabled(false);
        shootPhoto();
    }

    private void shootPhoto()
    {
        DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.ShootPhoto;
        DJICamera camera = DJISampleApplication.getProductInstance().getCamera();
        if (camera != null) {
            DJICameraSettingsDef.CameraShootPhotoMode photoMode = DJICameraSettingsDef.CameraShootPhotoMode.Single;
            camera.startShootPhoto(photoMode, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    if (error == null) {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "take photo: success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                error.getDescription(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
