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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.entity.DroneLocationEntity;
import com.dji.sdk.sample.common.values.MissionBoundary;
import com.dji.sdk.sample.common.presenter.api.I_MapPresenter;
import com.dji.sdk.sample.common.utility.BroadcastIntentNames;
import com.dji.sdk.sample.common.utility.CachedMapLoaderThread;
import com.dji.sdk.sample.common.utility.CachedMapNotifyingThread;
import com.dji.sdk.sample.common.utility.I_CachedMapThreadCompletedListener;
import com.dji.sdk.sample.common.values.Coordinate;
import com.dji.sdk.sample.common.view.api.I_MapView;
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
import java.util.ArrayList;
import java.util.Vector;

import static com.dji.sdk.sample.R.id.map;
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
    private final double dbl_areaSS = 0.002;
    // Area Selector dimension variables
    private double dbl_HeightSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;
    private double dbl_WidthSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;
    // Controls
    private SeekBar surveyProgressBar;
    private TextView surveyAreaHeightText;
    private SeekBar surveyAreaHeightBar;
    private TextView surveyAreaWidthText;
    private SeekBar surveyAreaWidthBar;
    // Lists
    private ArrayList<Circle> waypointCircleList;
    private ArrayList<Polyline> wayPointPolyLineList;
    // Progress Tracking
    private double percentageCompletion = 0.00;
    private int numWaypointsTotal = 0;
    private int numWaypointsCompleted = 0;
    // GPS Drone
    private boolean droneHasNotUsedRealGPSYet = true;
    private BroadcastReceiver droneLocationChangedReceiver_;
    private DroneLocationEntity droneLocation_;
    // GPS User
    private LocationListener userLocationListener;
    private LocationManager userLocationManager;
    private FragmentActivity fragmentActivity;
    private boolean haveAnimatedCameraToUserMarker = false;
    private boolean introZoomFinished = false;
    // Cached Map
    private CachedMapNotifyingThread cachedMapLoadNotifier;
    private CachedMapLoaderThread cachedMapLoaderThread;
    private TileOverlay offlineOverlay;
    private TileProvider offLineTileProvider;
    private boolean usingCachedMap = false;
    private boolean needToTellUserCachedMapAvailable = true;

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
                    .strokeWidth(1)
                    .zIndex(3004.5f) // above the polylines, below the drone and user
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.argb(150, 255, 0, 0)))); // transparent red circles
        }
        numWaypointsTotal = waypoints.size(); // do not use polyline size (1 extra)
    }

    @Override
    public void clearMap() {
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

    void writeToast(final String message) {
        Context context = fragmentActivity.getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_LONG;
        Toast toastConsole = Toast.makeText(context, text, duration);
        toastConsole.show();
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Implement map interactions
        LatLng calgaryLatLng = new LatLng(51.0486, -114.0708);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calgaryLatLng, 10.0f));
        mMap = googleMap;
        surveyProgressBar.setEnabled(false);
        // For now place drone and 'android' markers manually
        surveyAreaHeightBar.setProgress(50);
        surveyAreaWidthBar.setProgress(50);
        surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(surveyAreaWidthBar.getProgress()));
        surveyAreaHeightText.setText("Survey Area Height: " + String.valueOf(surveyAreaHeightBar.getProgress()));
        mMap.setMinZoomPreference(0.00f);
        mMap.setMaxZoomPreference(17.0f);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
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
                    writeToast("Android GPS Service Started (Permissions Granted)");
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
                    writeToast("No GPS Service available.");
                    new CountDownTimer(Long.MAX_VALUE, 6000) {
                        public void onTick(long millisUntilFinished) {
                            if (!(Build.VERSION.SDK_INT >= 23 &&
                                    ContextCompat.checkSelfPermission(fragmentActivity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(fragmentActivity.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                                userLocationManager = (LocationManager) fragmentActivity.getSystemService(Context.LOCATION_SERVICE);
                                userLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, userLocationListener);
                                Log.d("MapPresenter", "Android GPS Service Started (Permissions Granted)");
                                writeToast("Android GPS Service Started (Permissions Granted)");
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

                            }else{
                                writeToast("No GPS Service available.");
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
                        // Called when a new location is found by the network location provider.

                        if (location == null && !introZoomFinished) {
                            // no GPS position do nothing - cancel.
                            return;
                        }
                        if (userMarker != null) {
                            userMarker.remove();
                        }

                        //Log.d("MapPresenter", "Camera Zoom: " + mMap.getCameraPosition().zoom);

                        Drawable userDrawable = fragmentActivity.getResources().getDrawable(R.drawable.android_marker);
                        BitmapDescriptor markerIcon;
                        markerIcon = getMarkerIconFromDrawable(userDrawable, 30 + (int) (60.0f * ((int)(mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 30 + (int) (60.0f * ((int)(mMap.getCameraPosition().zoom - 3.00f) / 15.0f)));
                        userMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .title("You are here.")
                                .zIndex(3005.0f)
                                .icon(markerIcon));
                        // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // green marker
                        // Log.d("MapPresenter", "User Location: latitude=" + location.getLatitude() + " longitude=" + location.getLongitude());

                        if (droneMarker != null) {
                            return; // only draw drone next to user once. Then real drone GPS coordinates are used.
                        }
                        Drawable droneDrawable = fragmentActivity.getResources().getDrawable(R.drawable.drone_marker);
                        markerIcon = getMarkerIconFromDrawable(droneDrawable, 30 + (int) (110.0f * 1.85f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 30 + (int) (110.0f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f))); // aspect ratio = l/w = 1.85

                        droneMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude() + 0.004, location.getLongitude() + 0.004))
                                .title("Drone")
                                .zIndex(3005.0f)
                                .icon(markerIcon));

                        // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); // to change to actual phantom 4 icon
                        // Log.d("MapPresenter", "droneLocation: latitude=" + location.latitude_ + " longitude=" + location.longitude_);

                        if (!haveAnimatedCameraToUserMarker) {
                            haveAnimatedCameraToUserMarker = true;
                            mMap.setOnMapLoadedCallback(null); // don't need anymore since loaded
                            writeToast("GPS Signal Acquired.");
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            mMap.getUiSettings().setAllGesturesEnabled(false);
                            mMap.getUiSettings().setScrollGesturesEnabled(false);
                            mMap.getUiSettings().setZoomControlsEnabled(false);
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            polySurveyAreaPicked = true;
                            dbl_HeightSeek = (double) surveyAreaHeightBar.getProgress() / dbl_SurveyBoxScaler;
                            dbl_WidthSeek = (double) surveyAreaWidthBar.getProgress() / dbl_SurveyBoxScaler;
                            double lati = userMarker.getPosition().latitude + 0.002; // store the latitude of the tap
                            double longi = userMarker.getPosition().longitude + 0.004; // store the longitude of the tap
                            areaSelectingMaskMidpoint = (new LatLng(lati, longi)); // save the tap LatLng globally
                            surveyPolygon = mMap.addPolygon(new PolygonOptions()
                                    .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                                    .strokeWidth(2)
                                    .strokeColor(Color.BLACK)
                                    .zIndex(3005.0f)
                                    .fillColor(Color.argb(125, 0, 0, 0)) // black that is 50% opaque (see through)
                                    .clickable(true));
                            // Go the new GPS coordinates of the user
                            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(userMarker.getPosition(), 14.0f);
                            mMap.animateCamera(cu, 7000, MapPresenter.this);
                        }
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };


        // check if there is no internet, is so - switch to cached map
        new CountDownTimer(Long.MAX_VALUE, 10000) {
            public void onTick(long millisUntilFinished) {
                if (offLineTileProvider == null) {
                    return;
                }
                if (needToTellUserCachedMapAvailable){
                    needToTellUserCachedMapAvailable = false;
                    writeToast("Cached Map Files Loaded Successfully. Offline map available.");
                }

                if (!isNetworkAvailable() && !usingCachedMap) {
                    usingCachedMap = true;
                    Log.d("MapPresenter", "Detected no internet connection. Therefore, since the Cached Map data is ready - switching to Cached Map mode.");
                    offlineOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(offLineTileProvider).zIndex(0).transparency(0.0f));
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                    writeToast("No Internet Connection. Switching to Cached Map mode.");
                }

                if (isNetworkAvailable() && usingCachedMap) {
                    usingCachedMap = false;
                    Log.d("MapPresenter", "Internet Connection Found. Switching back to Google Hybrid Map");
                    offlineOverlay.remove();
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    writeToast("Internet Connection Found. Switching back to Google Hybrid Map");
                }
            }

            public void onFinish() {

            }

        }.start();

    }


    // Google map animation callback

    public void onFinish() {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        introZoomFinished = true;
    }

    // Google map animation callback
    public void onCancel() {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        introZoomFinished = true;
    }

    private void reachedWaypointAtIndex(int waypointIndex) {
        Log.d("MapPresenter", "Waypoint Reached: " + waypointIndex);
        waypointCircleList.get(waypointIndex).setFillColor(Color.argb(150, 0, 0, 255)); // change marker to blue
        numWaypointsCompleted++; // track mission progress
        percentageCompletion = (int) (100.0d * numWaypointsCompleted / numWaypointsTotal);
        surveyProgressBar.setProgress((int) percentageCompletion);
    }

    private void updateDroneLocation() {
        if (droneLocation_.droneLocation() == null || !introZoomFinished) {
            // no GPS position do nothing - cancel.
            return;
        }
        if (droneMarker != null) {
            droneMarker.remove();
            if (droneHasNotUsedRealGPSYet) {
                writeToast("Drone GPS OK.");
                droneHasNotUsedRealGPSYet = false;
            }

        }
        Drawable droneDrawable = fragmentActivity.getResources().getDrawable(R.drawable.drone_marker);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(droneDrawable, 30 + (int) (110.0f * 1.85f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f)), 30 + (int) (110.0f * ((mMap.getCameraPosition().zoom - 3.00f) / 15.0f))); // aspect ratio = l/w = 1.85

        Coordinate location = droneLocation_.droneLocation();
        droneMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude_, location.longitude_))
                .title("Drone")
                .zIndex(3005.0f)
                .icon(markerIcon));
        // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); // to change to actual phantom 4 icon
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
        File binFilestF = new File(binFilestr);
        if (csvFileF.exists() && binFilestF.exists()) {
            Log.d("MapPresenter", "Cached Map Files found. Attempting to load Map Tiles.");
            writeToast("Cached Map Files found. Attempting to load Map Tiles.");
            cachedMapLoaderThread = new CachedMapLoaderThread();
            cachedMapLoaderThread.setFragmentActivity(fragmentActivity);
            cachedMapLoadNotifier = cachedMapLoaderThread;
            cachedMapLoadNotifier.addListener(this);
            cachedMapLoadNotifier.start(); // starts another thread
        } else {
            writeToast("Cached Map files not find in /root/. Offline maps disabled.");
            Log.e("MapPresenter", "Parsing Cached Map Data Failed! Make sure the files mapcachebinaryData.bin and mapcachebinaryIndex.csv are both in the /root/ directory.");
            return;
        }

    }

    void initSurveyBox() {
        // Any tap on the Map calls this listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // This if statement runs if user taps map first (not Main Button)
                if (haveAnimatedCameraToUserMarker) {
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
                                .zIndex(3004.0f)
                                .strokeColor(Color.BLACK)
                                .fillColor(Color.argb(125, 0, 0, 0)) // black that is 50% opaque (see through)
                                .clickable(true));
                    } else { // User wants to place the rectangle center at another point on the map
                        surveyPolygon.remove();
                        polySurveyAreaPicked = false;
                    }
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
                if (haveAnimatedCameraToUserMarker) {
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
                                .zIndex(3004.0f)
                                .strokeColor(Color.BLACK)
                                .clickable(true));
                        surveyPolygon.remove(); // remove the old area selector
                        surveyPolygon = temp_surveyPolygon; // save this area but don't fill color
                    }
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
        surveyAreaWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                if (surveyAreaWidthBar.getProgress() < 2) {
                    surveyAreaWidthBar.setProgress(2);
                    return;
                }
                if (haveAnimatedCameraToUserMarker) {
                    dbl_WidthSeek = (double) progress / dbl_SurveyBoxScaler;
                    // redraw
                    if (polySurveyAreaPicked) {
                        double lati = areaSelectingMaskMidpoint.latitude;
                        double longi = areaSelectingMaskMidpoint.longitude;
                        surveyAreaWidthText.setText("Survey Area Width: " + String.valueOf(surveyAreaWidthBar.getProgress()));
                        Polygon temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                                .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                                .strokeWidth(2)
                                .zIndex(3004.0f)
                                .strokeColor(Color.BLACK)
                                .clickable(true));
                        surveyPolygon.remove();
                        surveyPolygon = temp_surveyPolygon;
                    }
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
    }

}
