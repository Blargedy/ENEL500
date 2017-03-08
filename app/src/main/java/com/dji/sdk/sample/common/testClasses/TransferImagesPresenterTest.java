package com.dji.sdk.sample.common.testClasses;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dji.sdk.sample.common.imageTransfer.api.I_DroneMediaListInitializer;
import com.dji.sdk.sample.common.imageTransfer.callbacks.I_ImageTransferCompletionCallback;
import com.dji.sdk.sample.common.imageTransfer.api.I_ImageTransferer;
import com.dji.sdk.sample.common.imageTransfer.src.AndroidToPcImageCopier;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

/**
 * Created by Julia on 2017-01-25.
 */

public class TransferImagesPresenterTest implements
        View.OnClickListener,
        I_ImageTransferCompletionCallback
{
    private Button transferImagesButton_;
    private I_ApplicationContextManager contextManager_;
    private I_ImageTransferer imageTransferer_;
    private AndroidToPcImageCopier copier_;
    private I_DroneMediaListInitializer mediaListInitializer_;

    public TransferImagesPresenterTest(
            I_ImageTransferTestView view,
            I_ApplicationContextManager contextManager,
            I_ImageTransferer imageTransferer,
            AndroidToPcImageCopier copier,
            I_DroneMediaListInitializer mediaListInitializer)
    {
        transferImagesButton_ = view.transferImagesButton();
        transferImagesButton_.setOnClickListener(this);
        contextManager_ = contextManager;
        imageTransferer_ = imageTransferer;
        copier_ = copier;
        mediaListInitializer_ = mediaListInitializer;
    }

    @Override
    public void onClick(View view)
    {
        mediaListInitializer_.initializeDroneMediaList(new ArrayList<DJIMedia>());
        copier_.start();
        imageTransferer_.transferNewImagesFromDrone(this);
    }

    @Override
    public void onImageTransferCompletion()
    {
        copier_.interrupt();
        try {
            copier_.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(contextManager_.getApplicationContext(), "Success: transferred photos" , Toast.LENGTH_SHORT).show();
    }
}
