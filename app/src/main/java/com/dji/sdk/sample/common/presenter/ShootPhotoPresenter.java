package com.dji.sdk.sample.common.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Button;
import android.widget.Toast;

import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.DJISampleApplication;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;
import dji.sdk.camera.DJIPlaybackManager;

/**
 * Created by Julia on 2017-01-25.
 */

public class ShootPhotoPresenter
        extends BroadcastReceiver
        implements DJIPlaybackManager.CameraFileDownloadCallback
{
    I_ApplicationContextManager contextManager_;
    Button shootPhotoButton_;

    public ShootPhotoPresenter(
            I_ApplicationContextManager contextManager,
            Button shootPhotoButton)
    {
        LocalBroadcastManager.getInstance(contextManager.getApplicationContext()).registerReceiver(
                this, new IntentFilter(BroadcastIntentNames.SHOOT_PHOTO_BUTTON_PRESSED));
        contextManager_ = contextManager;
        shootPhotoButton_ = shootPhotoButton;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        shootPhotoButton_.setEnabled(false);
        shootPhoto();
        //downloadPhoto();
    }

    private void shootPhoto()
    {
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

    private void downloadPhoto()
    {
        DJICamera camera = DJISampleApplication.getProductInstance().getCamera();
        camera.setCameraMode(DJICameraSettingsDef.CameraMode.Playback, new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (error == null) {
                    Toast.makeText(contextManager_.getApplicationContext(),
                            "change camera mode: success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(contextManager_.getApplicationContext(),
                            error.getDescription(), Toast.LENGTH_LONG).show();
                }
            }
        });

        DJIPlaybackManager playbackManager = camera.getPlayback();

//        DJICameraError error = playbackManager.selectAllFiles();
//        if (error == null) {
//            Toast.makeText(contextManager_.getApplicationContext(),
//                    "select photos: success", Toast.LENGTH_SHORT).show();
//            downloadPhoto();
//        } else {
//            Toast.makeText(contextManager_.getApplicationContext(),
//                    error.getDescription(), Toast.LENGTH_LONG).show();
//        }

//        File destination = contextManager_.getApplicationContext().getFilesDir();
//
//        playbackManager.downloadSelectedFiles(destination, this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {
        Toast.makeText(contextManager_.getApplicationContext(),
                "download photos: success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(contextManager_.getApplicationContext(),
                e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProgressUpdate(int i) {

    }
}
