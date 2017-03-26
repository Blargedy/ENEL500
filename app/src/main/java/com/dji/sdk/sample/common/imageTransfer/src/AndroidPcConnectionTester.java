package com.dji.sdk.sample.common.imageTransfer.src;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Julia on 2017-03-26.
 */

public class AndroidPcConnectionTester implements Runnable
{
    private String pcIpAddress_;
    private boolean isConnectionSuccessful_;

    @Override
    public void run()
    {
        try {
            Socket socket = new Socket(pcIpAddress_, 6789);

            OutputStream out = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            dataOutputStream.writeUTF("hello.testing");

            out.close();
            socket.close();
            isConnectionSuccessful_ = true;
        } catch (IOException e) {
            e.printStackTrace();
            isConnectionSuccessful_ = false;
        }
    }

    public void setPcIpAddress(String pcIpAddress)
    {
        pcIpAddress_ = pcIpAddress;
    }

    public boolean isConnectionSuccessful()
    {
        return isConnectionSuccessful_;
    }
}
