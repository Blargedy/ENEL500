package com.dji.sdk.sample.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dji.sdk.sample.common.presenter.MainMenuPresenter;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.view.MainMenuView;

public class MainMenuActivity extends AppCompatActivity
{
    private ApplicationContextManager contextManager_;
    private MainMenuView mainMenuView_;
    private MainMenuPresenter mainMenuPresenter_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextManager_ = new ApplicationContextManager(this);

        mainMenuView_ = new MainMenuView(this);

        mainMenuPresenter_ = new MainMenuPresenter(
                mainMenuView_.reconstructLiveButton(),
                mainMenuView_.reconstructLaterButton(),
                contextManager_);

        setContentView(mainMenuView_);
    }

}
