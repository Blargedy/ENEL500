package com.dji.sdk.sample.common.presenter.src;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;


import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.mission.src.MissionBoundary;
import com.dji.sdk.sample.common.presenter.api.I_MapPresenter;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.values.Coordinate;
import com.dji.sdk.sample.common.view.api.I_MapView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Vector;


import static com.dji.sdk.sample.R.id.map;
import static com.dji.sdk.sample.common.utility.IntentExtraKeys.WAYPOINT_INDEX;

/**
 * Created by Julia on 2017-03-08.
 */

public class MapPresenter implements
        I_MapPresenter,
        OnMapReadyCallback
{
    private BroadcastReceiver waypointReachedReceiver_;
    private BroadcastReceiver droneLocationChangedReceiver_;
    private DroneLocationEntity droneLocation_;

    private GoogleApiClient client;
    private GoogleMap mMap;

    private Marker userMarker;
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

    // Lists
    private ArrayList<Circle> waypointCircleList;
    private ArrayList<Polyline> wayPointPolyLineList;
    private Vector<Coordinate> wayPointList;

    // Progress Tracking
    private double percentageCompletion = 0.00;
    private int numWaypointsTotal = 0;
    private int numWaypointsCompleted = 0;

    // GPS User
    LocationListener userLocationListener;
    LocationManager userLocationManager;
    Activity fragmentActivity;

    public MapPresenter(
            FragmentActivity fragmentActivity,
            GoogleApiClient googleApiClient,
            I_MapView mapView,
            DroneLocationEntity droneLocation) {
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentActivity.
                getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        client = googleApiClient;
        this.fragmentActivity = fragmentActivity;

        surveyProgressBar = mapView.surveyProgressBar();
        surveyAreaHeightText = mapView.surveyAreaHeightText();
        surveyAreaHeightBar = mapView.surveyAreaHeightBar();
        surveyAreaWidthText = mapView.surveyAreaWidthText();
        surveyAreaWidthBar = mapView.surveyAreaWidthBar();

        droneLocation_ = droneLocation;
        registerWaypointReachedReceiver(fragmentActivity);
        registerDroneLocationChangedReceiver(fragmentActivity);
    }

    private void registerWaypointReachedReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.WAYPOINT_REACHED);

        waypointReachedReceiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int waypointIndex = intent.getIntExtra(WAYPOINT_INDEX, 2000);
                reachedWaypointAtIndex(waypointIndex);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(waypointReachedReceiver_, filter);
    }

    private void registerDroneLocationChangedReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastIntentNames.DRONE_LOCATION_CHANGED);

        droneLocationChangedReceiver_ = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateDroneLocation();
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(droneLocationChangedReceiver_, filter);
    }

    @Override
    public MissionBoundary getSurveyAreaBoundary() {
        // TODO Remove this, just for test purposes
        Coordinate topRight = new Coordinate(50.796276, -114.205159);
        Coordinate bottomLeft = new Coordinate(50.795906, -114.206540);

//        Coordinate topRight = new Coordinate(
//                surveyPolygon.getPoints().get(2).latitude,
//                surveyPolygon.getPoints().get(2).longitude);
//        Coordinate bottomLeft = new Coordinate(
//                surveyPolygon.getPoints().get(0).latitude,
//                surveyPolygon.getPoints().get(0).longitude);
        return new MissionBoundary(topRight, bottomLeft);
    }

    @Override
    public void displayMissionWaypoints(Vector<Coordinate> waypoints) {
        surveyPolygon.remove(); //no longer needed

        wayPointList = waypoints;

        // Make net between each waypoint marker
        wayPointPolyLineList = new ArrayList<Polyline>();
        // Connect drone to first waypoint
        wayPointPolyLineList.add(mMap.addPolyline(new PolylineOptions()
                .add(droneMarker.getPosition(), new LatLng(wayPointList.get(0).latitude_, wayPointList.get(0).longitude_))
                .width(5)
                .color(Color.BLACK)));

        for (int i = 0; i < wayPointList.size(); i++) {
            LatLng waypointLatLng1 = new LatLng(wayPointList.get(i).latitude_, wayPointList.get(i).longitude_);
            if (i == (wayPointList.size() - 1)) { //connect last waypoint to drone
                wayPointPolyLineList.add(mMap.addPolyline(new PolylineOptions()
                        .add(waypointLatLng1, droneMarker.getPosition())
                        .width(5)
                        .color(Color.BLACK)));
                break;
            }
            LatLng waypointLatLng2 = new LatLng(wayPointList.get(i + 1).latitude_, wayPointList.get(i + 1).longitude_);
            wayPointPolyLineList.add(mMap.addPolyline(new PolylineOptions()
                    .add(waypointLatLng1, waypointLatLng2)
                    .width(5)
                    .color(Color.BLACK)));
        }

        // Make the waypoint circles
        waypointCircleList = new ArrayList<Circle>();
        for (int j = 0; j < wayPointList.size(); j++) {
            waypointCircleList.add(mMap.addCircle(new CircleOptions()
                    .center(new LatLng(wayPointList.get(j).latitude_, wayPointList.get(j).longitude_))
                    .radius(12)
                    .strokeWidth(1)
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.argb(150, 255, 0, 0)))); // transparent red circles
        }
        numWaypointsTotal = wayPointList.size();

    }

    @Override
    public void clearMap() {
        wayPointList.clear();

        for (int i = 0; i < wayPointPolyLineList.size(); i++) {
            wayPointPolyLineList.get(i).remove();
        }

        wayPointPolyLineList.clear();

        for (int i = 0; i < waypointCircleList.size(); i++) {
            waypointCircleList.get(i).remove();
        }

        waypointCircleList.clear();
    }

    @Override
    public void enableAllControls() {
        surveyAreaHeightBar.setEnabled(true);
        surveyAreaWidthBar.setEnabled(true);
    }

    @Override
    public void disableAllControls() {
        surveyAreaHeightBar.setEnabled(false);
        surveyAreaWidthBar.setEnabled(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Implement map interactions
        mMap = googleMap;

        //surveyProgressBar.setEnabled(true);
        //surveyProgressBar.setProgress(25);
        //surveyProgressBar.setMax(100);
        // For now place drone and 'android' markers manually
        LatLng calgaryLatLng = new LatLng(51.076674, -114.134972);
        userMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(calgaryLatLng.latitude + 0.001, calgaryLatLng.longitude - 0.004))
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // to change to user icon

        droneMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(userMarker.getPosition().latitude + 0.0005, userMarker.getPosition().longitude + 0.0009))
                .title("Drone is here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); // to change to actual phantom 4 icon

        surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(surveyAreaWidthBar.getProgress()));
        surveyAreaHeightText.setText("Survey Area Height: " + String.valueOf(surveyAreaHeightBar.getProgress()));
        // the following code places the area selector on the map at startup
        LatLng calgaryLatLngUpSlightly = new LatLng((51.076674 + 0.005), -114.134972);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(calgaryLatLngUpSlightly));
        polySurveyAreaPicked = true;
        dbl_HeightSeek = (double) surveyAreaHeightBar.getProgress() / dbl_SurveyBoxScaler;
        dbl_WidthSeek = (double) surveyAreaWidthBar.getProgress() / dbl_SurveyBoxScaler;
        double lati = calgaryLatLngUpSlightly.latitude; // store the latitude of the tap
        double longi = calgaryLatLngUpSlightly.longitude; // store the longitude of the tap
        areaSelectingMaskMidpoint = (new LatLng(lati, longi)); // save the tap LatLng globally
        surveyPolygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                .strokeWidth(2)
                .strokeColor(Color.BLACK)
                .fillColor(Color.argb(125, 0, 0, 0)) // black that is 50% opaque (see through)
                .clickable(true));
        //enableAllControls();
        // Testing
//        Vector<Coordinate> waypoints = new Vector<Coordinate>();
//        waypoints.add(new Coordinate(51.076674 + 0.003, -114.134972 - 0.01));
//        waypoints.add(new Coordinate(51.076674 + 0.006, -114.134972 + 0.01));
//        waypoints.add(new Coordinate(51.076674 + 0.009, -114.134972 - 0.01));
//        waypoints.add(new Coordinate(51.076674 + 0.012, -114.134972 + 0.01));
//        waypoints.add(new Coordinate(51.076674 + 0.015, -114.134972 - 0.01));
//        displayMissionWaypoints(waypoints); // testing
//
//        clearMap();
        // Any tap on the Map calls this listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // This if statement runs if user taps map first (not Main Button)
                if (!polySurveyAreaPicked) { // Track if an area selector is already showing
                    polySurveyAreaPicked = true;
                    dbl_HeightSeek = (double) surveyAreaHeightBar.getProgress() / dbl_SurveyBoxScaler;
                    dbl_WidthSeek = (double) surveyAreaWidthBar.getProgress() / dbl_SurveyBoxScaler;
                    double lati = point.latitude; // store the latitude of the tap
                    double longi = point.longitude; // store the longitude of the tap
                    areaSelectingMaskMidpoint = (new LatLng(lati, longi)); // save the tap LatLng globally
                    surveyPolygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.argb(125, 0, 0, 0)) // black that is 50% opaque (see through)
                            .clickable(true));
                    //enableAllControls();
                } else { // User wants to place the rectangle center at another point on the map
                    surveyPolygon.remove();
                    polySurveyAreaPicked = false;
                    // disableAllControls();
                }
            }

        });

        // User changes the survey area height by sliding the seek bar
        surveyAreaHeightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // Don't let Height become too small..
                if (surveyAreaHeightBar.getProgress() < 2) {
                    surveyAreaHeightBar.setProgress(2);
                    return;
                }
                // Get new scaled height
                dbl_HeightSeek = (double) progress / dbl_SurveyBoxScaler;
                // redraw
                surveyPolygon.remove(); // remove old area selector
                if (polySurveyAreaPicked) { // if an area selector is on the map
                    double lati = areaSelectingMaskMidpoint.latitude;
                    double longi = areaSelectingMaskMidpoint.longitude;
                    surveyAreaHeightText.setText("Survey Area Height: " + String.valueOf(surveyAreaHeightBar.getProgress()));
                    Polygon temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .clickable(true));
                    surveyPolygon.remove(); // remove the old area selector
                    surveyPolygon = temp_surveyPolygon; // save this area but don't fill color
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                surveyPolygon.setFillColor(Color.argb(125, 0, 0, 0)); // fill color only after the user stops scaling pbar
            }
        });

        // User changes the survey area width by sliding the seek bar
        surveyAreaWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                if (surveyAreaWidthBar.getProgress() < 2) {
                    surveyAreaWidthBar.setProgress(2);
                    return;
                }
                dbl_WidthSeek = (double) progress / dbl_SurveyBoxScaler;
                // redraw
                if (polySurveyAreaPicked) {
                    double lati = areaSelectingMaskMidpoint.latitude;
                    double longi = areaSelectingMaskMidpoint.longitude;
                    surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(surveyAreaWidthBar.getProgress()));
                    Polygon temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            .clickable(true));
                    surveyPolygon.remove();
                    surveyPolygon = temp_surveyPolygon;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                surveyPolygon.setFillColor(Color.argb(125, 0, 0, 0));
            }
        });


        userLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if (location == null) {
                    // no GPS position do nothing - cancel.
                    return;
                }
                if (userMarker != null) {
                    userMarker.remove();
                }
                userMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // to change to user icon
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        userLocationManager = (LocationManager) fragmentActivity.getSystemService(Context.LOCATION_SERVICE);
        userLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, userLocationListener);
    }

    private void reachedWaypointAtIndex(int waypointIndex) {
        Log.d("MapPresenter", "Reached waypoint " + waypointIndex);

        droneMarker.setPosition(waypointCircleList.get(waypointIndex).getCenter());
        waypointCircleList.get(waypointIndex).setFillColor(Color.argb(150, 0, 0, 255)); // change marker to blue
        numWaypointsCompleted++; // track mission progress
        percentageCompletion = (int) (100.0d * numWaypointsCompleted / numWaypointsTotal);
        // surveyProgressBar.setProgress((int) percentageCompletion);
    }

    private void updateDroneLocation()
    {
        Coordinate location = droneLocation_.droneLocation();
        Log.d("MapPresenter", "droneLocation: latitude=" + location.latitude_ + " longitude=" + location.longitude_);
        // update the marker on the map
    }
}
