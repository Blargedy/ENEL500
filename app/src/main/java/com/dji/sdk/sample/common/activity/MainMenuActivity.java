package com.dji.sdk.sample.common.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.container.ImageTransferContainer;
import com.dji.sdk.sample.common.container.IntegrationLayerContainer;
import com.dji.sdk.sample.common.presenter.src.MainMenuPresenter;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.view.src.MainMenuView;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static java.lang.Thread.currentThread;

public class MainMenuActivity extends AppCompatActivity {
    private MainMenuView mainMenuView_;
    private MainMenuPresenter mainMenuPresenter_;
    volatile float fadeBuffer = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendLogsToFile();

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


    private void sendLogsToFile() {
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
