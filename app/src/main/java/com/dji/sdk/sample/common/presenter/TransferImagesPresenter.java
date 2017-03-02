package com.dji.sdk.sample.common.presenter;

import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.imageTransfer.I_ImageTransferer;

/**
 * Created by Julia on 2017-01-25.
 */

public class TransferImagesPresenter implements View.OnClickListener
{
    private Button transferImagesButton_;
    private I_ImageTransferer imageTransferer_;

    public TransferImagesPresenter(
            Button shootPhotoButton,
            I_ImageTransferer imageTransferer)
    {
        transferImagesButton_ = shootPhotoButton;
        transferImagesButton_.setOnClickListener(this);
        imageTransferer_ = imageTransferer;
    }

    @Override
    public void onClick(View view)
    {
        imageTransferer_.transferNewImagesFromDrone();
    }
}
