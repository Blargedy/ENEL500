package com.dji.sdk.sample.common.imageTransfer.src;

import android.util.Log;

import com.dji.sdk.sample.common.container.ImageTransferContainer;
import com.dji.sdk.sample.common.imageTransfer.api.I_AndroidToPcImageCopier;
import com.dji.sdk.sample.common.utility.ApplicationSettingsManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Julia on 2017-03-01.
 */

public class AndroidToPcImageCopier implements I_AndroidToPcImageCopier
{
    private static final String TAG = "DroneToAndroidImageDownloader";

    private String pcIpAddress_;
    private Queue<String> imagesToTransfer_;

    public AndroidToPcImageCopier(
            ApplicationSettingsManager applicationSettingsManager)
    {
        pcIpAddress_ = applicationSettingsManager.getPcIpAddressFromSettings();
        imagesToTransfer_ = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addImageToPcCopyQueue(String androidImagePath)
    {
        imagesToTransfer_.add(androidImagePath);
    }

    @Override
    public void run()
    {
        while(!Thread.currentThread().isInterrupted())
        {
            copyNextImageInBuffer();
        }
        emptyBuffer();
    }

    private void copyNextImageInBuffer()
    {
        if (!imagesToTransfer_.isEmpty())
        {
            String androidImagePath = imagesToTransfer_.remove();

            try {
                copyFileToPc(androidImagePath);
                //deleteFileOnAndroid(androidImagePath);
            } catch (IOException e) {
                Log.e(TAG, "Failed to send " + androidImagePath + ": " + e.toString());
            }
        }
        else
        {
            Thread.yield();
        }
    }

    private void emptyBuffer()
    {
        while(!imagesToTransfer_.isEmpty())
        {
            copyNextImageInBuffer();
        }
    }

    private void copyFileToPc(String androidImagePath) throws IOException
    {
        Socket socket = new Socket(pcIpAddress_, 6789);
        File image = new File(androidImagePath);

        InputStream in = new FileInputStream(image);
        OutputStream out = socket.getOutputStream();

        DataOutputStream dataOutputStream = new DataOutputStream(out);
        dataOutputStream.writeUTF(image.getName());

        int count;
        byte[] buffer = new byte[16384];
        while ((count = in.read(buffer)) > 0)
        {
            out.write(buffer, 0, count);
        }

        in.close();
        out.close();
        socket.close();
    }

    private void deleteFileOnAndroid(String androidImagePath)
    {
        File imageFile = new File(androidImagePath);
        imageFile.delete();
    }
}
