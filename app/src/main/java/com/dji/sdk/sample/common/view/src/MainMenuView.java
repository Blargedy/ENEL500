package com.dji.sdk.sample.common.view.src;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dji.sdk.sample.R;

/**
 * Created by Julia on 2017-02-08.
 */

public class MainMenuView
        extends LinearLayout {
    private Button reconstructLiveButton_;
    private Button reconstructLaterButton_;
    private TextView txtRealTimeQuestion_;
    private ImageView droneLogo_;
    private TextView txt_HydraTitle_;

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

    public Button reconstructLiveButton() {
        return reconstructLiveButton_;
    }

    public Button reconstructLaterButton() {
        return reconstructLaterButton_;
    }

    public TextView txtRealTimeQuestion() {
        return txtRealTimeQuestion_;
    }

    public ImageView droneLogo() {return droneLogo_;
    }

    public TextView txt_HydraTitle() {
        return txt_HydraTitle_;
    }

    private void initUI() {
        inflate(getContext(), R.layout.main_menu, this);

        reconstructLiveButton_ = (Button) findViewById(R.id.btn_reconstruct_live);
        reconstructLaterButton_ = (Button) findViewById(R.id.btn_reconstruct_later);

        txtRealTimeQuestion_ = (TextView) findViewById(R.id.txtRealTimeQuestion);
        droneLogo_ = (ImageView) findViewById(R.id.imageDroneLogo);
        txt_HydraTitle_ = (TextView)findViewById(R.id.txt_HydraTitle);
    }
}