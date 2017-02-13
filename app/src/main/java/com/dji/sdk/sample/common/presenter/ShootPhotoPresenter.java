package com.dji.sdk.sample.common.presenter;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dji.sdk.sample.common.integration.DJISampleApplication;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import java.io.File;
import java.util.ArrayList;

import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;
import dji.sdk.camera.DJIMedia;
import dji.sdk.camera.DJIMediaManager;

/**
 * Created by Julia on 2017-01-25.
 */

public class ShootPhotoPresenter implements View.OnClickListener
{
    I_ApplicationContextManager contextManager_;
    Button shootPhotoButton_;

    public ShootPhotoPresenter(
            I_ApplicationContextManager contextManager,
            Button shootPhotoButton)
    {
        contextManager_ = contextManager;
        shootPhotoButton_ = shootPhotoButton;
        shootPhotoButton_.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == shootPhotoButton_.getId())
        {
            downloadMediaList();
        }
    }

    private void changeCameraMode(final DJICameraSettingsDef.CameraMode cameraMode)
    {
        DJICamera camera = DJISampleApplication.getProductInstance().getCamera();
        if (camera != null)
        {
            camera.setCameraMode(cameraMode, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    String message;
                    if (djiError != null)
                    {
                        message = "Camera: could not switch to camera mode " + cameraMode.toString();
                    }
                    else
                    {
                        message = "Success: switched to camera mode " + cameraMode.toString();
                    }
                    Toast.makeText(contextManager_.getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void downloadMediaList()
    {
        DJICamera camera = DJISampleApplication.getProductInstance().getCamera();
        if (camera != null)
        {
            final DJIMediaManager mediaManager = camera.getMediaManager();
            mediaManager.setCameraModeMediaDownload(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    if (djiError == null) {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Success: Switched to download media mode", Toast.LENGTH_SHORT).show();

                        mediaManager.fetchMediaList(new DJIMediaManager.CameraDownloadListener<ArrayList<DJIMedia>>() {
                            @Override
                            public void onStart() {
                                Toast.makeText(contextManager_.getApplicationContext(),
                                        "Starting media list download", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onRateUpdate(long l, long l1, long l2) {

                            }

                            @Override
                            public void onProgress(long l, long l1) {

                            }

                            @Override
                            public void onSuccess(ArrayList<DJIMedia> djiMediaList) {
                                Toast.makeText(contextManager_.getApplicationContext(),
                                        "Success: Media list download | Media list length=" + djiMediaList.size(), Toast.LENGTH_SHORT).show();
                                downloadImage(djiMediaList);
                            }

                            @Override
                            public void onFailure(DJIError djiError) {
                                Toast.makeText(contextManager_.getApplicationContext(),
                                        "Failure: Media list download " + djiError.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Failure: Switched to download media mode " + djiError.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void downloadImage(ArrayList<DJIMedia> djiMediaList)
    {
        boolean found = false;
        for (DJIMedia media : djiMediaList)
        {
            if (!found && media.getMediaType() == DJIMedia.MediaType.JPG)
            {
                found = true;
                File destination = contextManager_.getApplicationContext().getExternalFilesDir(null);

                Toast.makeText(contextManager_.getApplicationContext(),
                        "Destination=" + destination.getAbsolutePath(), Toast.LENGTH_LONG).show();

                media.fetchMediaData(destination, "TestPhotoDownload", new DJIMediaManager.CameraDownloadListener<String>() {
                    @Override
                    public void onStart() {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Starting to download image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRateUpdate(long l, long l1, long l2) {

                    }

                    @Override
                    public void onProgress(long l, long l1) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Success: Downloaded image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(DJIError djiError) {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Failure: download image " + djiError.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        if (!found)
        {
            Toast.makeText(contextManager_.getApplicationContext(),
                    "Failure: Could not find image to download", Toast.LENGTH_LONG).show();
        }
    }
}
