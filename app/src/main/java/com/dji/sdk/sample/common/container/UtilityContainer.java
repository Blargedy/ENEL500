package com.dji.sdk.sample.common.container;

import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.ApplicationSettingsManager;
import com.dji.sdk.sample.common.utility.GoogleMapsConnectionHandler;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.utility.I_MissionStatusNotifier;
import com.dji.sdk.sample.common.utility.MissionStatusNotifier;

/**
 * Created by Julia on 2017-03-22.
 */

public class UtilityContainer
{
    private ApplicationContextManager contextManager_;
    private GoogleMapsConnectionHandler googleMapsConnectionHandler_;
    private MissionStatusNotifier missionErrorNotifier_;
    private ApplicationSettingsManager applicationSettingsManager_;

    public UtilityContainer(
            FragmentActivity activity)
    {
        contextManager_ = new ApplicationContextManager(activity);
        googleMapsConnectionHandler_ = new GoogleMapsConnectionHandler(activity);
        missionErrorNotifier_ = new MissionStatusNotifier(activity);
        applicationSettingsManager_ = new ApplicationSettingsManager(activity);
    }

    public GoogleMapsConnectionHandler googleMapsConnectionHandler()
    {
        return googleMapsConnectionHandler_;
    }

    public I_ApplicationContextManager contextManager()
    {
        return contextManager_;
    }

    public I_MissionStatusNotifier missionStatusNotifier()
    {
        return missionErrorNotifier_;
    }

    public ApplicationSettingsManager applicationSettingsManager()
    {
        return applicationSettingsManager_;
    }
}
