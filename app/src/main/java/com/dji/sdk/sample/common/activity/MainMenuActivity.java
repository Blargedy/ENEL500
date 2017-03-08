package com.dji.sdk.sample.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dji.sdk.sample.common.presenter.src.MainMenuPresenter;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.view.src.MainMenuView;

import java.io.File;

public class MainMenuActivity extends AppCompatActivity
{
    private ApplicationContextManager contextManager_;
    private MainMenuView mainMenuView_;
    private MainMenuPresenter mainMenuPresenter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextManager_ = new ApplicationContextManager(this);

        sendLogsToFile();

        mainMenuView_ = new MainMenuView(this);

        mainMenuPresenter_ = new MainMenuPresenter(
                mainMenuView_.reconstructLiveButton(),
                mainMenuView_.reconstructLaterButton(),
                contextManager_);

        setContentView(mainMenuView_);
    }

    private void sendLogsToFile()
    {
        try {
            String path = "/storage/emulated/0/Logging.txt";
            File file = new File(path);
            file.delete();
            Process process = Runtime.getRuntime().exec("logcat -d");
            process = Runtime.getRuntime().exec( "logcat -f " + path);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
