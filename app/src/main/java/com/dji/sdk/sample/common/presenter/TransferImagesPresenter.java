package com.dji.sdk.sample.common.presenter;

import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.imageTransfer.I_DroneMediaListInitializer;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferer;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-01-25.
 */

public class TransferImagesPresenter implements View.OnClickListener
{
    private Button transferImagesButton_;
    private I_ImageTransferer imageTransferer_;
    private I_DroneMediaListInitializer mediaListInitializer_;

    public TransferImagesPresenter(
            Button shootPhotoButton,
            I_ImageTransferer imageTransferer,
            I_DroneMediaListInitializer mediaListInitializer)
    {
        transferImagesButton_ = shootPhotoButton;
        transferImagesButton_.setOnClickListener(this);
        imageTransferer_ = imageTransferer;
        mediaListInitializer_ = mediaListInitializer;
    }

    @Override
    public void onClick(View view)
    {
        mediaListInitializer_.initializeDroneMediaList(new ArrayList<DJIMedia>());
        imageTransferer_.transferNewImagesFromDrone();
    }
}
