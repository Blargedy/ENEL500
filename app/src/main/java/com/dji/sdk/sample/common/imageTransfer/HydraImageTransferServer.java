package com.dji.sdk.sample.common.imageTransfer;

/**
 * Created by Julia on 2017-03-04.
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HydraImageTransferServer {

    // Note that this is not to be run on the Android device
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(6789);

        while(true)
        {
            Socket socket = serverSocket.accept();
            System.out.println("Connected");

            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String filename = reader.readLine();

            OutputStream out = new FileOutputStream("C:\\Users\\Julia\\Documents\\2016-2017\\ENEL500\\HydraPhotos\\" + filename);

            byte[] bytes = new byte[16384];

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            out.close();
            in.close();
            socket.close();
        }
    }
}

