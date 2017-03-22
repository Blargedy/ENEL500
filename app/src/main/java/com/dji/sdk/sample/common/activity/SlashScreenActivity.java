package com.dji.sdk.sample.common.activity;

/**
 * Created by Peter on 2017-03-20.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dji.sdk.sample.R;

public class SlashScreenActivity extends Activity {

    /**
     * Duration of wait
     **/
    private ImageView ucLogo;
    private ImageView lockheedLogo;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);
        ucLogo = (ImageView) findViewById(R.id.imageucLogo);
        lockheedLogo = (ImageView) findViewById(R.id.imagelockheedLogo);

        // check if there is no internet, if so - switch to cached map
        new Handler().

                post(new Runnable() {
                    @Override
                    public void run() {
                        ucLogo.setVisibility(View.VISIBLE);
                        new Handler().

                                postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        ucLogo.setVisibility(View.GONE);
                                        lockheedLogo.setVisibility(View.VISIBLE);

                                        new

                                                Handler().

                                                postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent mainIntent = new Intent(SlashScreenActivity.this, MainMenuActivity.class);
                                                        SlashScreenActivity.this.startActivity(mainIntent);
                                                        SlashScreenActivity.this.finish();
                                                    }
                                                }, 1000);


                                    }
                                }, 1000);
                    }
                });


    }


}