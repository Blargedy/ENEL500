package com.dji.sdk.sample.common.presenter.src;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.utility.ApplicationSettingsManager;
import com.dji.sdk.sample.common.values.MissionBoundary;
import com.dji.sdk.sample.common.presenter.api.I_MapPresenter;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.CachedMapLoaderThread;
import com.dji.sdk.sample.common.utility.CachedMapNotifyingThread;
import com.dji.sdk.sample.common.utility.I_CachedMapThreadCompletedListener;
import com.dji.sdk.sample.common.values.Coordinate;
import com.dji.sdk.sample.common.view.api.I_MapView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Vector;

import static com.dji.sdk.sample.R.id.loadingProgressAnimation;
import static com.dji.sdk.sample.R.id.map;
import static com.dji.sdk.sample.R.id.progressBar;
import static com.dji.sdk.sample.R.id.txt_surveyAreaHeight;
import static com.dji.sdk.sample.R.id.txt_surveyAreaWidth;
import static com.dji.sdk.sample.common.utility.IntentExtraKeys.WAYPOINT_INDEX;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class MapPresenter implements
        I_MapPresenter,
        OnMapReadyCallback, I_CachedMapThreadCompletedListener, GoogleMap.CancelableCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {
    // Progress tracking
    private BroadcastReceiver waypointReachedReceiver_;
    // Google misc
    private GoogleApiClient client;
    private GoogleMap mMap;
    // Survey Area Selector
    private Marker userMarker;
    private Marker droneMarker;
    private boolean polySurveyAreaPicked = false;
    private Polygon surveyPolygon;
    private LatLng areaSelectingMaskMidpoint;
    // Constants
    // Area Selector dimension variables
    private double dbl_HeightScaler = 222000d;
    private double dbl_HeightSeek = 75.0d / dbl_HeightScaler;
    private double dbl_WidthScaler = 140000d;
    private double dbl_WidthSeek = 75.0d / dbl_WidthScaler;
    // Controls
    private SeekBar surveyProgressBar;
    private TextView surveyAreaHeightText;
    private SeekBar surveyAreaHeightBar;
    private TextView surveyAreaWidthText;
    private SeekBar surveyAreaWidthBar;
    private Button startMission;
    private TextView txtPercentCompleteMap;
    private Button btnFindMeNow;
    // Lists
    static ArrayList<Circle> waypointCircleList;
    static ArrayList<Polyline> wayPointPolyLineList;
    // Progress Tracking
    private float zlayer = 4000.0f;
    private double percentageCompletion = 0.00d;
    private int numWaypointsTotal = 0;
    private int numWaypointsCompleted = 0;
    private float wayPointCircleRadius = 1.0f;
    ApplicationSettingsManager myASM;
    // GPS Drone
    private BroadcastReceiver droneLocationChangedReceiver_;
    private DroneLocationEntity droneLocation_;
    private long droneGPSStartTime = System.nanoTime() + 1000000000l;
    private final Drawable droneDrawable;
    private BitmapDescriptor droneMarkerIcon;
    // GPS User
    private LocationListener userLocationListener;
    private LocationManager userLocationManager;
    private boolean haveAnimatedCameraToUserMarker = false;
    private boolean introZoomFinished = false;
    private long userGPSStartTime = System.nanoTime() + 1000000000l;
    private final Drawable userDrawable;
    private BitmapDescriptor userMarkerIcon;
    private Location lastKnownUserLocation;
    // Cached Map
    private CachedMapNotifyingThread cachedMapLoadNotifier;
    private CachedMapLoaderThread cachedMapLoaderThread;
    private TileOverlay offlineOverlay;
    private TileProvider offLineTileProvider;
    private boolean usingCachedMap = false;
    private boolean needToTellUserCachedMapAvailable = true;
    // Activity and intents
    private Intent starterIntent;
    private FragmentActivity fragmentActivity;
    private MissionStateEntity missionState;


    // TO DO


    public MapPresenter(
            FragmentActivity fragmentActivity,
            GoogleApiClient googleApiClient,
            I_MapView mapView,
            DroneLocationEntity droneLocation, MissionStateEntity missionState_) {
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentActivity.
                getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
        this.fragmentActivity = fragmentActivity;
        starterIntent = fragmentActivity.getIntent();
        client = googleApiClient;
        // Get GUI View Handles
        getViews(mapView);


        // Prepare GUI
        this.surveyAreaHeightBar.setEnabled(false);
        this.surveyAreaWidthBar.setEnabled(false);
        this.surveyProgressBar.setEnabled(false);
        btnFindMeNow.setVisibility(View.INVISIBLE);


        droneDrawable = fragmentActivity.getResources().getDrawable(R.drawable.drone_marker);
        userDrawable = fragmentActivity.getResources().getDrawable(R.drawable.android_marker);
        droneLocation_ = droneLocation;

        // Register for intent filer to watch for broadcasts
        registerWaypointReachedReceiver(fragmentActivity);
        registerDroneLocationChangedReceiver(fragmentActivity);

        // Set state
        missionState = missionState_;
        missionState.setCurrentMissionState(MissionStateEnum.INITIALIZING_MAP);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Implement map interactions
        mMap = googleMap;
        Toast toastConsole = Toast.makeText(fragmentActivity.getApplicationContext(), "Waiting for GPS signal...", Toast.LENGTH_LONG);
        toastConsole.show();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng calgaryLatLng = new LatLng(51.0486, -114.0708);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calgaryLatLng, 10.0f));
        // For now place drone and 'android' markers manually
        surveyAreaHeightBar.setProgress(75);
        surveyAreaWidthBar.setProgress(75);
        surveyAreaHeightText.setText("Survey Area Height: " + "75.0 " + " m");
        surveyAreaWidthText.setText("Survey Area Width: " + "75.0 " + " m");
        mMap.setMinZoomPreference(0.00f);
        mMap.setMaxZoomPreference(25.0f);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        txtPercentCompleteMap.setText("0.00% complete.");

        disableMapGestures();
        initSurveyBox();
        initCachedMap();
        initCachedMapChecker();

        // Assign all listeners here:
        initMiscMapListeners();
        userLocationListener = new GPSSignalListener();


    }


    @Override
    public void displayMissionWaypoints(Vector<Coordinate> waypoints) {
        this.surveyAreaHeightBar.setEnabled(false);
        this.surveyAreaWidthBar.setEnabled(false);
        if (surveyPolygon != null) surveyPolygon.remove(); //no longer needed
        myASM = new ApplicationSettingsManager(fragmentActivity);
        wayPointCircleRadius = myASM.getWaypointSizeFromSettings();
        // check if swatch up down or left right
        // then when lat changes for up down add polyline, or
        // when long changes for left right add polyine.
        // Make the waypoint circles
        waypointCircleList = new ArrayList<Circle>();
        for (int j = 0; j < waypoints.size(); j++) {
            zlayer++;
            if (j == 0) {
                waypointCircleList.add(mMap.addCircle(new CircleOptions()
                        .center(new LatLng(waypoints.get(j).latitude_, waypoints.get(j).longitude_))
                        .radius(wayPointCircleRadius + 3.0f)
                        .strokeWidth(3)
                        .zIndex(zlayer + 5000.1f) // above the polylines, below the drone and user
                        .strokeColor(Color.BLACK)
                        .fillColor(Color.argb(100, 255, 255, 255)))); // white circle for first
                continue;
            }

            if (j == (waypoints.size() - 1)) {
                waypointCircleList.add(mMap.addCircle(new CircleOptions()
                        .center(new LatLng(waypoints.get(j).latitude_, waypoints.get(j).longitude_))
                        .radius(wayPointCircleRadius + 3.0f)
                        .strokeWidth(3)
                        .zIndex(zlayer + 5000.0f) // above the polylines, below the drone and user
                        .strokeColor(Color.BLACK)
                        .fillColor(Color.argb(100, 0, 0, 0)))); // black circle for last
                continue;
            }

            waypointCircleList.add(mMap.addCircle(new CircleOptions()
                    .center(new LatLng(waypoints.get(j).latitude_, waypoints.get(j).longitude_))
                    .radius(wayPointCircleRadius)
                    .strokeWidth(3)
                    .zIndex(zlayer)
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.argb(255, 255, 0, 0)))); // red circles
        }
        numWaypointsTotal = waypoints.size(); // do not use polyline size (1 extra)

        // add the poly line between swaths

        double latWayPointZero = waypoints.get(0).latitude_;
        double longWayPointZero = waypoints.get(0).longitude_;
        double latWayPointOne = waypoints.get(1).latitude_;
        double longWayPointOne = waypoints.get(1).longitude_;

        boolean switchBackIsUpDown = false;
        if (latWayPointZero != latWayPointOne) {
            switchBackIsUpDown = true;
        } else {
            switchBackIsUpDown = false;
        }

        wayPointPolyLineList = new ArrayList<Polyline>();
        if (switchBackIsUpDown) {
            // look for changes in long
            double currentLong = waypoints.get(0).longitude_;
            for (int j = 1; j < waypoints.size() - 1; j++) {
                if (waypoints.get(j).longitude_ != currentLong) {

                    // Connect drone to first waypoint
                    wayPointPolyLineList.add(mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(waypoints.get(j - 1).latitude_, waypoints.get(j - 1).longitude_), new LatLng(waypoints.get(j).latitude_, waypoints.get(j).longitude_))
                            .width(wayPointCircleRadius + 3.0f)
                            .zIndex(3004.0f)
                            .color(Color.BLACK)));
                } else {
                    currentLong = waypoints.get(j).longitude_;
                }
            }
        } else {
            // look for changes in long
            double currentLat = waypoints.get(0).latitude_;
            for (int j = 1; j < waypoints.size() - 1; j++) {
                if (waypoints.get(j).latitude_ != currentLat) {

                    // Connect drone to first waypoint
                    wayPointPolyLineList.add(mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(waypoints.get(j - 1).latitude_, waypoints.get(j - 1).longitude_), new LatLng(waypoints.get(j).latitude_, waypoints.get(j).longitude_))
                            .width(wayPointCircleRadius + 3.0f)
                            .zIndex(3004.0f)
                            .color(Color.BLACK)));
                    currentLat = waypoints.get(j).latitude_;
                } else {
                    currentLat = waypoints.get(j).latitude_;
                }
            }
        }


    }

    private void reachedWaypointAtIndex(int waypointIndex) {
        myASM = new ApplicationSettingsManager(fragmentActivity);
        wayPointCircleRadius = myASM.getWaypointSizeFromSettings();
        Log.d("MapPresenter", "Waypoint Reached: " + waypointIndex);
        // Make the waypoint circles
        numWaypointsCompleted++;
        zlayer++;
        if (waypointIndex == (waypointCircleList.size() - 2)) { // if second last print last as well
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(waypointCircleList.get(waypointIndex + 1).getCenter().latitude, waypointCircleList.get(waypointIndex + 1).getCenter().longitude))
                    .radius(wayPointCircleRadius + 3.0d)
                    .strokeWidth(3)
                    .zIndex(zlayer)
                    .strokeColor(Color.BLACK)
                    .clickable(false)
                    .fillColor(Color.argb(255, 0, 255, 0))); // green for completed waypoint
        }
        if (waypointIndex == 0) {
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(waypointCircleList.get(waypointIndex).getCenter().latitude, waypointCircleList.get(waypointIndex).getCenter().longitude))
                    .radius(wayPointCircleRadius + 3.0d)
                    .strokeWidth(3)
                    .zIndex(zlayer)
                    .strokeColor(Color.BLACK)
                    .clickable(false)
                    .fillColor(Color.argb(255, 0, 255, 0))); // green for completed waypoint
        }
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(waypointCircleList.get(waypointIndex).getCenter().latitude, waypointCircleList.get(waypointIndex).getCenter().longitude))
                .radius(wayPointCircleRadius)
                .strokeWidth(3)
                .zIndex(zlayer)
                .strokeColor(Color.BLACK)
                .clickable(false)
                .fillColor(Color.argb(255, 0, 255, 0))); // green for completed waypoint

        percentageCompletion = (int) (100.0d * numWaypointsCompleted / numWaypointsTotal);
        surveyProgressBar.setProgress((int) percentageCompletion);
        txtPercentCompleteMap.setText(percentageCompletion + "% complete.");
    }

    void initSurveyBox() {


        // User changes the survey area height by sliding the seek bar
        surveyAreaHeightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (haveAnimatedCameraToUserMarker) {
                    if (polySurveyAreaPicked) { // if an area selector is on the map
                        if (progress < 10) {
                            surveyAreaHeightBar.setProgress(10);
                            return;
                        }
                        drawAreaSelector(false);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (surveyPolygon != null)
                    surveyPolygon.setFillColor(Color.argb(125, 0, 0, 0)); // fill color only after the user stops scaling pbar
            }
        });

        // User changes the survey area width by sliding the seek bar
        surveyAreaWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (haveAnimatedCameraToUserMarker) {
                    if (polySurveyAreaPicked) {
                        if (progress < 10) {
                            surveyAreaWidthBar.setProgress(10);
                            return;
                        }
                        drawAreaSelector(false);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (surveyPolygon != null)
                    surveyPolygon.setFillColor(Color.argb(125, 0, 0, 0));
            }
        });
    }

    //@Override
    public void clearMap() {
        if (waypointCircleList != null) waypointCircleList.clear();
        if (wayPointPolyLineList != null) wayPointPolyLineList.clear();
        mMap.clear();
        if (lastKnownUserLocation == null) {
            drawUserUpdate(new LatLng(userMarker.getPosition().latitude, userMarker.getPosition().longitude));
            drawDroneUpdate();
        } else {
            drawUserUpdate(lastKnownUserLocation);
        }


        mMap.setOnMapLoadedCallback(null); // don't need anymore since loaded
        disableMapGestures();
        drawAreaSelector(true);
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(userMarker.getPosition().latitude, userMarker.getPosition().longitude + 0.001), 18.0f);
        mMap.animateCamera(cu, 3000, MapPresenter.this);
        surveyProgressBar.setProgress(0);
        numWaypointsCompleted = 0;
        numWaypointsTotal = 0;
        surveyAreaWidthBar.setEnabled(true);
        surveyAreaHeightBar.setEnabled(true);
        txtPercentCompleteMap.setText("0.00% complete.");
        return;
    }

    public class GPSSignalListener implements android.location.LocationListener {
        public void onLocationChanged(Location location) {
            lastKnownUserLocation = location;
            // Called when a new location is found by the network location provider.
            long timeSinceLastUserPositionUpdate = System.nanoTime() - userGPSStartTime;
            if (timeSinceLastUserPositionUpdate > 100000000l) { // 1 second between updates to reduce lag
                userGPSStartTime = System.nanoTime(); // reset time
                if (location == null && !introZoomFinished) {
                    return;
                }
                drawUserUpdate(location);
                //Log.d("MapPresenter", "Camera Zoom: " + mMap.getCameraPosition().zoom);
                if (!haveAnimatedCameraToUserMarker) {
                    haveAnimatedCameraToUserMarker = true;
                    drawDroneUpdate();
                    mMap.setOnMapLoadedCallback(null); // don't need anymore since loaded
                    // writeToast("GPS Signal Acquired.");
                    disableMapGestures();
                    areaSelectingMaskMidpoint = new LatLng(location.getLatitude(), location.getLongitude() + 0.0009);
                    drawAreaSelector(true);

                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(userMarker.getPosition().latitude, userMarker.getPosition().longitude + 0.001), 18.0f);
                    mMap.animateCamera(cu, 3000, MapPresenter.this);

                }
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////// UTILITIES AND HELPERS /////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    private void drawUserUpdate(Location location) {
        userMarkerIcon = getMarkerIconFromDrawable(userDrawable, 40 + (int) (70 * ((int) (mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 40 + (int) (70.0f * ((int) (mMap.getCameraPosition().zoom - 3.00f) / 15.0f)));
        Marker tempUserMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .zIndex(10010.0f)
                .icon(userMarkerIcon));
        if (userMarker != null) userMarker.remove();

        userMarker = tempUserMarker;
    }

    private void drawUserUpdate(LatLng position) {
        userMarkerIcon = getMarkerIconFromDrawable(userDrawable, 40 + (int) (70 * ((int) (mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 40 + (int) (70.0f * ((int) (mMap.getCameraPosition().zoom - 3.00f) / 15.0f)));
        Marker tempUserMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(position.latitude, position.longitude))
                .zIndex(10010.0f)
                .icon(userMarkerIcon));
        if (userMarker != null) userMarker.remove();

        userMarker = tempUserMarker;
    }

    private void drawAreaSelector(Boolean fillColor) {

        polySurveyAreaPicked = true;
        dbl_HeightSeek = ((double) surveyAreaHeightBar.getProgress()) / dbl_HeightScaler;
        dbl_WidthSeek = ((double) surveyAreaWidthBar.getProgress()) / dbl_WidthScaler;
        // Log.d("MapPresenter", "Height Seek: " + dbl_HeightSeek);
        // Log.d("MapPresenter", "Width Seek: " + dbl_WidthSeek);

        double lati = areaSelectingMaskMidpoint.latitude; // store the latitude of the tap
        double longi = areaSelectingMaskMidpoint.longitude; // store the longitude of the tap
        Polygon temp_surveyPolygon;
        if (fillColor) {
            temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                    .add(new LatLng((lati - dbl_HeightSeek), (longi - dbl_WidthSeek)), new LatLng((lati + dbl_HeightSeek), (longi - dbl_WidthSeek)), new LatLng((lati + dbl_HeightSeek), (longi + dbl_WidthSeek)), new LatLng((lati - dbl_HeightSeek), (longi + dbl_WidthSeek)))
                    .strokeWidth(2)
                    .strokeColor(Color.BLACK)
                    .zIndex(3005.0f)
                    .fillColor(Color.argb(125, 0, 0, 0))
                    .clickable(false));
        } else {
            temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                    .add(new LatLng((lati - dbl_HeightSeek), (longi - dbl_WidthSeek)), new LatLng((lati + dbl_HeightSeek), (longi - dbl_WidthSeek)), new LatLng((lati + dbl_HeightSeek), (longi + dbl_WidthSeek)), new LatLng((lati - dbl_HeightSeek), (longi + dbl_WidthSeek)))
                    .strokeWidth(2)
                    .strokeColor(Color.BLACK)
                    .zIndex(3005.0f)
                    .clickable(false));
        }
        if (surveyPolygon != null) surveyPolygon.remove();
        surveyPolygon = temp_surveyPolygon;
        // Go the new GPS coordinates of the user
        Double heightDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(1).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(1).longitude, 0d, 0d);
        surveyAreaHeightText.setText("Survey Area Height: " + String.valueOf(round(heightDistance, 0)) + " m");
        Double widthDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(3).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(3).longitude, 0d, 0d);
        surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(round(widthDistance, 0)) + " m");
    }

    private void drawAreaSelector(Boolean fillColor, LatLng CenterScreen) {

        polySurveyAreaPicked = true;
        dbl_HeightSeek = ((double) surveyAreaHeightBar.getProgress()) / dbl_HeightScaler;
        dbl_WidthSeek = ((double) surveyAreaWidthBar.getProgress()) / dbl_WidthScaler;
        // Log.d("MapPresenter", "Height Seek: " + dbl_HeightSeek);
        // Log.d("MapPresenter", "Width Seek: " + dbl_WidthSeek);
        areaSelectingMaskMidpoint = CenterScreen;
        double lati = areaSelectingMaskMidpoint.latitude; // store the latitude of the tap
        double longi = areaSelectingMaskMidpoint.longitude; // store the longitude of the tap
        Polygon temp_surveyPolygon;
        if (fillColor) {
            temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                    .add(new LatLng((lati - dbl_HeightSeek), (longi - dbl_WidthSeek)), new LatLng((lati + dbl_HeightSeek), (longi - dbl_WidthSeek)), new LatLng((lati + dbl_HeightSeek), (longi + dbl_WidthSeek)), new LatLng((lati - dbl_HeightSeek), (longi + dbl_WidthSeek)))
                    .strokeWidth(2)
                    .strokeColor(Color.BLACK)
                    .zIndex(3005.0f)
                    .fillColor(Color.argb(125, 0, 0, 0))
                    .clickable(false));
        } else {
            temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                    .add(new LatLng((lati - dbl_HeightSeek), (longi - dbl_WidthSeek)), new LatLng((lati + dbl_HeightSeek), (longi - dbl_WidthSeek)), new LatLng((lati + dbl_HeightSeek), (longi + dbl_WidthSeek)), new LatLng((lati - dbl_HeightSeek), (longi + dbl_WidthSeek)))
                    .strokeWidth(2)
                    .strokeColor(Color.BLACK)
                    .zIndex(3005.0f)
                    .clickable(false));
        }
        if (surveyPolygon != null) surveyPolygon.remove();
        surveyPolygon = temp_surveyPolygon;
        // Go the new GPS coordinates of the user
        Double heightDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(1).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(1).longitude, 0d, 0d);
        surveyAreaHeightText.setText("Survey Area Height: " + String.valueOf(round(heightDistance, 0)) + " m");
        Double widthDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(3).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(3).longitude, 0d, 0d);
        surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(round(widthDistance, 0)) + " m");
    }


    private void drawDroneUpdate() {
        long timeSinceLastDronePositionUpdate = System.nanoTime() - droneGPSStartTime;
        if (timeSinceLastDronePositionUpdate > 50000000l) { // 8 seconds between updates to reduce lag
            droneGPSStartTime = System.nanoTime(); // reset time
            droneMarkerIcon = getMarkerIconFromDrawable(droneDrawable, 50 + (int) (70.0f * 1.85f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 50 + (int) (70.0f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f))); // aspect ratio = l/w = 1.85
            if (droneLocation_.droneLocation() == null && lastKnownUserLocation == null) {
                //LatLng calgaryLatLng = new LatLng(51.0486, -114.0708);
                //LatLng calgaryUniLatLng = new LatLng(51.079948, -114.125534);
                droneMarkerIcon = getMarkerIconFromDrawable(droneDrawable, 50 + (int) (70.0f * 1.85f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 50 + (int) (70.0f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f))); // aspect ratio = l/w = 1.85
                Marker tempdroneMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(userMarker.getPosition().latitude - 0.0006, userMarker.getPosition().longitude + 0.001))
                        .zIndex(10000.0f)
                        .icon(droneMarkerIcon));
                if (droneMarker != null) droneMarker.remove(); // no flicker
                droneMarker = tempdroneMarker;
                return;

            } else if (droneLocation_.droneLocation() == null && lastKnownUserLocation != null) { // place drone marker next to user
                Marker tempdroneMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lastKnownUserLocation.getLatitude() - 0.0006, lastKnownUserLocation.getLongitude() + 0.001))
                        .zIndex(10000.0f)
                        .icon(droneMarkerIcon));
                if (droneMarker != null) droneMarker.remove(); // no flicker
                droneMarker = tempdroneMarker;
                return;
            } else {
                Marker tempdroneMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(droneLocation_.droneLocation().latitude_, droneLocation_.droneLocation().longitude_))
                        .zIndex(10000.0f)
                        .icon(droneMarkerIcon));
                if (droneMarker != null) droneMarker.remove(); // no flicker
                droneMarker = tempdroneMarker;
            }

        }
    }

    private void initCachedMapChecker() {
        // check if there is no internet, if so - switch to cached map
        new CountDownTimer(Long.MAX_VALUE, 10000) {
            public void onTick(long millisUntilFinished) {
                if (offLineTileProvider == null) {
                    return;
                }
                if (needToTellUserCachedMapAvailable) {
                    needToTellUserCachedMapAvailable = false;
                    writeToast("Cached map available.");
                }

                if (!isNetworkAvailable() && !usingCachedMap) {
                    usingCachedMap = true;
                    Log.d("MapPresenter", "Detected no internet connection. Therefore, since the Cached Map data is ready - switching to Cached Map mode.");
                    offlineOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(offLineTileProvider).zIndex(0).transparency(0.0f));
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    if (mMap.getCameraPosition().zoom > 17.0f) {
                        if (userMarker != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userMarker.getPosition().latitude, userMarker.getPosition().longitude), 14.0f));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.0486, -114.0708), 14.0f));
                        }
                        mMap.setMaxZoomPreference(17.0f);
                    }

                    //writeToast("No Internet Connection. Switching to Cached Map mode.");
                }

                if (isNetworkAvailable() && usingCachedMap) {
                    usingCachedMap = false;
                    mMap.setMaxZoomPreference(25.0f);
                    if (userMarker != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userMarker.getPosition().latitude, userMarker.getPosition().longitude), 14.0f));
                    } else {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.0486, -114.0708), 14.0f));
                    }
                    Log.d("MapPresenter", "Internet Connection Found. Switching back to Google Hybrid Map");
                    if (offlineOverlay != null) offlineOverlay.remove();
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    //writeToast("Internet Connection Found. Switching back to Google Hybrid Map");
                }
            }

            public void onFinish() {

            }

        }.start();
    }


    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {
        final int R = 6371; // Radius of the earth
        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    void writeToast(final String message) {
        Context context = fragmentActivity.getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_LONG;
        Toast toastConsole = Toast.makeText(context, text, duration);
        toastConsole.show();

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable, int w, int h) {
        Canvas canvas = new Canvas();
        //Log.d("MapPresenter", "Icon Marker Width: " + String.valueOf(drawable.getIntrinsicWidth()));
        //Log.d("MapPresenter", "Icon Marker Height: " + String.valueOf(drawable.getIntrinsicHeight()));
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) fragmentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void notifyOfThreadComplete(final Thread thread) {
        if (thread == cachedMapLoaderThread) {
            Log.d("MapPresenter", " NotifyOfThreadComplete ID: " + currentThread().getName());
            this.offLineTileProvider = cachedMapLoaderThread.getTileProvider();
            Log.d("MapPresenter", "Cached Map Files Loaded Successfully.");
        }
    }

    void initCachedMap() { // non-blocking
        // Looks for the 2 cache files in the root directory
        // Then builds the data structures required to use the cached tiles
        this.offlineOverlay = null;
        this.offLineTileProvider = null;
        String csvFilestr = Environment.getExternalStorageDirectory().toString() + "/mapcachebinaryIndex.csv";
        String binFilestr = Environment.getExternalStorageDirectory().toString() + "/mapcachebinaryData.bin";
        File csvFileF = new File(csvFilestr);
        File binFileF = new File(binFilestr);
        if (csvFileF.exists() && binFileF.exists()) {
            Log.d("MapPresenter", "Cached Map Files found. Attempting to load Map Tiles.");
            //writeToast("Cached Map Files found. Attempting to load Map Tiles.");
            cachedMapLoaderThread = new CachedMapLoaderThread();
            cachedMapLoaderThread.setFragmentActivity(fragmentActivity);
            cachedMapLoadNotifier = cachedMapLoaderThread;
            cachedMapLoadNotifier.addListener(this);
            cachedMapLoadNotifier.start(); // starts another thread
        } else {
            //writeToast("Cached Map files not found in /root/. Offline maps disabled.");
            Log.e("MapPresenter", "Parsing Cached Map Data Failed! Make sure the files mapcachebinaryData.bin and mapcachebinaryIndex.csv are both in the /root/ directory.");
            return;
        }

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
                drawDroneUpdate();
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(droneLocationChangedReceiver_, filter);
    }

    // Google map animation callback
    public void onFinish() {
        enableMapGestures();
        introZoomFinished = true;

        if (missionState.getCurrentMissionState() == MissionStateEnum.INITIALIZING_MAP) {
            btnFindMeNow.setVisibility(View.VISIBLE);
            missionState.setCurrentMissionState(MissionStateEnum.SELECT_AREA);
            this.surveyAreaHeightBar.setEnabled(true);
            this.surveyAreaWidthBar.setEnabled(true);
        }
    }

    // Google map animation callback
    public void onCancel() {
        enableMapGestures();
        introZoomFinished = true;
        if (missionState.getCurrentMissionState() == MissionStateEnum.INITIALIZING_MAP) {
            btnFindMeNow.setVisibility(View.VISIBLE);
            missionState.setCurrentMissionState(MissionStateEnum.SELECT_AREA);
            this.surveyAreaHeightBar.setEnabled(true);
            this.surveyAreaWidthBar.setEnabled(true);
        }
    }


    @Override
    public MissionBoundary getSurveyAreaBoundary() {
        Coordinate topRight = new Coordinate(
                surveyPolygon.getPoints().get(2).latitude,
                surveyPolygon.getPoints().get(2).longitude);
        Coordinate bottomLeft = new Coordinate(
                surveyPolygon.getPoints().get(0).latitude,
                surveyPolygon.getPoints().get(0).longitude);
        return new MissionBoundary(topRight, bottomLeft);
    }


    private void disableMapGestures() {
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void enableMapGestures() {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void getViews(I_MapView mapView) {
        surveyProgressBar = mapView.surveyProgressBar();
        surveyAreaHeightText = mapView.surveyAreaHeightText();
        surveyAreaHeightBar = mapView.surveyAreaHeightBar();
        surveyAreaWidthText = mapView.surveyAreaWidthText();
        surveyAreaWidthBar = mapView.surveyAreaWidthBar();
        startMission = mapView.startMissionButton();
        txtPercentCompleteMap = mapView.txtPercentCompleteMap();
        btnFindMeNow = mapView.btnFindMeNow();
    }

    void initAndroidGPS() {

        if (!(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(fragmentActivity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(fragmentActivity.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            userLocationManager = (LocationManager) fragmentActivity.getSystemService(Context.LOCATION_SERVICE);
            userLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, userLocationListener);
            Log.d("MapPresenter", "Android GPS Service Started (Permissions Granted)");
            //writeToast("Android GPS Service Started (Permissions Granted)");
            Location locationGPS = userLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = userLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            long GPSLocationTime = 0;
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }
            long NetLocationTime = 0;
            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }
            if (0 < GPSLocationTime - NetLocationTime) {
                userLocationListener.onLocationChanged(locationGPS); // send the last GPS coordinates right away
            } else {
                userLocationListener.onLocationChanged(locationNet);
            }
        } else {
            Log.e("MapPresenter", "Android GPS Service Error (Permissions NOT Granted)");
            // Keep checking for GPS permission - writeToast("No GPS Service available.");
            new CountDownTimer(Long.MAX_VALUE, 6000) {
                public void onTick(long millisUntilFinished) {
                    if (!(Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission(fragmentActivity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(fragmentActivity.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                        userLocationManager = (LocationManager) fragmentActivity.getSystemService(Context.LOCATION_SERVICE);
                        userLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, userLocationListener);
                        Log.d("MapPresenter", "Android GPS Service Started (Permissions Granted)");
                        // writeToast("Android GPS Service Started (Permissions Granted)");
                        Location locationGPS = userLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Location locationNet = userLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        long GPSLocationTime = 0;
                        if (null != locationGPS) {
                            GPSLocationTime = locationGPS.getTime();
                        }
                        long NetLocationTime = 0;

                        if (null != locationNet) {
                            NetLocationTime = locationNet.getTime();
                        }
                        if (0 < GPSLocationTime - NetLocationTime) {
                            userLocationListener.onLocationChanged(locationGPS); // send the last GPS coordinates right away
                        } else {
                            userLocationListener.onLocationChanged(locationNet);
                        }

                        this.cancel();

                    } else {
                        //writeToast("No GPS Service available.");
                    }
                }

                public void onFinish() {
                }
            }.start();
        }


    }


    void initMiscMapListeners() {

        btnFindMeNow.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (userMarker == null) return; // can't find... user marker not showing on map
                                                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(userMarker.getPosition().latitude, userMarker.getPosition().longitude + 0.001), 18.0f);
                                                mMap.animateCamera(cu, 3000, MapPresenter.this);


                                            }
                                        }
        );


        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    boolean doNotMoveCameraToCenterMarker = true;

                    public boolean onMarkerClick(Marker marker) {
                        return doNotMoveCameraToCenterMarker; // ignore any Marker Click
                    }
                });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                initAndroidGPS();
            }
        });
        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {

            public void onPolygonClick(Polygon areaSelector) {
                surveyAreaHeightBar.setVisibility(View.GONE);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (haveAnimatedCameraToUserMarker && missionState.getCurrentMissionState() == MissionStateEnum.SELECT_AREA) {
                    areaSelectingMaskMidpoint = point;
                    drawAreaSelector(true);
                }
            }

        });


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng pointOfLongClick) {

                if (missionState.getCurrentMissionState() == MissionStateEnum.INITIALIZING_MAP) {
                    //startMission.setText("Demo Mode Activated. Return to the Main Menu to reset.");
                    // Called when a new location is found by the network location provider.
                    userGPSStartTime = System.nanoTime(); // reset time
                    // LatLng calgaryLatLng = new LatLng(51.0486, -114.0708);
                    LatLng calgaryUniLatLng = new LatLng(51.079948, -114.125534);
                    drawUserUpdate(calgaryUniLatLng);
                    //Log.d("MapPresenter", "Camera Zoom: " + mMap.getCameraPosition().zoom);
                    if (!haveAnimatedCameraToUserMarker) {
                        haveAnimatedCameraToUserMarker = true;
                        drawDroneUpdate();
                        // writeToast("GPS Signal Acquired.");
                        disableMapGestures();
                        areaSelectingMaskMidpoint = new LatLng(calgaryUniLatLng.latitude, calgaryUniLatLng.longitude + 0.0009);
                        drawAreaSelector(true);

                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(calgaryUniLatLng.latitude, calgaryUniLatLng.longitude + 0.001), 18.0f);
                        mMap.animateCamera(cu, 3000, MapPresenter.this);

                    }


                }


            }


        });

        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraIdleListener(this);
    }

    boolean draggingpoly = false;

    @Override
    public void onCameraMoveStarted(int reason) {
        if (missionState.getCurrentMissionState() == MissionStateEnum.SELECT_AREA) {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                if (polySurveyAreaPicked) {
                    draggingpoly = true;
                    LatLng LatLngOfCenterScreen = mMap.getCameraPosition().target;
                    drawAreaSelector(false, LatLngOfCenterScreen);
                }


            }
        }
    }


    @Override
    public void onCameraMove() {
        if (polySurveyAreaPicked) {
            if (draggingpoly) {
                LatLng LatLngOfCenterScreen = mMap.getCameraPosition().target;
                drawAreaSelector(false, LatLngOfCenterScreen);
            }
        }


    }

    @Override
    public void onCameraIdle() {
        if (polySurveyAreaPicked) {
            if (draggingpoly) {

                draggingpoly = false;
                LatLng LatLngOfCenterScreen = mMap.getCameraPosition().target;
                drawAreaSelector(true, LatLngOfCenterScreen);
            }
        }
    }

    public void pbarsHide() {
        surveyAreaHeightBar.setVisibility(View.GONE);
        surveyAreaWidthBar.setVisibility(View.GONE);
        surveyAreaHeightText.setVisibility(View.GONE);
        surveyAreaWidthText.setVisibility(View.GONE);
    }

    public void pbarsShow() {
        surveyAreaHeightBar.setVisibility(View.VISIBLE);
        surveyAreaWidthBar.setVisibility(View.VISIBLE);
        surveyAreaHeightText.setVisibility(View.VISIBLE);
        surveyAreaWidthText.setVisibility(View.VISIBLE);
    }

}
