package com.dji.sdk.sample.common.view.api;

import android.widget.EditText;

/**
 * Created by Julia on 2017-03-06.
 */

public interface I_SurveyBoundaryCoordinatesView
{
    EditText bottomLeftCornerLatitudeText();
    EditText bottomLeftCornerLongitudeText();
    EditText topRightCornerLatitudeText();
    EditText topRightCornerLongitudeText();
}