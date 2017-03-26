package com.dji.sdk.sample.common.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.presenter.src.MainMenuPresenter;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.src.MainMenuView;

import java.io.File;

public class MainMenuActivity extends AppCompatActivity
{
    private UserPermissionRequester permissionRequester_;
    private MainMenuView mainMenuView_;
    private MainMenuPresenter mainMenuPresenter_;
    volatile float fadeBuffer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sendLogsToFile();


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        Log.d("MapPresenter", "BEFORE PERMISSIONS");
        permissionRequester_ = new UserPermissionRequester();
        permissionRequester_.requestPermissions(this);
        Log.d("MapPresenter", "AFTER PERMISSIONS");

        mainMenuView_ = new MainMenuView(this);

        mainMenuPresenter_ = new MainMenuPresenter(
                mainMenuView_.reconstructLiveButton(),
                mainMenuView_.reconstructLaterButton(),
                this);
        setContentView(mainMenuView_);

        mainMenuView_.droneLogo().setAlpha(0.05f);
        mainMenuView_.reconstructLaterButton().setAlpha(0.0f);
        mainMenuView_.reconstructLiveButton().setAlpha(0.0f);
        mainMenuView_.txtRealTimeQuestion().setAlpha(0.05f);
        mainMenuView_.txt_HydraTitle().setAlpha(0.05f);

        final Handler handler = new Handler();

        final Runnable fadein = new Runnable() {
            public void run() {
                fadeBuffer++;
                final float x = (fadeBuffer/50.0f);
                float newAlpha = mainMenuView_.txtRealTimeQuestion().getAlpha();
                mainMenuView_.txt_HydraTitle().setAlpha(mainMenuView_.txt_HydraTitle().getAlpha() + x*0.004f);
                mainMenuView_.txtRealTimeQuestion().setAlpha(mainMenuView_.txtRealTimeQuestion().getAlpha() + x*0.004f);
                mainMenuView_.droneLogo().setAlpha(mainMenuView_.droneLogo().getAlpha() + x*0.004f);
                if (newAlpha < 0.99f) {
                    handler.post(this);
                }
            }
        };
        mainMenuView_.reconstructLaterButton().setAlpha(1.0f);
        mainMenuView_.reconstructLiveButton().setAlpha(1.0f);
        handler.post(fadein);
    }

    private void sendLogsToFile()
    {
        try {
            String path = "/storage/emulated/0/Logging.txt";
            File file = new File(path);
            file.delete();
            Process process = Runtime.getRuntime().exec("logcat -d");
            process = Runtime.getRuntime().exec("logcat -f " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
