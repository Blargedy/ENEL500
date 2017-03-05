package com.dji.sdk.sample.common.presenter;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dji.sdk.sample.common.imageTransfer.I_DroneMediaListInitializer;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferCompletionCallback;
import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferer;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-01-25.
 */

public class TransferImagesPresenter implements
        View.OnClickListener,
        I_ImageTransferCompletionCallback
{
    private Button transferImagesButton_;
    private I_ApplicationContextManager contextManager_;
    private I_ImageTransferer imageTransferer_;
    private I_DroneMediaListInitializer mediaListInitializer_;

    public TransferImagesPresenter(
            Button shootPhotoButton,
            I_ApplicationContextManager contextManager,
            I_ImageTransferer imageTransferer,
            I_DroneMediaListInitializer mediaListInitializer)
    {
        transferImagesButton_ = shootPhotoButton;
        transferImagesButton_.setOnClickListener(this);
        contextManager_ = contextManager;
        imageTransferer_ = imageTransferer;
        mediaListInitializer_ = mediaListInitializer;
    }

    @Override
    public void onClick(View view)
    {
        mediaListInitializer_.initializeDroneMediaList(new ArrayList<DJIMedia>());
        imageTransferer_.transferNewImagesFromDrone(this);
    }

    @Override
    public void onImageTransferCompletion()
    {
        Toast.makeText(contextManager_.getApplicationContext(), "Success: transferred photos" , Toast.LENGTH_SHORT).show();
    }
}
