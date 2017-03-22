package com.dji.sdk.sample.common.container;

import android.support.v4.app.FragmentActivity;

import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.GoogleMapsConnectionHandler;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.utility.I_MissionErrorNotifier;
import com.dji.sdk.sample.common.utility.MissionErrorNotifier;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;

/**
 * Created by Julia on 2017-03-22.
 */

public class UtilityContainer
{
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;
    private GoogleMapsConnectionHandler googleMapsConnectionHandler_;
    private MissionErrorNotifier missionErrorNotifier_;

    public UtilityContainer(
            FragmentActivity activity)
    {
        permissionRequester_ = new UserPermissionRequester();
        permissionRequester_.requestPermissions(activity);
        contextManager_ = new ApplicationContextManager(activity);
        googleMapsConnectionHandler_ = new GoogleMapsConnectionHandler(activity);
        missionErrorNotifier_ = new MissionErrorNotifier(activity);
    }

    public GoogleMapsConnectionHandler googleMapsConnectionHandler()
    {
        return googleMapsConnectionHandler_;
    }

    public I_ApplicationContextManager contextManager()
    {
        return contextManager_;
    }

    public I_MissionErrorNotifier missionErrorNotifier()
    {
        return missionErrorNotifier_;
    }
}
