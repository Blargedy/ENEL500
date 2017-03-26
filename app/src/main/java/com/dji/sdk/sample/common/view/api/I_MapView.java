package com.dji.sdk.sample.common.view.api;


import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;

/**
 * Created by Julia on 2017-03-08.
 */

public interface I_MapView
{
    SeekBar surveyProgressBar();
    TextView surveyAreaHeightText();
    SeekBar surveyAreaHeightBar();
    TextView surveyAreaWidthText();
    SeekBar surveyAreaWidthBar();
    LinearLayout linearLayoutMainV();
    Button startMissionButton();
    TextView txtPercentCompleteMap();
    Button btnFindMeNow();

}
