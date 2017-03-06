package com.dji.sdk.sample.common.utility;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.SupportMapFragment;

import static com.dji.sdk.sample.R.id.map;

/**
 * Created by Julia on 2017-03-06.
 */

public class GoogleMapsConnectionHandler
{
    private GoogleApiClient googleApiClient_;

    public GoogleMapsConnectionHandler(
            FragmentActivity fragmentActivity)
    {
        googleApiClient_ = new GoogleApiClient.Builder(fragmentActivity).addApi(AppIndex.API).build();
    }

    public GoogleApiClient googleApiClient()
    {
        return googleApiClient_;
    }

    public void startConnection()
    {
        googleApiClient_.connect();
        AppIndex.AppIndexApi.start(googleApiClient_, getIndexApiAction());
    }

    public void endConnection()
    {
        AppIndex.AppIndexApi.end(googleApiClient_, getIndexApiAction());
        googleApiClient_.disconnect();
    }

    public Action getIndexApiAction()
    {
        Thing object = new Thing.Builder()
                .setName("Map Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
