package com.dji.sdk.sample.common.view.src;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.view.api.I_ImageTransferView;
import com.dji.sdk.sample.common.view.api.I_MissionControlView;
import com.dji.sdk.sample.common.view.api.I_SurveyBoundaryCoordinatesView;

/**
 * Created by Julia on 2017-01-15.
 */

public class MapView extends RelativeLayout implements
        I_MissionControlView,
        I_ImageTransferView,
        I_SurveyBoundaryCoordinatesView
{
    private Button btn_mainButton_;
    private Switch sw_hoverNow_;
    private SeekBar pbar_surveyAreaHeight_;
    private SeekBar pbar_surveyAreaWidth_;

    private TextView txt_console_;
    private SeekBar pbar_surveyProgressTracking_;
    private TextView txt_surveyAreaHeight_;
    private TextView txt_surveyAreaWidth_;

    private EditText bottomLeftCornerLatitudeText_;
    private EditText bottomLeftCornerLongitudeText_;
    private EditText topRightCornerLatitudeText_;
    private EditText topRightCornerLongitudeText_;

    private Button generateMissionButton_;
    private Button startMissionButton_;
    private Button transferImagesButton_;

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

    public EditText bottomLeftCornerLatitudeText()
    {
        return bottomLeftCornerLatitudeText_;
    }

    public EditText bottomLeftCornerLongitudeText()
    {
        return bottomLeftCornerLongitudeText_;
    }

    public EditText topRightCornerLatitudeText()
    {
        return topRightCornerLatitudeText_;
    }

    public EditText topRightCornerLongitudeText()
    {
        return topRightCornerLongitudeText_;
    }

    public Button generateMissionButton()
    {
        return generateMissionButton_;
    }

    public Button startMissionButton()
    {
        return startMissionButton_;
    }

    public Button transferImagesButton()
    {
        return transferImagesButton_;
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

        bottomLeftCornerLatitudeText_ = (EditText) findViewById(R.id.txt_bottomLeftCornerLat);
        bottomLeftCornerLongitudeText_ = (EditText) findViewById(R.id.txt_bottomLeftCornerLong);
        topRightCornerLatitudeText_ = (EditText) findViewById(R.id.txt_topRightCornerLat);
        topRightCornerLongitudeText_ = (EditText) findViewById(R.id.txt_topRightCornerLong);
        generateMissionButton_ = (Button) findViewById(R.id.btn_generateMission);
        startMissionButton_ = (Button) findViewById(R.id.btn_startMission);
        transferImagesButton_ = (Button) findViewById(R.id.btn_transferImages);
        
    }
}
