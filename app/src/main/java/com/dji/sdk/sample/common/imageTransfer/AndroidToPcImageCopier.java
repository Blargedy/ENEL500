package com.dji.sdk.sample.common.imageTransfer;

import android.util.Log;

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

public class AndroidToPcImageCopier implements
        I_AndroidToPcImageCopier,
        Runnable
{
    private static final String TAG = "DroneToAndroidImageDownloader";

    private String pcIpAddress_;
    private Queue<String> imagesToTransfer_;

    public AndroidToPcImageCopier(
            String pcIpAddress)
    {
        pcIpAddress_ = pcIpAddress;
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
        while(true)
        {
            if (!imagesToTransfer_.isEmpty())
            {
                String androidImagePath = imagesToTransfer_.remove();

                try {
                    copyFile(androidImagePath);
                } catch (IOException e) {
                    Log.e(TAG, "Failed to send " + androidImagePath + ": " + e.toString());
                }
            }
            else
            {
                Thread.yield();
            }
        }
    }

    private void copyFile(String androidImagePath) throws IOException {
        Socket socket = new Socket(pcIpAddress_, 6789);
        File image = new File(androidImagePath);

        InputStream in = new FileInputStream(image);
        OutputStream out = socket.getOutputStream();

        PrintWriter writer = new PrintWriter(out);
        writer.println(image.getName());
        writer.flush();

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
}
