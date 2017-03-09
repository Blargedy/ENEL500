package com.dji.sdk.sample.common.presenter.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dji.sdk.sample.common.mission.api.I_WaypointReachedHandler;
import com.dji.sdk.sample.common.mission.src.MissionBoundary;
import com.dji.sdk.sample.common.presenter.api.I_MapPresenter;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;
import com.dji.sdk.sample.common.values.Coordinate;
import com.dji.sdk.sample.common.view.api.I_MapView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import dji.sdk.missionmanager.DJIWaypoint;

import static com.dji.sdk.sample.R.id.map;
import static com.dji.sdk.sample.common.utility.IntentExtraKeys.WAYPOINT_INDEX;

/**
 * Created by Julia on 2017-03-08.
 */

public class MapPresenter implements
        I_MapPresenter,
        OnMapReadyCallback
{
    private BroadcastReceiver receiver_;

    private GoogleApiClient client;
    private GoogleMap mMap;

    private Marker userMarker;
    private LatLng droneStartingLatLng;
    private Marker droneMarker;
    private boolean polySurveyAreaPicked = false;
    private Polygon surveyPolygon;
    private LatLng areaSelectingMaskMidpoint;

    private SeekBar surveyProgressBar;
    private TextView surveyAreaHeightText;
    private SeekBar surveyAreaHeightBar;
    private TextView surveyAreaWidthText;
    private SeekBar surveyAreaWidthBar;

    private final double dbl_SurveyBoxScaler = 50.0;
    private final double dbl_InitialSurveyBoxSeekValue = 50.0;
    private final double dbl_areaSS = 0.002;

    private double dbl_HeightSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;
    private double dbl_WidthSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;

    // Might not need all of this anymore?
    private ArrayList<Circle> waypointCircleList;
    private ArrayList<Circle> droneNotYetVisitedWaypointList;
    private double percentageCompletion = 0.00;
    private int numWaypointsTotal = 0;
    private int numWaypointsCompleted = 0;
    private ArrayList<Polyline> waypointPolylineList;
    private List<Coordinate> waypointCoordinateList;

    public MapPresenter(
            FragmentActivity fragmentActivity,
            I_MapView mapView,
            GoogleApiClient googleApiClient)
    {
        SupportMapFragment mapFragment = (SupportMapFragment)fragmentActivity.
                getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
        client = googleApiClient;

        surveyProgressBar = mapView.surveyProgressBar();
        surveyAreaHeightText = mapView.surveyAreaHeightText();
        surveyAreaHeightBar = mapView.surveyAreaHeightBar();
        surveyAreaWidthText = mapView.surveyAreaWidthText();
        surveyAreaWidthBar = mapView.surveyAreaWidthBar();

        registerWaypointReachedReceiver(fragmentActivity);
    }

    void registerWaypointReachedReceiver(Context context)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.WAYPOINT_REACHED);

        receiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int waypointIndex = intent.getIntExtra(WAYPOINT_INDEX, 2000);
                reachedWaypointAtIndex(waypointIndex);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver_, filter);
    }

    @Override
    public MissionBoundary getSurveyAreaBoundary()
    {
        // Replace with actual logic to get the area from the map
        Coordinate topRight = new Coordinate(50.796276, -114.205159);
        Coordinate bottomLeft = new Coordinate(50.795906, -114.206540);

        return new MissionBoundary(topRight, bottomLeft);
    }

    @Override
    public void displayMissionWaypoints(Vector<Coordinate> waypoints)
    {
        // display list of waypoints
        // Persist this list for looking up the location of the waypoints later
    }

    @Override
    public void clearMap()
    {
        // remove waypoints from the map
        // Clear any state you have
        // We are starting a new mission
    }

    @Override
    public void enableAllControls()
    {
        // enable sliders
    }

    @Override
    public void disableAllControls()
    {
        // disable sliders
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        // Implement map interactions
        // Seems like most of the map stuff is done through the sliders so I'm not sure
        // What goes here
    }

    private void reachedWaypointAtIndex(int waypointIndex)
    {
        Log.d("MapPresenter", "Reached waypoint " + waypointIndex);
        // Implement the colouring of the waypoint at the index corresponding to your list of waypoints
    }
}
