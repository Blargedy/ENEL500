package com.dji.sdk.sample.common.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.presenter.src.MissionSettingsChangedNotifier;
import com.dji.sdk.sample.common.utility.ApplicationSettingsManager;

import dji.common.camera.DJICameraSettingsDef;

/**
 * Created by Peter on 2017-03-23.
 */

public class SettingsMenuActivity extends AppCompatActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
{
    private MissionSettingsChangedNotifier missionSettingsChangedNotifier_;
    private ApplicationSettingsManager settingsManager_;

    private NumberPicker numPickerAltitude_;
    private NumberPicker numPickerFlightSpeed_;
    private NumberPicker numPickerImageOverlap_;
    private NumberPicker numPickerSwathOverlap_;
    private NumberPicker numPickerWayointSize_;
    private Spinner spinnerCameraISO_;
    private Spinner spinnerCameraShutter_;
    private TextView cameraISOLabel;
    private TextView cameraShutterLabel;
    private Spinner spinnerImageType_;
    private Button btnAcceptOK_;
    private CheckBox chkCameraAuto_;

    private ArrayAdapter<String> isoAdapter_;
    private ArrayAdapter<String> shutterAdapter_;
    private ArrayAdapter<String> imageTypeAdapter_;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_dialog_screen);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        missionSettingsChangedNotifier_ = new MissionSettingsChangedNotifier(this);
        settingsManager_ = new ApplicationSettingsManager(this);

        initializeViewElements();
        populateIsoComboBox();
        populateShutterSpeedComboBox();
        populateImageTypeComboBox();
        restoreSettingsFromLastSession();
    }

    private void initializeViewElements()
    {
        numPickerAltitude_ = (NumberPicker) findViewById(R.id.numPickerAltitude);
        numPickerFlightSpeed_ = (NumberPicker) findViewById(R.id.numPickerMissionSpeed);
        numPickerImageOverlap_ = (NumberPicker) findViewById(R.id.imageOverlap);
        numPickerSwathOverlap_ = (NumberPicker) findViewById(R.id.swathOverlap);
        numPickerWayointSize_ = (NumberPicker) findViewById(R.id.numPickerWaypointSize);
        spinnerCameraISO_ = (Spinner) findViewById(R.id.spinnerCameraISO);
        spinnerCameraShutter_ = (Spinner) findViewById(R.id.spinnerCameraShutter);
        spinnerImageType_ = (Spinner) findViewById(R.id.spinnerImageType);
        chkCameraAuto_ = (CheckBox) findViewById(R.id.chkCameraAuto);
        btnAcceptOK_ = (Button) findViewById(R.id.btnAcceptOK);
        cameraShutterLabel = (TextView)findViewById(R.id.cameraShutterLabel);
        cameraISOLabel = (TextView)findViewById(R.id.cameraISOLabel);
        btnAcceptOK_.setEnabled(true);
        btnAcceptOK_.setOnClickListener(this);

        numPickerAltitude_.setMinValue(10);
        numPickerAltitude_.setMaxValue(90);

        numPickerFlightSpeed_.setMinValue(1);
        numPickerFlightSpeed_.setMaxValue(10);

        numPickerImageOverlap_.setMinValue(30);
        numPickerImageOverlap_.setMaxValue(90);

        numPickerSwathOverlap_.setMinValue(30);
        numPickerSwathOverlap_.setMaxValue(90);

        numPickerWayointSize_.setMinValue(1);
        numPickerWayointSize_.setMaxValue(10);

        chkCameraAuto_.setOnCheckedChangeListener(this);
    }

    private void populateIsoComboBox()
    {
        DJICameraSettingsDef.CameraISO[] ISOENUMLIST = DJICameraSettingsDef.CameraISO.values();
        String[] ISOENUMSTRINGS = new String[ISOENUMLIST.length];

        for (int i = 0; i < ISOENUMLIST.length; i++) {
            ISOENUMSTRINGS[i] = ISOENUMLIST[i].name();
        }

        isoAdapter_ = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ISOENUMSTRINGS);
        spinnerCameraISO_.setAdapter(isoAdapter_);
    }

    private void populateShutterSpeedComboBox()
    {
        DJICameraSettingsDef.CameraShutterSpeed[] SHUTTERENUMLIST = DJICameraSettingsDef.CameraShutterSpeed.values();
        String[] SHUTTERENUMSTRINGS = new String[SHUTTERENUMLIST.length];

        for (int i = 0; i < SHUTTERENUMLIST.length; i++) {
            SHUTTERENUMSTRINGS[i] = SHUTTERENUMLIST[i].name();
        }

        shutterAdapter_ = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, SHUTTERENUMSTRINGS);
        spinnerCameraShutter_.setAdapter(shutterAdapter_);
    }

    private void populateImageTypeComboBox()
    {
        String[] imageTypes = new String[2];
        imageTypes[0] = DJICameraSettingsDef.CameraPhotoFileFormat.JPEG.name();
        imageTypes[1] = DJICameraSettingsDef.CameraPhotoFileFormat.RAW.name();

        imageTypeAdapter_ = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, imageTypes);
        spinnerImageType_.setAdapter(imageTypeAdapter_);
    }

    private void restoreSettingsFromLastSession()
    {
        numPickerAltitude_.setValue((int) settingsManager_.getAltitudeFromSettings());
        numPickerFlightSpeed_.setValue((int) settingsManager_.getMissionSpeedFromSettings());
        numPickerImageOverlap_.setValue(
                (int)(settingsManager_.getMinimumPercentImageOverlapFromSettings() * 100));
        numPickerSwathOverlap_.setValue(
                (int)(settingsManager_.getMinimumPercentSwathOverlapFromSettings() * 100));
        numPickerWayointSize_.setValue((int) settingsManager_.getWaypointSizeFromSettings());

        chkCameraAuto_.setChecked(settingsManager_.getIsCameraInAutomaticModeFromSettings());

        int isoSpinnerPosition = isoAdapter_.getPosition(
                settingsManager_.getCameraIsoFromSettings());
        spinnerCameraISO_.setSelection(isoSpinnerPosition);

        int shutterSpeedSpinnerPosition = shutterAdapter_.getPosition(
                settingsManager_.getCameraShutterSpeedFromSettings());
        spinnerCameraShutter_.setSelection(shutterSpeedSpinnerPosition);

        int imageTypeSpinnerPosition = imageTypeAdapter_.getPosition(
                settingsManager_.getImageTypeFromSettings());
        spinnerImageType_.setSelection(imageTypeSpinnerPosition);


            if (chkCameraAuto_.isChecked()) {
                spinnerCameraISO_.setVisibility(View.GONE);
                spinnerCameraShutter_.setVisibility(View.GONE);
                cameraShutterLabel.setVisibility(View.GONE);
                cameraISOLabel.setVisibility(View.GONE);
            }else{
                spinnerCameraISO_.setVisibility(View.VISIBLE);
                spinnerCameraShutter_.setVisibility(View.VISIBLE);
                cameraShutterLabel.setVisibility(View.VISIBLE);
                cameraISOLabel.setVisibility(View.VISIBLE);
            }
    }

    private void persistSettingsForNextSession()
    {
        settingsManager_.saveAltitudeToSettings(numPickerAltitude_.getValue());
        settingsManager_.saveMissionSpeedToSettings(numPickerFlightSpeed_.getValue());
        settingsManager_.saveMinimumPercentImageOverlapToSettings(
                numPickerImageOverlap_.getValue() / 100.0f);
        settingsManager_.saveMinimumPercentSwathOverlapToSettings(
                numPickerSwathOverlap_.getValue() / 100.0f);
        settingsManager_.saveWaypointSizeToSettings(numPickerWayointSize_.getValue());

        settingsManager_.saveIsCameraInAutomaticModeToSettings(chkCameraAuto_.isChecked());

        DJICameraSettingsDef.CameraISO cameraISO = DJICameraSettingsDef.CameraISO.valueOf(
                spinnerCameraISO_.getSelectedItem().toString());
        settingsManager_.saveCameraIsoToSettings(cameraISO);

        DJICameraSettingsDef.CameraShutterSpeed shutterSpeed = DJICameraSettingsDef.CameraShutterSpeed
                .valueOf(spinnerCameraShutter_.getSelectedItem().toString());
        settingsManager_.saveCameraShutterSpeedToSettings(shutterSpeed);

        DJICameraSettingsDef.CameraPhotoFileFormat imageType = DJICameraSettingsDef.CameraPhotoFileFormat
                .valueOf(spinnerImageType_.getSelectedItem().toString());
        settingsManager_.saveImageTypeToSettings(imageType);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAcceptOK_.getId()) {
            persistSettingsForNextSession();
            missionSettingsChangedNotifier_.notifyMissionSettingsChanged();
            this.finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView.getId() == chkCameraAuto_.getId())
        {
            changeCameraSettingsEnabledProperty(!isChecked);
        }
    }

    private void changeCameraSettingsEnabledProperty(boolean arePropertiesEnabled)
    {
        if (arePropertiesEnabled) {
            spinnerCameraISO_.setVisibility(View.VISIBLE);
            spinnerCameraShutter_.setVisibility(View.VISIBLE);
            cameraShutterLabel.setVisibility(View.VISIBLE);
            cameraISOLabel.setVisibility(View.VISIBLE);

        }else{
            spinnerCameraISO_.setVisibility(View.GONE);
            spinnerCameraShutter_.setVisibility(View.GONE);
            cameraShutterLabel.setVisibility(View.GONE);
            cameraISOLabel.setVisibility(View.GONE);
        }
    }
}