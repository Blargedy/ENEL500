package com.dji.sdk.sample.common.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.dji.sdk.sample.common.activity.MapActivity;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;

/**
 * Created by Peter on 2017-02-13.
 */

public class MapPresenter implements View.OnClickListener {
    private Button goToMapButton_;
    private I_ApplicationContextManager contextManager_;

    public MapPresenter(I_ApplicationContextManager contextManager,Button goToMapButton) {
        goToMapButton_ = goToMapButton;
        goToMapButton_.setOnClickListener(this);
        contextManager_ = contextManager;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == goToMapButton_.getId()) {
        try {
            Context context = contextManager_.getApplicationContext();
            Intent gotoMapIntent = new Intent(
                    context, MapActivity.class);
            context.startActivity(gotoMapIntent);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        }
    }
}
