package com.dji.sdk.sample.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dji.sdk.sample.R;

/**
 * Created by Julia on 2017-02-08.
 */

public class MainMenuView
        extends RelativeLayout
{
    private Button reconstructLiveButton_;
    private Button reconstructLaterButton_;

    public MainMenuView(
            Context context) {
        super(context);
        initUI();
    }

    public MainMenuView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MainMenuView(
            Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    public Button reconstructLiveButton()
    {
        return reconstructLiveButton_;
    }

    public Button reconstructLaterButton()
    {
        return reconstructLaterButton_;
    }

    private void initUI()
    {
        inflate(getContext(), R.layout.main_menu, this);

        reconstructLiveButton_ = (Button) findViewById(R.id.btn_reconstruct_live);
        reconstructLaterButton_ = (Button) findViewById(R.id.btn_reconstruct_later);
    }
}