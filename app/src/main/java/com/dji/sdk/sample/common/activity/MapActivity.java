package com.dji.sdk.sample.common.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.mission.src.SwitchBackPathGenerator;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.values.Coordinate;
import com.dji.sdk.sample.common.view.MapView;

import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

import static com.dji.sdk.sample.R.id.map;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // Context and view items
    private ApplicationContextManager contextManager_;
    private UserPermissionRequester permissionRequester_;
    private MapView mapView_;

    // Map specific variables
    private GoogleMap mMap;

    // State enumeration
    private enum enum_menuStates {
        MAPNOTREADY, MAPREADY, SELECTAREA, RDYTOBEGIN, INFLIGHT, HOVER
    }

    private enum_menuStates enum_menuStatesVar = enum_menuStates.MAPNOTREADY;

    // User tracking
    private Marker userMarker;

    // Drone Tracking
    private LatLng droneStartingLatLng;
    private Marker droneMarker;

    // Area selector Variables
    private boolean polySurveyAreaPicked = false;
    private Polygon surveyPolygon;
    private LatLng areaSelectingMaskMidpoint;

    //  User Controls such as switch and buttons
    private Button btn_mainButton;
    private Switch sw_hoverNow;
    private SeekBar pbar_surveyAreaHeight;
    private SeekBar pbar_surveyAreaWidth;

    // Controls that tell the user information
    private TextView txt_console;
    private SeekBar pbar_surveyProgressTracking;
    private TextView txt_surveyAreaHeight;
    private TextView txt_surveyAreaWidth;

    // Global area selection constants
    private final double dbl_SurveyBoxScaler = 50.0;
    private final double dbl_InitialSurveyBoxSeekValue = 50.0;
    private final double dbl_areaSS = 0.002;

    // Global area selection starting values
    private double dbl_HeightSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;
    private double dbl_WidthSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;


    // Google map Waypoint List
    private ArrayList<Circle> waypointCircleList;

    // Mission Tracking
    private ArrayList<Circle> droneNotYetVisitedWaypointList;
    private double percentageCompletion = 0.00;
    private int numWaypointsTotal = 0;
    private int numWaypointsCompleted = 0;

    // Google map inter-Waypoint Polyline List
    private ArrayList<Polyline> waypointPolylineList;

    // Waypoint coordinate list
    private List<Coordinate> waypointCoordinateList;

    // Flight simulation (TO BE REMOVED AFTER INTEGRATION)
    private CountDownTimer myTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionRequester_ = new UserPermissionRequester();
        contextManager_ = new ApplicationContextManager(this);

        mapView_ = new MapView(this);
        permissionRequester_.requestPermissions(this);
        setContentView(mapView_);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        // Get control handles:
        btn_mainButton = (Button) findViewById(R.id.btn_mainButton);
        sw_hoverNow = (Switch) findViewById(R.id.sw_hoverNow);
        txt_console = (TextView) findViewById(R.id.txt_console);
        txt_surveyAreaWidth = (TextView) findViewById(R.id.txt_surveyAreaWidth);
        txt_surveyAreaHeight = (TextView) findViewById(R.id.txt_surveyAreaHeight);
        pbar_surveyAreaHeight = (SeekBar) findViewById(R.id.pbar_surveyAreaHeight);
        pbar_surveyAreaWidth = (SeekBar) findViewById(R.id.pbar_surveyAreaWidth);
        pbar_surveyProgressTracking = (SeekBar) findViewById(R.id.pbar_surveyProgressTracking);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initializeEnvironment() {
        btn_mainButton.setVisibility(View.INVISIBLE);
        pbar_surveyAreaHeight.setVisibility(View.INVISIBLE);
        pbar_surveyAreaWidth.setVisibility(View.INVISIBLE);
        pbar_surveyProgressTracking.setVisibility(View.INVISIBLE);
        txt_surveyAreaHeight.setVisibility(View.INVISIBLE);
        txt_surveyAreaWidth.setVisibility(View.INVISIBLE);
        sw_hoverNow.setVisibility(View.INVISIBLE);
        enum_menuStatesVar = enum_menuStates.MAPNOTREADY;
        pbar_surveyAreaHeight.setEnabled(false);
        pbar_surveyAreaWidth.setEnabled(false);
        pbar_surveyAreaHeight.setProgress((int) dbl_InitialSurveyBoxSeekValue);
        pbar_surveyAreaWidth.setProgress((int) dbl_InitialSurveyBoxSeekValue);
        txt_surveyAreaHeight.setText("Survey Area Height: " + String.valueOf(pbar_surveyAreaHeight.getProgress()));
        txt_surveyAreaWidth.setText("Survey Area Width: " + String.valueOf(pbar_surveyAreaWidth.getProgress()));
        sw_hoverNow.setEnabled(false);
        pbar_surveyProgressTracking.setEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        txt_console.setTextSize(20);
        waypointPolylineList = new ArrayList<Polyline>();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initializeEnvironment();
        enum_menuStatesVar = enum_menuStates.MAPREADY;
        txt_console.setText("Console: Ready.");
        // Add a marker in Sydney and move the camera
        final LatLng calgaryLatLng = new LatLng(51.076674, -114.134972); // GPS coordinates of U of C
        LatLng calgaryLatLngUpSlightly = new LatLng((51.076674 + 0.005), -114.134972); // Drone starting
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(calgaryLatLngUpSlightly));
        simulateUserPositionMarker(); // Where the android GPS position starts
        simulateDroneMarker(); // Where the drone GPS position starts
        btn_mainButton.setVisibility(View.VISIBLE);
        // Main Button Listener
        btn_mainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (enum_menuStatesVar) {
                    case MAPREADY: // User pressed 'SELECT SURVEY AREA'
                        enum_menuStatesVar = enum_menuStatesVar.SELECTAREA;
                        txt_console.setText("Console: Tap the map to place area selector.");
                        btn_mainButton.setText("ACCEPT SURVEY AREA");
                        btn_mainButton.setVisibility(View.INVISIBLE);
                        txt_surveyAreaWidth.setVisibility(View.VISIBLE);
                        txt_surveyAreaHeight.setVisibility(View.VISIBLE);
                        pbar_surveyAreaHeight.setVisibility(View.VISIBLE);
                        pbar_surveyAreaWidth.setVisibility(View.VISIBLE);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                        pbar_surveyProgressTracking.setVisibility(View.INVISIBLE);
                        break;
                    case SELECTAREA: // User pressed 'ACCEPT SURVEY AREA'
                        enum_menuStatesVar = enum_menuStatesVar.RDYTOBEGIN;
                        btn_mainButton.setVisibility(View.INVISIBLE);
                        btn_mainButton.setText("START SURVEY");
                        txt_surveyAreaWidth.setVisibility(View.INVISIBLE);
                        txt_surveyAreaHeight.setVisibility(View.INVISIBLE);
                        pbar_surveyAreaHeight.setVisibility(View.INVISIBLE);
                        pbar_surveyAreaWidth.setVisibility(View.INVISIBLE);
                        // Send Area selector coordinates to Mission Generator (top right and bottom left corner)
                        GenerateMissionThenPlotWaypoints(); // Need to integrate the internal code of this function
                        btn_mainButton.setVisibility(View.VISIBLE);
                        break;
                    case RDYTOBEGIN: // Waypoints are on the map, User Pressed 'START SURVEYING' (FLYING)
                        enum_menuStatesVar = enum_menuStatesVar.INFLIGHT;
                        percentageCompletion = (int) 100.0d * numWaypointsCompleted / numWaypointsTotal;
                        txt_console.setText("Console: Progress " + numWaypointsCompleted + "/" + numWaypointsTotal + " (" + percentageCompletion + "%) (Drone In-flight)");
                        btn_mainButton.setText("CANCEL MISSION");
                        sw_hoverNow.setEnabled(true); // allow to hover
                        pbar_surveyProgressTracking.setVisibility(View.VISIBLE);
                        pbar_surveyProgressTracking.setEnabled(true);
                        sw_hoverNow.setVisibility(View.VISIBLE);
                        pbar_surveyProgressTracking.setEnabled(false);
                        droneStartingLatLng = droneMarker.getPosition();
                        // Timer used to animate the drone mission
                        myTimer = new CountDownTimer(999999, 180) { // move drone marker every 180 ms
                            int tickIndex = -1;
                            public LatLng DroneStartingPlace = droneMarker.getPosition();

                            public void onTick(long millisUntilFinished) {
                                tickIndex++;
                                if (tickIndex < droneNotYetVisitedWaypointList.size()) {
                                    droneMarker.setPosition(droneNotYetVisitedWaypointList.get(tickIndex).getCenter());
                                    waypointCircleList.get(tickIndex).setFillColor(Color.argb(150, 0, 0, 255)); // change marker to blue
                                    waypointCircleList.get(tickIndex).setZIndex(0);
                                    numWaypointsCompleted++; // track mission progress
                                    percentageCompletion = (int) 100.0d * numWaypointsCompleted / numWaypointsTotal;
                                    txt_console.setText("Console: Progress " + numWaypointsCompleted + "/" + numWaypointsTotal + " (" + percentageCompletion + "%) (Drone In-flight)");
                                    pbar_surveyProgressTracking.setProgress((int) percentageCompletion);
                                } else { // mission has completed sucessfully
                                    polySurveyAreaPicked = false; // will need to pick a new survey area for next mission
                                    btn_mainButton.setText("Select another Survey Area?");
                                    droneMarker.setPosition(DroneStartingPlace); // make sure drone is back beside operator
                                    enum_menuStatesVar = enum_menuStates.MAPREADY; // back to state before area is selected
                                    txt_console.setText("Console: Mission Successful. Ready.");
                                    pbar_surveyProgressTracking.setEnabled(false);
                                    sw_hoverNow.setEnabled(false);
                                    pbar_surveyAreaHeight.setEnabled(false);
                                    pbar_surveyAreaWidth.setEnabled(false);
                                    numWaypointsTotal = 0; // reset number of waypoints total
                                    numWaypointsCompleted = 0; // reset number of waypoints complete
                                    sw_hoverNow.setVisibility(View.INVISIBLE);
                                    for (int i = 0; i < droneNotYetVisitedWaypointList.size(); i++) {
                                        droneNotYetVisitedWaypointList.get(i).remove();
                                    }

                                    for (int i = 0; i < waypointPolylineList.size(); i++) {
                                        waypointPolylineList.get(i).remove();
                                    }
                                    droneNotYetVisitedWaypointList.clear();
                                    waypointPolylineList.clear();
                                    this.cancel(); // stop this timer from ticking any more
                                }
                            }

                            public void onFinish() {
                            }
                        }.start();

                        break;
                    case INFLIGHT: // Was in Mission but user pressed 'CANCEL MISSION'
                        CancelMission();
                        break;
                    case HOVER: // While hovering user pressed 'CANCEL MISSION'
                        CancelMission();
                        break;
                    default:

                }
            }
        });
        // Any tap on the Map calls this listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // This if statement runs if user taps map first (not Main Button)
                if (enum_menuStatesVar == enum_menuStates.MAPREADY) {
                    enum_menuStatesVar = enum_menuStatesVar.SELECTAREA;
                    txt_console.setText("Console: Tap the map to place area selector.");
                    btn_mainButton.setText("ACCEPT SURVEY AREA");
                    btn_mainButton.setVisibility(View.INVISIBLE);
                    txt_surveyAreaWidth.setVisibility(View.VISIBLE);
                    txt_surveyAreaHeight.setVisibility(View.VISIBLE);
                    pbar_surveyAreaHeight.setVisibility(View.VISIBLE);
                    pbar_surveyAreaWidth.setVisibility(View.VISIBLE);
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.getUiSettings().setAllGesturesEnabled(true);
                    return;
                }
                // if survey rectangle not yet showing, place it down where the user touched
                if (enum_menuStatesVar == enum_menuStates.SELECTAREA) {
                    if (!polySurveyAreaPicked) { // Track if an area selector is already showing
                        polySurveyAreaPicked = true;
                        dbl_HeightSeek = (double) pbar_surveyAreaHeight.getProgress() / dbl_SurveyBoxScaler;
                        dbl_WidthSeek = (double) pbar_surveyAreaWidth.getProgress() / dbl_SurveyBoxScaler;
                        double lati = point.latitude; // store the latitude of the tap
                        double longi = point.longitude; // store the longitude of the tap
                        areaSelectingMaskMidpoint = point; // save the tap LatLng globally
                        surveyPolygon = mMap.addPolygon(new PolygonOptions()
                                .add(new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi - dbl_areaSS * dbl_WidthSeek)), new LatLng((lati + dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)), new LatLng((lati - dbl_areaSS * dbl_HeightSeek), (longi + dbl_areaSS * dbl_WidthSeek)))
                                .strokeWidth(2)
                                .strokeColor(Color.BLACK)
                                .fillColor(Color.argb(125, 0, 0, 0)) // black that is 50% opaque (see through)
                                .clickable(true));
                        pbar_surveyAreaHeight.setEnabled(true);
                        pbar_surveyAreaWidth.setEnabled(true);
                        btn_mainButton.setVisibility(View.VISIBLE);
                        txt_console.setText("Console: Scale the area then press Accept.");
                    } else { // User wants to place the rectangle center at another point on the map
                        surveyPolygon.remove();
                        polySurveyAreaPicked = false;
                        btn_mainButton.setVisibility(View.INVISIBLE);
                        pbar_surveyAreaHeight.setEnabled(false);
                        pbar_surveyAreaWidth.setEnabled(false);
                        txt_console.setText("Console: Tap the map to place area selector.");
                    }
                }
            }

        });
        // User changes the survey area height by sliding the seek bar
        pbar_surveyAreaHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // Don't let Height become too small..
                if (pbar_surveyAreaHeight.getProgress() < 2) {
                    pbar_surveyAreaHeight.setProgress(2);
                    return;
                }
                // Get new scaled height
                dbl_HeightSeek = (double) progress / dbl_SurveyBoxScaler;
                // redraw
                surveyPolygon.remove(); // remove old area selector
                if (polySurveyAreaPicked) { // if an area selector is on the map
                    double lati = areaSelectingMaskMidpoint.latitude;
                    double longi = areaSelectingMaskMidpoint.longitude;
                    txt_surveyAreaHeight.setText("Survey Area Height: " + String.valueOf(pbar_surveyAreaHeight.getProgress()));
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
        pbar_surveyAreaWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (pbar_surveyAreaWidth.getProgress() < 2) {
                    pbar_surveyAreaWidth.setProgress(2);
                    return;
                }
                dbl_WidthSeek = (double) progress / dbl_SurveyBoxScaler;
                // redraw
                if (polySurveyAreaPicked) {
                    double lati = areaSelectingMaskMidpoint.latitude;
                    double longi = areaSelectingMaskMidpoint.longitude;
                    txt_surveyAreaWidth.setText("Survey Area Width: " + String.valueOf(pbar_surveyAreaWidth.getProgress()));
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

        // user taps hover now / resume now switch
        sw_hoverNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // user wants to hover now
                    if (enum_menuStatesVar == enum_menuStates.INFLIGHT) {
                        enum_menuStatesVar = enum_menuStatesVar.HOVER;
                        percentageCompletion = (int) 100.0d * numWaypointsCompleted / numWaypointsTotal;
                        txt_console.setText("Console: Progress " + numWaypointsCompleted + "/" + numWaypointsTotal + " (" + percentageCompletion + "%) (Drone Hovering)");
                        btn_mainButton.setText("CANCEL MISSION");
                        sw_hoverNow.setText("Resume Now");
                        myTimer.cancel(); // stop drone movement
                        return;
                    }
                } else { // not checked user wants to resume now

                    if (enum_menuStatesVar == enum_menuStates.HOVER) { // is resumed from hovering
                        enum_menuStatesVar = enum_menuStatesVar.INFLIGHT;
                        percentageCompletion = (int) 100.0d * numWaypointsCompleted / numWaypointsTotal;
                        txt_console.setText("Console: Progress " + numWaypointsCompleted + "/" + numWaypointsTotal + " (" + percentageCompletion + "%) (Drone In-flight)");
                        btn_mainButton.setText("CANCEL MISSION");
                        sw_hoverNow.setText("Hover Now");
                        myTimer.start(); // start drone movement
                    }
                }

            }

        });
    }

    // This needs to use either the android GPS or DJI remote control coordinates
    void simulateUserPositionMarker() {
        LatLng calgaryLatLng = new LatLng(51.076674, -114.134972);
        userMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(calgaryLatLng.latitude + 0.001, calgaryLatLng.longitude - 0.004))
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    // This needs to use the drone GPS coordinates
    void simulateDroneMarker() {
        LatLng calgaryLatLng = new LatLng(51.076674, -114.134972);
        droneMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(userMarker.getPosition().latitude + 0.0005, userMarker.getPosition().longitude + 0.0009))
                .title("Drone is here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))); // try change to drone icon
    }

    // Send the area selector corners to Mission Generator, get waypoints back and plot
    void GenerateMissionThenPlotWaypoints() {
        Coordinate bottomLeftCorner = new Coordinate();
        bottomLeftCorner.latitude_ = surveyPolygon.getPoints().get(0).latitude;
        bottomLeftCorner.longitude_ = surveyPolygon.getPoints().get(0).longitude;

        Coordinate topRightCorner = new Coordinate();
        topRightCorner.latitude_ = surveyPolygon.getPoints().get(2).latitude;
        topRightCorner.longitude_ = surveyPolygon.getPoints().get(2).longitude;

        // CALL MISSIONGENERATOR TO SEND AREA HERE

        // CALL MISSIONGENERATOR TO GET WAYPOINTS HERE

        SwitchBackPathGenerator mySwitch = new SwitchBackPathGenerator();
        surveyPolygon.remove();

        // Need to call this through Mission Generator.
        waypointCoordinateList = mySwitch.generateSwitchback(bottomLeftCorner, topRightCorner, 500.0f);

        txt_console.setText("Console: " + waypointCoordinateList.size() + " Mission waypoints generated. Ready to begin.");
        waypointPolylineList = new ArrayList<Polyline>();
        // Connect drone to first waypoint
        waypointPolylineList.add(mMap.addPolyline(new PolylineOptions()
                .add(droneMarker.getPosition(), new LatLng(waypointCoordinateList.get(0).latitude_, waypointCoordinateList.get(0).longitude_))
                .width(5)
                .color(Color.BLACK)));
        for (int i = 0; i < waypointCoordinateList.size(); i++) {
            LatLng waypointLatLng1 = new LatLng(waypointCoordinateList.get(i).latitude_, waypointCoordinateList.get(i).longitude_);
            if (i == (waypointCoordinateList.size() - 1)) { // the last waypoint polyline goes back to drone starting pos
                //connect last waypoint to drone
                waypointPolylineList.add(mMap.addPolyline(new PolylineOptions()
                        .add(waypointLatLng1, droneMarker.getPosition())
                        .width(5)
                        .color(Color.BLACK)));
                break;
            }
            LatLng waypointLatLng2 = new LatLng(waypointCoordinateList.get(i + 1).latitude_, waypointCoordinateList.get(i + 1).longitude_);
            waypointPolylineList.add(mMap.addPolyline(new PolylineOptions()
                    .add(waypointLatLng1, waypointLatLng2)
                    .width(5)
                    .color(Color.BLACK)));
        }
        // Make and save the waypoint circles
        waypointCircleList = new ArrayList<Circle>();
        droneNotYetVisitedWaypointList = new ArrayList<Circle>();
        for (int j = 0; j < waypointCoordinateList.size(); j++) {
            waypointCircleList.add(mMap.addCircle(new CircleOptions()
                    .center(new LatLng(waypointCoordinateList.get(j).latitude_, waypointCoordinateList.get(j).longitude_))
                    .radius(12)
                    .strokeWidth(1)
                    .strokeColor(Color.BLACK)
                    .fillColor(Color.argb(150, 255, 0, 0)))); // transparent red circles
            droneNotYetVisitedWaypointList.add(waypointCircleList.get(j));
        }
        numWaypointsTotal = waypointCoordinateList.size();
    }


    void CancelMission() {
        enum_menuStatesVar = enum_menuStatesVar.MAPREADY;
        txt_console.setText("Console: Cancelled. Drone returning home.");
        btn_mainButton.setText("Select another Survey Area?");
        btn_mainButton.setVisibility(View.VISIBLE);
        sw_hoverNow.setEnabled(false);
        sw_hoverNow.setVisibility(View.INVISIBLE);
        pbar_surveyProgressTracking.setVisibility(View.INVISIBLE);
        pbar_surveyProgressTracking.setEnabled(false);
        pbar_surveyAreaHeight.setEnabled(false);
        pbar_surveyAreaWidth.setEnabled(false);
        numWaypointsTotal = 0;
        numWaypointsCompleted = 0;
        polySurveyAreaPicked = false;
        myTimer.cancel();
        droneMarker.setPosition(droneStartingLatLng);

        for (int i = 0; i < droneNotYetVisitedWaypointList.size(); i++) {
            droneNotYetVisitedWaypointList.get(i).remove();
        }

        for (int i = 0; i < waypointPolylineList.size(); i++) {
            waypointPolylineList.get(i).remove();
        }
        droneNotYetVisitedWaypointList.clear();
        waypointPolylineList.clear();

        sw_hoverNow.setChecked(false);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}