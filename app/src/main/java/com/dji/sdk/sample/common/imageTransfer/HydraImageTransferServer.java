package com.dji.sdk.sample.common.imageTransfer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

//import javax.swing.JFileChooser;


public class HydraImageTransferServer
{
    // This will not be run in android app. Just adding it for source control
    public static void main(String[] args) throws Exception
    {
        ServerSocket serverSocket = new ServerSocket(6789);

        displayComputerIpAddress();

        File imageDirectoryPath = getImageDirectoryPath();

        System.out.println("Waiting for connection...");

        while(true)
        {
            Socket socket = serverSocket.accept();
            InputStream in = socket.getInputStream();

            DataInputStream dataInputStream = new DataInputStream(in);
            String filename = dataInputStream.readUTF();

            if (filename.equals("hello.testing"))
            {
                System.out.println("Connected successfully");
                continue;
            }

            File imageFile = new File(imageDirectoryPath, filename);

            OutputStream out = new FileOutputStream(imageFile);

            byte[] bytes = new byte[16384];

            int count;
            while ((count = in.read(bytes)) > 0)
            {
                out.write(bytes, 0, count);
            }

            out.close();
            in.close();
            socket.close();

            System.out.println("Recieved file: " + filename);
        }
    }

    private static File getImageDirectoryPath()
    {
//        JFileChooser chooser = new JFileChooser();
//        chooser.setCurrentDirectory(new java.io.File("."));
//        chooser.setDialogTitle("Select Image Directory");
//        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        chooser.setAcceptAllFileFilterUsed(false);
//
//        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
//        {
//            File imageDirectoryPath = chooser.getSelectedFile();
//            System.out.println("Image directory chosen: " + imageDirectoryPath.getAbsoluteFile());
//            return imageDirectoryPath;
//        }
//        else
//        {
//            System.exit(-1);
//            return null;
//        }
        return null;
    }

    private static void displayComputerIpAddress() throws Exception
    {
        Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
        System.out.println("Computer's IP addresses:");

        while (networks.hasMoreElements())
        {
            NetworkInterface networkInterface = networks.nextElement();
            if(!networkInterface.isVirtual() && !networkInterface.isLoopback())
            {
                if (networkInterface.getInetAddresses().hasMoreElements())
                {
                    InetAddress address = networkInterface.getInetAddresses().nextElement();

                    if (address instanceof Inet4Address)
                    {
                        System.out.println("\t" + networkInterface.getDisplayName() + " : " + address.getHostAddress());
                    }
                }
            }
        }
    }
}
