package com.dji.sdk.sample.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.dji.sdk.sample.R;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Julia on 2017-01-15.
 */

public class MapView extends RelativeLayout
{
    private Button btn_mainButton_;
    private Switch sw_hoverNow_;
    private SeekBar pbar_surveyAreaHeight_;
    private SeekBar pbar_surveyAreaWidth_;

    private TextView txt_console_;
    private SeekBar pbar_surveyProgressTracking_;
    private TextView txt_surveyAreaHeight_;
    private TextView txt_surveyAreaWidth_;

    public MapView(
            Context context) {
        super(context);
        initUI();
    }

    public MapView(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MapView(
            Context context,
            AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }
    
    public Button btn_mainButton()
    {
        return btn_mainButton_;
    }
    
    public Switch sw_hoverNow()
    {
        return sw_hoverNow_;
    }
    
    public SeekBar pbar_surveyAreaHeight()
    {
        return pbar_surveyAreaHeight_;
    }
    
    public SeekBar pbar_surveyAreaWidth()
    {
        return pbar_surveyAreaWidth_;
    }
    
    public TextView txt_console()
    {
        return txt_console_;
    }
    
    public SeekBar pbar_surveyProgressTracking()
    {
        return pbar_surveyProgressTracking_;
    }
    
    public TextView txt_surveyAreaHeight()
    {
        return txt_surveyAreaHeight_;
    }
    
    public TextView txt_surveyAreaWidth()
    {
        return txt_surveyAreaWidth_;
    }

    private void initUI()
    {
        inflate(getContext(), R.layout.map_screen, this);

        btn_mainButton_ = (Button) findViewById(R.id.btn_mainButton);
        sw_hoverNow_ = (Switch) findViewById(R.id.sw_hoverNow);
        txt_console_ = (TextView) findViewById(R.id.txt_console);
        txt_surveyAreaWidth_ = (TextView) findViewById(R.id.txt_surveyAreaWidth);
        txt_surveyAreaHeight_ = (TextView) findViewById(R.id.txt_surveyAreaHeight);
        pbar_surveyAreaHeight_ = (SeekBar) findViewById(R.id.pbar_surveyAreaHeight);
        pbar_surveyAreaWidth_ = (SeekBar) findViewById(R.id.pbar_surveyAreaWidth);
        pbar_surveyProgressTracking_ = (SeekBar) findViewById(R.id.pbar_surveyProgressTracking);
    }
}
