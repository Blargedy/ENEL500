package com.dji.sdk.sample.common.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.activity.FlightControlActivity;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

/**
 * Created by Julia on 2017-02-08.
 */

public class MainMenuPresenter implements View.OnClickListener
{
    private Button reconstructLiveButton_;
    private Button reconstructLaterButton_;
    private I_ApplicationContextManager contextManager_;

    public MainMenuPresenter(
            Button reconstructLiveButton,
            Button reconstructLaterButton,
            I_ApplicationContextManager contextManager)
    {
        reconstructLiveButton_ = reconstructLiveButton;
        reconstructLiveButton_.setOnClickListener(this);

        reconstructLaterButton_ = reconstructLaterButton;
        reconstructLaterButton_.setOnClickListener(this);

        contextManager_ = contextManager;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == reconstructLiveButton_.getId())
        {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        String user = "Julia";
                        String pass ="covert";

                        String sharedFolder="HydraPhotos";
                        String path="smb://DESKTOP-DUPKRJE/"+sharedFolder+"/test.txt";
                        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, pass);


                        SmbFile smbFile = null;
                        try {
                            smbFile = new SmbFile(path,auth);
                        } catch (MalformedURLException e) {
                            Log.d("MainMenuPresenter", e.toString());
                        }


                        SmbFileOutputStream smbfos = null;
                        try {
                            smbfos = new SmbFileOutputStream(smbFile);
                        } catch (SmbException e) {
                            Log.d("MainMenuPresenter", e.toString());
                        } catch (MalformedURLException e) {
                            Log.d("MainMenuPresenter", e.toString());
                        } catch (UnknownHostException e) {
                            Log.d("MainMenuPresenter", e.toString());
                        }


                        try {
                            smbfos.write("testing....and writing to a file".getBytes());
                        } catch (IOException e) {
                            Log.d("MainMenuPresenter", e.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // Unfortunately can't connect to the network using the code above. Needs more work
            //thread.start();
        }
        else if (view.getId() == reconstructLaterButton_.getId())
        {
            Context context = contextManager_.getApplicationContext();
            Intent reconstructLaterIntent = new Intent(
                    context,FlightControlActivity.class);
            reconstructLaterIntent.putExtra("isLiveModeEnabled", new Boolean(false));

            context.startActivity(reconstructLaterIntent);
        }
    }
}
