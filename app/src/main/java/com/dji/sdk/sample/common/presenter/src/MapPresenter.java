package com.dji.sdk.sample.common.presenter.src;

import android.support.v4.app.FragmentActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dji.sdk.sample.common.mission.src.MissionBoundary;
import com.dji.sdk.sample.common.presenter.api.I_MapPresenter;
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

/**
 * Created by Julia on 2017-03-08.
 */

public class MapPresenter implements
        I_MapPresenter,
        OnMapReadyCallback
{
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
    }

    @Override
    public MissionBoundary getSurveyAreaBoundary()
    {
        // TODO replace with actually getting coordinates from map
        Coordinate topRight = new Coordinate(50.796276, -114.205159);
        Coordinate bottomLeft = new Coordinate(50.795906, -114.206540);

        return new MissionBoundary(topRight, bottomLeft);
    }

    @Override
    public void displayMissionWaypoints(Vector<DJIWaypoint> waypoints)
    {

    }

    @Override
    public void clearWaypointsFromMap()
    {

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {

    }
}
