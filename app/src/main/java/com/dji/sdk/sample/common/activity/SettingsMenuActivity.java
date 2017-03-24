package com.dji.sdk.sample.common.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.presenter.src.MissionSettingsChangedNotifier;

import dji.common.camera.DJICameraSettingsDef;


/**
 * Created by Peter on 2017-03-23.
 */

public class SettingsMenuActivity extends AppCompatActivity implements View.OnClickListener {
    //private SettingsMenuView  settingsMenuView_;

    private NumberPicker numPickerAltitude_;
    private NumberPicker numPickerImageOverlap_;
    private NumberPicker numPickerSwathOverlap_;
    private Spinner spinnerCameraISO_;
    private Spinner spinnerCameraShutter_;
    private Button btnAcceptOK_;
    private CheckBox chkCameraAuto_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //settingsMenuView_ = new SettingsMenuView(this);
        //setContentView(settingsMenuView_);
        setContentView(R.layout.settings_dialog_screen);

        numPickerAltitude_ = (NumberPicker) findViewById(R.id.numPickerAltitude);
        numPickerImageOverlap_ = (NumberPicker) findViewById(R.id.imageOverlap);
        numPickerSwathOverlap_ = (NumberPicker) findViewById(R.id.swathOverlap);
        spinnerCameraISO_ = (Spinner) findViewById(R.id.spinnerCameraISO);
        spinnerCameraShutter_ = (Spinner) findViewById(R.id.spinnerCameraShutter);
        chkCameraAuto_ = (CheckBox) findViewById(R.id.chkCameraAuto);
        btnAcceptOK_ = (Button) findViewById(R.id.btnAcceptOK);


        btnAcceptOK_.setEnabled(true);
        btnAcceptOK_.setOnClickListener(this);

        numPickerAltitude_.setMinValue(0);
        numPickerAltitude_.setMaxValue(90);


        numPickerImageOverlap_.setMinValue(0);
        numPickerImageOverlap_.setMaxValue(100);

        numPickerSwathOverlap_.setMinValue(0);
        numPickerSwathOverlap_.setMaxValue(100);


        // FILL ISO

        DJICameraSettingsDef.CameraISO[] ISOENUMLIST = DJICameraSettingsDef.CameraISO.values();

        String[] ISOENUMSTRINGS = new String[ISOENUMLIST.length];

        for (int i = 0; i < ISOENUMLIST.length; i++) {
            ISOENUMSTRINGS[i] = ISOENUMLIST[i].toString();
        }


        ArrayAdapter<String> ISOAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ISOENUMSTRINGS);
        spinnerCameraISO_.setAdapter(ISOAdapter);

    // FILL SHUTTER

        DJICameraSettingsDef.CameraShutterSpeed[] SHUTTERENUMLIST = DJICameraSettingsDef.CameraShutterSpeed.values();

        String[] SHUTTERENUMSTRINGS = new String[SHUTTERENUMLIST.length];

        for (int i = 0; i < SHUTTERENUMLIST.length; i++) {
            SHUTTERENUMSTRINGS[i] = SHUTTERENUMLIST[i].toString();
        }


        ArrayAdapter<String> shutterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SHUTTERENUMSTRINGS);
        spinnerCameraShutter_.setAdapter(shutterAdapter);


        // Set the values as per current settings:

        numPickerAltitude_.setValue(20);
        numPickerImageOverlap_.setValue(80);
        numPickerSwathOverlap_.setValue(50);
        chkCameraAuto_.setChecked(true);

    String compareValue = "ISO_100";
        if (!compareValue.equals(null)) {
            int spinnerPosition = ISOAdapter.getPosition(compareValue);
            spinnerCameraISO_.setSelection(spinnerPosition);
        }

        compareValue = "ShutterSpeed1_800";
        if (!compareValue.equals(null)) {
            int spinnerPosition = shutterAdapter.getPosition(compareValue);
            spinnerCameraShutter_.setSelection(spinnerPosition);
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAcceptOK_.getId()) {
            // Save the current settings
            DJICameraSettingsDef.CameraISO[] ISOENUMLIST = DJICameraSettingsDef.CameraISO.values();
            DJICameraSettingsDef.CameraShutterSpeed[] SHUTTERENUMLIST = DJICameraSettingsDef.CameraShutterSpeed.values();

            MissionSettingsChangedNotifier myMSCN = new MissionSettingsChangedNotifier(this);

            float altitude = this.numPickerAltitude_.getValue();
            double minimumPercentImageOverlap = this.numPickerImageOverlap_.getValue();
            double minimumPercentSwathOverlap = this.numPickerSwathOverlap_.getValue();

            boolean isInAutomaticMode = this.chkCameraAuto_.isChecked();
            DJICameraSettingsDef.CameraISO cameraISO = ISOENUMLIST[this.spinnerCameraISO_.getSelectedItemPosition()];
            DJICameraSettingsDef.CameraShutterSpeed cameraShutterSpeed = SHUTTERENUMLIST[this.spinnerCameraShutter_.getSelectedItemPosition()];

            myMSCN.notifySettingsChanged(altitude,minimumPercentImageOverlap,minimumPercentSwathOverlap,isInAutomaticMode,cameraShutterSpeed,cameraISO);
            this.finish();
        }
    }

}