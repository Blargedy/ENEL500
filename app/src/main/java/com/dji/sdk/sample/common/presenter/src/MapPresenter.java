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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.entity.MissionStateEntity;
import com.dji.sdk.sample.common.entity.MissionStateEnum;
import com.dji.sdk.sample.common.utility.IntentExtraKeys;
import com.dji.sdk.sample.common.values.MissionBoundary;
import com.dji.sdk.sample.common.presenter.api.I_MapPresenter;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.CachedMapLoaderThread;
import com.dji.sdk.sample.common.utility.CachedMapNotifyingThread;
import com.dji.sdk.sample.common.utility.I_CachedMapThreadCompletedListener;
import com.dji.sdk.sample.common.values.Coordinate;
import com.dji.sdk.sample.common.view.api.I_MapView;
import com.dji.sdk.sample.common.view.src.FlightControlView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

import static com.dji.sdk.sample.R.id.btn_accept_area;
import static com.dji.sdk.sample.R.id.map;
import static com.dji.sdk.sample.R.id.pbar_surveyAreaHeight;
import static com.dji.sdk.sample.R.id.start;
import static com.dji.sdk.sample.R.id.surveyProgressBar;
import static com.dji.sdk.sample.common.utility.IntentExtraKeys.WAYPOINT_INDEX;
import static java.lang.Thread.currentThread;

public class MapPresenter implements
        I_MapPresenter,
        OnMapReadyCallback, I_CachedMapThreadCompletedListener, GoogleMap.CancelableCallback {
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
    private final double dbl_SurveyBoxScaler = 50.0;
    private final double dbl_InitialSurveyBoxSeekValue = 50.0;
    private final double dbl_areaSS = 0.0005;
    // Area Selector dimension variables
    private double dbl_HeightSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;
    private double dbl_WidthSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;
    // Controls
    private SeekBar surveyProgressBar;
    private TextView surveyAreaHeightText;
    private SeekBar surveyAreaHeightBar;
    private TextView surveyAreaWidthText;
    private SeekBar surveyAreaWidthBar;
    private LinearLayout mainLinearLayoutVert;
    // Lists
    static ArrayList<Circle> waypointCircleList;
    static ArrayList<Polyline> wayPointPolyLineList;
    // Progress Tracking
    private double percentageCompletion = 0.00;
    private int numWaypointsTotal = 0;
    private int numWaypointsCompleted = 0;
    // GPS Drone
    private boolean droneHasNotUsedRealGPSYet = true;
    private BroadcastReceiver droneLocationChangedReceiver_;
    private DroneLocationEntity droneLocation_;
    private long droneGPSStartTime = System.nanoTime();
    private final Drawable droneDrawable;
    private BitmapDescriptor droneMarkerIcon;
    private boolean allowRealTimeDroneGPS = false;
    // GPS User
    private LocationListener userLocationListener;
    private LocationManager userLocationManager;
    private boolean haveAnimatedCameraToUserMarker = false;
    private boolean introZoomFinished = false;
    private long userGPSStartTime = System.nanoTime() + 5000000000l;
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

    public MapPresenter(
            FragmentActivity fragmentActivity,
            GoogleApiClient googleApiClient,
            I_MapView mapView,
            DroneLocationEntity droneLocation, MissionStateEntity missionState_) {
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentActivity.
                getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
        missionState = missionState_;

        client = googleApiClient;
        this.fragmentActivity = fragmentActivity;
        surveyProgressBar = mapView.surveyProgressBar();
        surveyAreaHeightText = mapView.surveyAreaHeightText();
        surveyAreaHeightBar = mapView.surveyAreaHeightBar();
        surveyAreaWidthText = mapView.surveyAreaWidthText();
        surveyAreaWidthBar = mapView.surveyAreaWidthBar();
        mainLinearLayoutVert = mapView.linearLayoutMainV();

        droneDrawable = fragmentActivity.getResources().getDrawable(R.drawable.drone_marker);
        userDrawable = fragmentActivity.getResources().getDrawable(R.drawable.android_marker);
        droneLocation_ = droneLocation;
        registerWaypointReachedReceiver(fragmentActivity);
        registerDroneLocationChangedReceiver(fragmentActivity);
        starterIntent = fragmentActivity.getIntent();
        missionState.setCurrentMissionState(MissionStateEnum.INITIALIZING_MAP);




        this.surveyAreaHeightBar.setEnabled(false);
        this.surveyAreaWidthBar.setEnabled(false);
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
        Coordinate topRight = new Coordinate(
                surveyPolygon.getPoints().get(2).latitude,
                surveyPolygon.getPoints().get(2).longitude);
        Coordinate bottomLeft = new Coordinate(
                surveyPolygon.getPoints().get(0).latitude,
                surveyPolygon.getPoints().get(0).longitude);
        return new MissionBoundary(topRight, bottomLeft);
    }

    @Override
    public void displayMissionWaypoints(Vector<Coordinate> waypoints) {
        if (surveyPolygon != null) surveyPolygon.remove(); //no longer needed
        this.surveyAreaHeightBar.setEnabled(false);
        this.surveyAreaWidthBar.setEnabled(false);
        // Make net between each waypoint marker
        wayPointPolyLineList = new ArrayList<Polyline>();
        // Connect drone to first waypoint
        wayPointPolyLineList.add(mMap.addPolyline(new PolylineOptions()
                .add(droneMarker.getPosition(), new LatLng(waypoints.get(0).latitude_, waypoints.get(0).longitude_))
                .width(5)
                .zIndex(3004.0f)
                .color(Color.BLACK)));

        for (int i = 0; i < waypoints.size(); i++) {
            LatLng waypointLatLng1 = new LatLng(waypoints.get(i).latitude_, waypoints.get(i).longitude_);
            if (i == (waypoints.size() - 1)) { //connect last waypoint to drone
                wayPointPolyLineList.add(mMap.addPolyline(new PolylineOptions()
                        .add(waypointLatLng1, droneMarker.getPosition())
                        .width(5)
                        .zIndex(3004.0f)
                        .color(Color.BLACK)));
                break;
            }
            LatLng waypointLatLng2 = new LatLng(waypoints.get(i + 1).latitude_, waypoints.get(i + 1).longitude_);
            wayPointPolyLineList.add(mMap.addPolyline(new PolylineOptions()
                    .add(waypointLatLng1, waypointLatLng2)
                    .width(5)
                    .zIndex(3004.0f)
                    .color(Color.BLACK)));
        }

        // Make the waypoint circles
        waypointCircleList = new ArrayList<Circle>();
        for (int j = 0; j < waypoints.size(); j++) {
            waypointCircleList.add(mMap.addCircle(new CircleOptions()
                    .center(new LatLng(waypoints.get(j).latitude_, waypoints.get(j).longitude_))
                    .radius(3)
                    .strokeWidth(3)
                    .zIndex(3004.5f) // above the polylines, below the drone and user
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.argb(150, 255, 0, 0)))); // transparent red circles
        }
        numWaypointsTotal = waypoints.size(); // do not use polyline size (1 extra)
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(waypointCircleList.get(0).getCenter().latitude, waypointCircleList.get(0).getCenter().longitude), 17.0f);
        mMap.animateCamera(cu, 2000, MapPresenter.this);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Implement map interactions
        Toast toastConsole = Toast.makeText(fragmentActivity.getApplicationContext(), "Waiting for GPS signal...", Toast.LENGTH_LONG);
        toastConsole.show();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng calgaryLatLng = new LatLng(51.0486, -114.0708);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calgaryLatLng, 10.0f));
        mMap = googleMap;
        surveyProgressBar.setEnabled(false);
        // For now place drone and 'android' markers manually
        surveyAreaHeightBar.setProgress(50);
        surveyAreaWidthBar.setProgress(50);
        surveyAreaHeightText.setText("Survey Area Height: 111.2 m");
        surveyAreaWidthText.setText("Survey Area Width: 69.9 m");
        mMap.setMinZoomPreference(0.00f);
        mMap.setMaxZoomPreference(25.0f);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        initSurveyBox();
        initCachedMap();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
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
                    // Keep checking for GPS permission
                    //writeToast("No GPS Service available.");
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
        });


        userLocationListener = new

                LocationListener() {
                    public void onLocationChanged(Location location) {
                        lastKnownUserLocation = location;
                        // Called when a new location is found by the network location provider.
                        long timeSinceLastUserPositionUpdate = System.nanoTime() - userGPSStartTime;
                        if (timeSinceLastUserPositionUpdate > 5000000000l) { // 8 seconds between updates to reduce lag
                            userGPSStartTime = System.nanoTime(); // reset time
                            if (location == null && !introZoomFinished) {
                                // no GPS position do nothing - cancel.
                                return;
                            }
                            //Log.d("MapPresenter", "Camera Zoom: " + mMap.getCameraPosition().zoom);

                            userMarkerIcon = getMarkerIconFromDrawable(userDrawable, 40 + (int) (70 * ((int) (mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 40 + (int) (70.0f * ((int) (mMap.getCameraPosition().zoom - 3.00f) / 15.0f)));
                            Marker tempUserMarker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zIndex(3005.0f)
                                    .icon(userMarkerIcon));
                            // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // green marker
                            // Log.d("MapPresenter", "User Location: latitude=" + location.getLatitude() + " longitude=" + location.getLongitude());
                            if (userMarker != null) userMarker.remove();

                            userMarker = tempUserMarker;
                            if (!haveAnimatedCameraToUserMarker) {
                                haveAnimatedCameraToUserMarker = true;

                                droneMarkerIcon = getMarkerIconFromDrawable(droneDrawable, 50 + (int) (130.0f * 1.85f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 50 + (int) (130 * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f))); // aspect ratio = l/w = 1.85
                                droneMarker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(location.getLatitude() - 0.0006, location.getLongitude() + 0.001))
                                        .zIndex(3005.0f)
                                        .icon(droneMarkerIcon));
                                mMap.setOnMapLoadedCallback(null); // don't need anymore since loaded
                                //writeToast("GPS Signal Acquired.");
                                mMap.getUiSettings().setAllGesturesEnabled(false);
                                mMap.getUiSettings().setScrollGesturesEnabled(false);
                                mMap.getUiSettings().setZoomControlsEnabled(false);
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                polySurveyAreaPicked = true;
                                dbl_HeightSeek = (double) surveyAreaHeightBar.getProgress() / dbl_SurveyBoxScaler;
                                dbl_WidthSeek = (double) surveyAreaWidthBar.getProgress() / dbl_SurveyBoxScaler;
                                double lati = userMarker.getPosition().latitude + 0.0001; // store the latitude of the tap
                                double longi = userMarker.getPosition().longitude + 0.001; // store the longitude of the tap
                                areaSelectingMaskMidpoint = (new LatLng(lati, longi)); // save the tap LatLng globally
                                surveyPolygon = mMap.addPolygon(new PolygonOptions()
                                        .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                                        .strokeWidth(2)
                                        .strokeColor(Color.BLACK)
                                        .zIndex(3005.0f)
                                        .fillColor(Color.argb(125, 0, 0, 0)) // black that is 50% opaque (see through)
                                        .clickable(true));

                                // Go the new GPS coordinates of the user
                                Double heightDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(1).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(1).longitude, 0d, 0d);
                                surveyAreaHeightText.setText("Survey Area Height: " + String.valueOf(round(heightDistance, 1)) + " m");
                                Double widthDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(3).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(3).longitude, 0d, 0d);
                                surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(round(widthDistance, 1)) + " m");
                                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(userMarker.getPosition().latitude, userMarker.getPosition().longitude + 0.001), 18.0f);
                                Intent intent = new Intent(BroadcastIntentNames.ERROR_OCCURRED);
                                intent.putExtra(IntentExtraKeys.ERROR_MESSAGE, "Zooming in!");
                                LocalBroadcastManager.getInstance(fragmentActivity).sendBroadcast(intent);
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
                };


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
                    if (mMap.getCameraPosition().zoom < 17.0f) {
                        CameraUpdate cu = CameraUpdateFactory.zoomTo(15.0f);
                        mMap.animateCamera(cu, 2000, MapPresenter.this);
                    }

                    //writeToast("No Internet Connection. Switching to Cached Map mode.");
                }

                if (isNetworkAvailable() && usingCachedMap) {
                    usingCachedMap = false;
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


    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
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


    // add this to I_MapView
    public void setallowRealTimeDroneGPS(boolean b) {
        this.allowRealTimeDroneGPS = b;
    }

    private void updateDroneLocation() {
        // if (allowRealTimeDroneGPS) {
        long timeSinceLastDronePositionUpdate = System.nanoTime() - droneGPSStartTime;
        if (timeSinceLastDronePositionUpdate > 5000000000l) { // 8 seconds between updates to reduce lag
            droneGPSStartTime = System.nanoTime(); // reset time
            if (droneLocation_.droneLocation() == null || !introZoomFinished) {
                // no GPS position do nothing - cancel.
                return;
            }
            if (droneHasNotUsedRealGPSYet) {
                //writeToast("Drone Location Acquired.");
                droneHasNotUsedRealGPSYet = false;
            }
            droneMarkerIcon = getMarkerIconFromDrawable(droneDrawable, 50 + (int) (130.0f * 1.85f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 50 + (int) (130 * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f))); // aspect ratio = l/w = 1.85

            Marker tempdroneMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(droneLocation_.droneLocation().latitude_, droneLocation_.droneLocation().longitude_))
                    .zIndex(3005.0f)
                    .icon(droneMarkerIcon));
            if (droneMarker != null) droneMarker.remove(); // no flicker
            droneMarker = tempdroneMarker;
            // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); // to change to actual phantom 4 icon
        }
        // }

    }


    // Google map animation callback
    public void onFinish() {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        introZoomFinished = true;
        if (missionState.getCurrentMissionState() == MissionStateEnum.INITIALIZING_MAP) {
            missionState.setCurrentMissionState(MissionStateEnum.SELECT_AREA);
            this.surveyAreaHeightBar.setEnabled(true);
            this.surveyAreaWidthBar.setEnabled(true);
        }
    }

    // Google map animation callback
    public void onCancel() {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        introZoomFinished = true;
        if (missionState.getCurrentMissionState() == MissionStateEnum.INITIALIZING_MAP) {
            missionState.setCurrentMissionState(MissionStateEnum.SELECT_AREA);
            this.surveyAreaHeightBar.setEnabled(true);
            this.surveyAreaWidthBar.setEnabled(true);
        }
    }

    private void reachedWaypointAtIndex(int waypointIndex) {

        Log.d("MapPresenter", "Waypoint Reached: " + waypointIndex);

        Circle temp = waypointCircleList.get(waypointIndex);
        waypointCircleList.get(waypointIndex).remove();
        waypointCircleList.remove(waypointIndex);

        waypointCircleList.add(waypointIndex, mMap.addCircle((new CircleOptions()
                .center(new LatLng(temp.getCenter().latitude, temp.getCenter().longitude))
                .radius(3)
                .strokeWidth(3)
                .zIndex(3004.5f) // above the polylines, below the drone and user
                .strokeColor(Color.BLACK)
                .fillColor(Color.argb(150, 0, 0, 255))))); // transparent blue circles


        // waypointCircleList.get(waypointIndex).setFillColor(Color.argb(150, 0, 0, 255)); // change marker to blue
        numWaypointsCompleted++; // track mission progress
        percentageCompletion = (int) (100.0d * numWaypointsCompleted / numWaypointsTotal);
        surveyProgressBar.setProgress((int) percentageCompletion);
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    void initSurveyBox() {
        // Any tap on the Map calls this listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // This if statement runs if user taps map first (not Main Button)
                if (haveAnimatedCameraToUserMarker && missionState.getCurrentMissionState() == MissionStateEnum.SELECT_AREA) {
                    //if (!polySurveyAreaPicked) { // Track if an area selector is already showing
                    polySurveyAreaPicked = true;
                    dbl_HeightSeek = (double) surveyAreaHeightBar.getProgress() / dbl_SurveyBoxScaler;
                    dbl_WidthSeek = (double) surveyAreaWidthBar.getProgress() / dbl_SurveyBoxScaler;
                    double lati = point.latitude; // store the latitude of the tap
                    double longi = point.longitude; // store the longitude of the tap
                    areaSelectingMaskMidpoint = (new LatLng(lati, longi)); // save the tap LatLng globally
                    Polygon temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                            .strokeWidth(2)
                            .zIndex(3004.0f)
                            .strokeColor(Color.BLACK)
                            .fillColor(Color.argb(125, 0, 0, 0)) // black that is 50% opaque (see through)
                            .clickable(true));
                    if (surveyPolygon != null) surveyPolygon.remove();
                    surveyPolygon = temp_surveyPolygon;
                    // } else { // User wants to place the rectangle center at another point on the map
                    //    if (surveyPolygon != null) surveyPolygon.remove();
                    //    polySurveyAreaPicked = false;
                    //}
                }
            }

        });

        // User changes the survey area height by sliding the seek bar
        surveyAreaHeightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // Don't let Height become too small..
                if (surveyAreaHeightBar.getProgress() < 20) {
                    surveyAreaHeightBar.setProgress(20);
                    return;
                }
                if (haveAnimatedCameraToUserMarker) {
                    // Get new scaled height
                    dbl_HeightSeek = (double) progress / dbl_SurveyBoxScaler;
                    // redraw
                    if (surveyPolygon != null) surveyPolygon.remove(); // remove old area selector
                    if (polySurveyAreaPicked) { // if an area selector is on the map
                        double lati = areaSelectingMaskMidpoint.latitude;
                        double longi = areaSelectingMaskMidpoint.longitude;
                        Polygon temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                                .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                                .strokeWidth(2)
                                .zIndex(3004.0f)
                                .strokeColor(Color.BLACK)
                                .clickable(true));
                        if (surveyPolygon != null)
                            surveyPolygon.remove(); // remove the old area selector
                        surveyPolygon = temp_surveyPolygon; // save this area but don't fill color
                        Double heightDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(1).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(1).longitude, 0d, 0d);
                        surveyAreaHeightText.setText("Survey Area Height: " + String.valueOf(round(heightDistance, 1)) + " m");

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

                if (surveyAreaWidthBar.getProgress() < 20) {
                    surveyAreaWidthBar.setProgress(20);
                    return;
                }
                if (haveAnimatedCameraToUserMarker) {
                    dbl_WidthSeek = (double) progress / dbl_SurveyBoxScaler;
                    // redraw
                    if (polySurveyAreaPicked) {
                        double lati = areaSelectingMaskMidpoint.latitude;
                        double longi = areaSelectingMaskMidpoint.longitude;
                        Polygon temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                                .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                                .strokeWidth(2)
                                .zIndex(3004.0f)
                                .strokeColor(Color.BLACK)
                                .clickable(true));
                        if (surveyPolygon != null) surveyPolygon.remove();
                        surveyPolygon = temp_surveyPolygon;
                        Double widthDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(3).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(3).longitude, 0d, 0d);
                        surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(round(widthDistance, 1)) + " m");
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

        if (wayPointPolyLineList != null) waypointCircleList.clear();
        if (wayPointPolyLineList != null) wayPointPolyLineList.clear();
        Marker tempAndroidMarker = userMarker;

        mMap.clear();

        userMarkerIcon = getMarkerIconFromDrawable(userDrawable, 40 + (int) (70 * ((int) (mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 40 + (int) (70.0f * ((int) (mMap.getCameraPosition().zoom - 3.00f) / 15.0f)));
        Marker tempUserMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lastKnownUserLocation.getLatitude(), lastKnownUserLocation.getLongitude()))
                .zIndex(3005.0f)
                .icon(userMarkerIcon));
        if (userMarker != null) userMarker.remove();

        userMarker = tempUserMarker;
        droneMarkerIcon = getMarkerIconFromDrawable(droneDrawable, 50 + (int) (130.0f * 1.85f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 50 + (int) (130 * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f))); // aspect ratio = l/w = 1.85
        droneMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lastKnownUserLocation.getLatitude() - 0.0006, lastKnownUserLocation.getLongitude() + 0.001))
                .zIndex(3005.0f)
                .icon(droneMarkerIcon));
        mMap.setOnMapLoadedCallback(null); // don't need anymore since loaded
        //writeToast("GPS Signal Acquired.");
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        polySurveyAreaPicked = true;
        dbl_HeightSeek = (double) surveyAreaHeightBar.getProgress() / dbl_SurveyBoxScaler;
        dbl_WidthSeek = (double) surveyAreaWidthBar.getProgress() / dbl_SurveyBoxScaler;
        double lati = userMarker.getPosition().latitude + 0.0001; // store the latitude of the tap
        double longi = userMarker.getPosition().longitude + 0.001; // store the longitude of the tap
        areaSelectingMaskMidpoint = (new LatLng(lati, longi)); // save the tap LatLng globally
        surveyPolygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                .strokeWidth(2)
                .strokeColor(Color.BLACK)
                .zIndex(3005.0f)
                .fillColor(Color.argb(125, 0, 0, 0)) // black that is 50% opaque (see through)
                .clickable(true));

        // Go the new GPS coordinates of the user
        Double heightDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(1).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(1).longitude, 0d, 0d);
        surveyAreaHeightText.setText("Survey Area Height: " + String.valueOf(round(heightDistance, 1)) + " m");
        Double widthDistance = distance(surveyPolygon.getPoints().get(0).latitude, surveyPolygon.getPoints().get(3).latitude, surveyPolygon.getPoints().get(0).longitude, surveyPolygon.getPoints().get(3).longitude, 0d, 0d);
        surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(round(widthDistance, 1)) + " m");
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(userMarker.getPosition().latitude, userMarker.getPosition().longitude + 0.001), 18.0f);
        mMap.animateCamera(cu, 3000, MapPresenter.this);
        this.surveyAreaHeightBar.setEnabled(true);
        this.surveyAreaWidthBar.setEnabled(true);
        return;
    }

    void writeToast(final String message) {
        Context context = fragmentActivity.getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_LONG;
        Toast toastConsole = Toast.makeText(context, text, duration);
        toastConsole.show();

    }

}
