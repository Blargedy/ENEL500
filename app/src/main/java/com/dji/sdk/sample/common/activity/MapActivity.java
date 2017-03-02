package com.dji.sdk.sample.common.activity;
// Format code shortcut: ctrl+windows+alt+'L'

import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.MapView;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.editable;
import static android.R.attr.visible;
import static com.dji.sdk.sample.R.id.map;
import static com.dji.sdk.sample.common.activity.MapActivity.enum_menuStates.MAPNOTREADY;
import static java.lang.Math.abs;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;
    private MapView mapView_;


    public enum enum_menuStates {
        MAPNOTREADY, MAPREADY, SELECTAREA, RDYTOBEGIN, INFLIGHT, ABORTCANCEL, HOVER, RETURNHOME
    }


    // Map specific variables
    private GoogleMap mMap;
    private boolean polySurveyAreaPicked = false;
    private Polygon surveyPolygon;

    // Controls such as buttons and sliders
    private Button btn_mainButton;
    private enum_menuStates enum_menuStatesVar = MAPNOTREADY;
    private Switch sw_hoverNow;
    private SeekBar pbar_surveyProgressTracking;


    private SeekBar pbar_surveyAreaHeight;
    private SeekBar pbar_surveyAreaWidth;
    private TextView txt_console;

    private TextView txt_surveyAreaHeight;
    private TextView txt_surveyAreaWidth;

    private final double dbl_SurveyBoxScaler = 50.0;
    private double dbl_InitialSurveyBoxSeekValue = 50.0;
    private double dbl_HeightSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;
    private double dbl_WidthSeek = dbl_InitialSurveyBoxSeekValue / dbl_SurveyBoxScaler;

    private Marker userMarker;
    private Marker droneMarker;

    private ArrayList<Marker> waypointMarkerList;
    private ArrayList<Polyline> waypointPolylineList;

    LatLng currentAreaMidPoint;

    private String wayPointsTotal = "4";
    private String wayPointsCompleted = "0";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



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


        btn_mainButton.setVisibility(View.INVISIBLE);

        pbar_surveyAreaHeight.setVisibility(View.INVISIBLE);
        pbar_surveyAreaWidth.setVisibility(View.INVISIBLE);
        pbar_surveyProgressTracking.setVisibility(View.INVISIBLE);
        txt_surveyAreaHeight.setVisibility(View.INVISIBLE);
        txt_surveyAreaWidth.setVisibility(View.INVISIBLE);
        sw_hoverNow.setVisibility(View.INVISIBLE);


        enum_menuStatesVar = enum_menuStates.MAPNOTREADY;
        txt_console.setTextSize(20);
        pbar_surveyAreaHeight.setEnabled(false);
        pbar_surveyAreaWidth.setEnabled(false);

        pbar_surveyAreaHeight.setProgress((int) dbl_InitialSurveyBoxSeekValue);
        pbar_surveyAreaWidth.setProgress((int) dbl_InitialSurveyBoxSeekValue);
        txt_surveyAreaHeight.setText("Survey Area Height: " + String.valueOf(pbar_surveyAreaHeight.getProgress()));
        txt_surveyAreaWidth.setText("Survey Area Width: " + String.valueOf(pbar_surveyAreaWidth.getProgress()));
        //btn_mainButton.setHeight(500);
        sw_hoverNow.setEnabled(false);
        waypointMarkerList = new ArrayList<Marker>();
        waypointPolylineList = new ArrayList<Polyline>();
        pbar_surveyProgressTracking.setEnabled(false);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enum_menuStatesVar = enum_menuStates.MAPREADY;
        txt_console.setText("Console: Ready.");
        // Add a marker in Sydney and move the camera
        //LatLng calgaryLatLng = new LatLng(51, -114);
        LatLng calgaryLatLng = new LatLng(51.076674, -114.134972);
        LatLng calgaryLatLngUpSlightly = new LatLng((51.076674+0.005), -114.134972);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(calgaryLatLngUpSlightly));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        //mMap.getUiSettings().setZoomGesturesEnabled(true);
        simulateUserPositionMarker(); // Where the android GPS position is
        simulateDroneMarker(); // Where the drone GPS postition is
        // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
        //         userMarker.getPosition(),mMap.getCameraPosition().zoom));
        btn_mainButton.setVisibility(View.VISIBLE);

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
                        break;
                    case SELECTAREA: // User pressed 'ACCEPT SURVEY AREA'
                        enum_menuStatesVar = enum_menuStatesVar.RDYTOBEGIN;
                        btn_mainButton.setVisibility(View.INVISIBLE);
                        txt_console.setText("Console: Mission waypoints generated. Ready to begin.");
                        btn_mainButton.setText("START SURVEY");
                        txt_surveyAreaWidth.setVisibility(View.INVISIBLE);
                        txt_surveyAreaHeight.setVisibility(View.INVISIBLE);
                        pbar_surveyAreaHeight.setVisibility(View.INVISIBLE);
                        pbar_surveyAreaWidth.setVisibility(View.INVISIBLE);
                        simulateWaypoints();

                        // Button remains invisible until the waypoints are plotted
                        break;
                    case RDYTOBEGIN: // Waypoints are on the map, User Pressed 'START SURVEYING' (FLYING)
                        enum_menuStatesVar = enum_menuStatesVar.INFLIGHT;
                        txt_console.setText("Console: Waypoint progress " + wayPointsCompleted + "/" +   wayPointsTotal +  " (0%) (Drone In-flight)");
                        btn_mainButton.setText("CANCEL MISSION");
                        sw_hoverNow.setEnabled(true); // allow to hover
                        pbar_surveyProgressTracking.setVisibility(View.VISIBLE);
                        sw_hoverNow.setVisibility(View.VISIBLE);

                        //   (new Thread() {
                        //     public void run() {
                        //   double Cx = (droneMarker.getPosition().latitude + waypointMarkerList.get(0).getPosition().latitude) / 2;
                        // double Cy = (droneMarker.getPosition().longitude + waypointMarkerList.get(0).getPosition().longitude) / 2;
                        //droneMarker.setPosition(new LatLng(Cx,Cy));

                        //wayPointsCompleted = "1";

// add delay

//        Cx = (droneMarker.getPosition().latitude + waypointMarkerList.get(0).getPosition().latitude) / 2;
//        Cy = (droneMarker.getPosition().longitude + waypointMarkerList.get(0).getPosition().longitude) / 2;
//        droneMarker.setPosition(new LatLng(waypointMarkerList.get(0).getPosition().latitude,waypointMarkerList.get(0).getPosition().longitude));
//
//        wayPointsCompleted = "1";
//
//        Cx = (droneMarker.getPosition().latitude + waypointMarkerList.get(1).getPosition().latitude) / 2;
//        Cy = (droneMarker.getPosition().longitude + waypointMarkerList.get(1).getPosition().longitude) / 2;
//        droneMarker.setPosition(new LatLng(Cx,Cy));
//
//        wayPointsCompleted = "1";
                        //}
                        //  }).start();

                        // Simulates drone flying background
                        // Timer myTimer = new Timer(doAsynchronousTask);


                        break;
                    case INFLIGHT: // Was in Mission but user pressed 'CANCEL MISSION'
                        enum_menuStatesVar = enum_menuStatesVar.ABORTCANCEL;
                        txt_console.setText("Console: Waypoint progress " + wayPointsCompleted + "/" +  wayPointsTotal + " (0%) (Cancelled, Returning home)");
                        btn_mainButton.setVisibility(View.INVISIBLE);
                        sw_hoverNow.setEnabled(false); // no more hover
                        sw_hoverNow.setVisibility(View.INVISIBLE);
                        pbar_surveyProgressTracking.setVisibility(View.INVISIBLE);
                        waypointMarkerList.get(0).remove();
                        waypointMarkerList.get(1).remove();
                        waypointMarkerList.get(2).remove();
                        waypointMarkerList.get(3).remove();
                        waypointPolylineList.get(0).remove();
                        waypointPolylineList.get(1).remove();
                        waypointPolylineList.get(2).remove();
                        waypointPolylineList.get(3).remove();
                        waypointPolylineList.get(4).remove();

                        break;
                    case HOVER: //while hovering user presses cancel mission
                        enum_menuStatesVar = enum_menuStatesVar.ABORTCANCEL;
                        txt_console.setText("Console: Waypoint progress " + wayPointsCompleted + "/" +  wayPointsTotal + " (0%) (Cancelled, Returning home)");
                        btn_mainButton.setVisibility(View.INVISIBLE);
                        sw_hoverNow.setEnabled(false); // no more hover
                        sw_hoverNow.setVisibility(View.INVISIBLE);
                        pbar_surveyProgressTracking.setVisibility(View.INVISIBLE);
                        waypointMarkerList.get(0).remove();
                        waypointMarkerList.get(1).remove();
                        waypointMarkerList.get(2).remove();
                        waypointMarkerList.get(3).remove();
                        waypointPolylineList.get(0).remove();
                        waypointPolylineList.get(1).remove();
                        waypointPolylineList.get(2).remove();
                        waypointPolylineList.get(3).remove();
                        waypointPolylineList.get(4).remove();

                        //   case ABORTCANCEL:
                        //     break;
                        //  case RETURNHOME:
                    default:

                }


            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

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
                // if survey rectangle not yet showing
                // place it down where the user touched
                if (enum_menuStatesVar == enum_menuStates.SELECTAREA) {
                    if (!polySurveyAreaPicked) {
                        dbl_HeightSeek = (double) pbar_surveyAreaHeight.getProgress() / dbl_SurveyBoxScaler;
                        dbl_WidthSeek = (double) pbar_surveyAreaWidth.getProgress() / dbl_SurveyBoxScaler;
                        polySurveyAreaPicked = true;
                        double lati = point.latitude;
                        double longi = point.longitude;
                        currentAreaMidPoint = point;
                        double startingSize = 0.002;
                        surveyPolygon = mMap.addPolygon(new PolygonOptions()
                                .add(new LatLng((lati - startingSize * dbl_HeightSeek), (longi - startingSize * dbl_WidthSeek)), new LatLng((lati + startingSize * dbl_HeightSeek), (longi - startingSize * dbl_WidthSeek)), new LatLng((lati + startingSize * dbl_HeightSeek), (longi + startingSize * dbl_WidthSeek)), new LatLng((lati - startingSize * dbl_HeightSeek), (longi + startingSize * dbl_WidthSeek)))
                                //.add(new LatLng(53, -114), new LatLng(54, -114), new LatLng(54, -113), new LatLng(53, -113))
                                .strokeWidth(2)
                                .strokeColor(Color.BLACK)
                                .fillColor(Color.argb(125, 0, 0, 0))
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

        pbar_surveyAreaHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (pbar_surveyAreaHeight.getProgress() < 2) {
                    pbar_surveyAreaHeight.setProgress(2);
                    return;
                }
                dbl_HeightSeek = (double) progress / dbl_SurveyBoxScaler;
                // redraw
                surveyPolygon.remove();
                if (polySurveyAreaPicked) {
                    double lati = currentAreaMidPoint.latitude;
                    double longi = currentAreaMidPoint.longitude;
                    double startingSize = 0.002;
                    //txt_console.setText(String.valueOf(lati - startingSize*dbl_HeightSeek) +" " + String.valueOf((longi - startingSize*dbl_WidthSeek)) + " " + String.valueOf(dbl_WidthSeek) + " " + String.valueOf(dbl_HeightSeek) );
                    txt_surveyAreaHeight.setText("Survey Area Height: " + String.valueOf(pbar_surveyAreaHeight.getProgress()));
                    Polygon temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng((lati - startingSize * dbl_HeightSeek), (longi - startingSize * dbl_WidthSeek)), new LatLng((lati + startingSize * dbl_HeightSeek), (longi - startingSize * dbl_WidthSeek)), new LatLng((lati + startingSize * dbl_HeightSeek), (longi + startingSize * dbl_WidthSeek)), new LatLng((lati - startingSize * dbl_HeightSeek), (longi + startingSize * dbl_WidthSeek)))
                            //.add(new LatLng(53, -114), new LatLng(54, -114), new LatLng(54, -113), new LatLng(53, -113))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            //.fillColor(Color.argb(125, 0, 0, 0))
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


        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon surveyPolygon) {
            }

        });


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
                    double lati = currentAreaMidPoint.latitude;
                    double longi = currentAreaMidPoint.longitude;
                    double startingSize = 0.002;
                    // txt_console.setText(String.valueOf(lati - startingSize*dbl_HeightSeek) +" " + String.valueOf((longi - startingSize*dbl_WidthSeek)) + " " + String.valueOf(dbl_WidthSeek) + " " + String.valueOf(dbl_HeightSeek) );
                    txt_surveyAreaWidth.setText("Survey Area Width: " + String.valueOf(pbar_surveyAreaWidth.getProgress()));
                    Polygon temp_surveyPolygon = mMap.addPolygon(new PolygonOptions()
                            .add(new LatLng((lati - startingSize * dbl_HeightSeek), (longi - startingSize * dbl_WidthSeek)), new LatLng((lati + startingSize * dbl_HeightSeek), (longi - startingSize * dbl_WidthSeek)), new LatLng((lati + startingSize * dbl_HeightSeek), (longi + startingSize * dbl_WidthSeek)), new LatLng((lati - startingSize * dbl_HeightSeek), (longi + startingSize * dbl_WidthSeek)))
                            //.add(new LatLng(53, -114), new LatLng(54, -114), new LatLng(54, -113), new LatLng(53, -113))
                            .strokeWidth(2)
                            .strokeColor(Color.BLACK)
                            // .fillColor(Color.argb(125, 0, 0, 0))
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


        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon surveyPolygon) {
            }

        });


//attach a listener to check for changes in state
        sw_hoverNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) { // user wants to hover now
                    if (enum_menuStatesVar == enum_menuStates.INFLIGHT) {
                        enum_menuStatesVar = enum_menuStatesVar.HOVER;
                        txt_console.setText("Console: Waypoint progress " + wayPointsCompleted + "/" +  wayPointsTotal+ " (0%) (Drone Hovering)");
                        btn_mainButton.setText("CANCEL MISSION");
                        sw_hoverNow.setText("Resume Now");
                        return;
                    }

                } else { // not checked so user wants to resume now

                    if (enum_menuStatesVar == enum_menuStates.HOVER) { // is resumed from hovering
                        enum_menuStatesVar = enum_menuStatesVar.INFLIGHT;
                        txt_console.setText("Console: Waypoint progress " + wayPointsCompleted + "/" +  wayPointsTotal+ " (0%) (Drone In-flight (Resumed))");
                        btn_mainButton.setText("CANCEL MISSION");
                        sw_hoverNow.setText("Hover Now");
                    }
                }

            }

        });
    }

    void simulateUserPositionMarker() {
        LatLng calgaryLatLng = new LatLng(51.076674, -114.134972);
        userMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(calgaryLatLng.latitude + 0.001, calgaryLatLng.longitude - 0.004))
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    void simulateDroneMarker() {
        LatLng calgaryLatLng = new LatLng(51.076674, -114.134972);
        droneMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(userMarker.getPosition().latitude + 0.0005, userMarker.getPosition().longitude + 0.0009))
                .title("Drone is here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    void simulateWaypoints() {

        for (int i = 0; i < 4; i++) {
            waypointMarkerList.add(mMap.addMarker(new MarkerOptions()
                    .position(surveyPolygon.getPoints().get(i))
                    .title("Drone is here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))));
        }

        waypointPolylineList.add(mMap.addPolyline(new PolylineOptions()
                .add(droneMarker.getPosition(), surveyPolygon.getPoints().get(0))
                .width(5)
                .color(Color.BLACK)));

        waypointPolylineList.add(mMap.addPolyline(new PolylineOptions()
                .add(surveyPolygon.getPoints().get(0), surveyPolygon.getPoints().get(1))
                .width(5)
                .color(Color.BLACK)));


        waypointPolylineList.add(mMap.addPolyline(new PolylineOptions()
                .add(surveyPolygon.getPoints().get(1), surveyPolygon.getPoints().get(2))
                .width(5)
                .color(Color.BLACK)));

        waypointPolylineList.add(mMap.addPolyline(new PolylineOptions()
                .add(surveyPolygon.getPoints().get(2), surveyPolygon.getPoints().get(3))
                .width(5)
                .color(Color.BLACK)));

        waypointPolylineList.add(mMap.addPolyline(new PolylineOptions()
                .add(surveyPolygon.getPoints().get(3), droneMarker.getPosition())
                .width(5)
                .color(Color.BLACK)));


        btn_mainButton.setVisibility(View.VISIBLE);

        surveyPolygon.remove();

    }
//remove
//    TimerTask doAsynchronousTask = new TimerTask() {
//        @Override
//        public void run() {
//
//            //Perform background work here
//
//            handler.post(new Runnable() {
//                public void run() {
//                    //Perform GUI updation work here
//                    //Toast work also
//                }
//            });
//        }
//    };



    void simulateDroneFlyingMission(){
//        double Cx = (droneMarker.getPosition().latitude + waypointMarkerList.get(0).getPosition().latitude) / 2;
//        double Cy = (droneMarker.getPosition().longitude + waypointMarkerList.get(0).getPosition().longitude) / 2;
//        droneMarker.setPosition(new LatLng(Cx,Cy));
//
//        wayPointsCompleted = "1";
//
//// add delay
//
//        Cx = (droneMarker.getPosition().latitude + waypointMarkerList.get(0).getPosition().latitude) / 2;
//        Cy = (droneMarker.getPosition().longitude + waypointMarkerList.get(0).getPosition().longitude) / 2;
//        droneMarker.setPosition(new LatLng(waypointMarkerList.get(0).getPosition().latitude,waypointMarkerList.get(0).getPosition().longitude));
//
//        wayPointsCompleted = "1";
//
//        Cx = (droneMarker.getPosition().latitude + waypointMarkerList.get(1).getPosition().latitude) / 2;
//        Cy = (droneMarker.getPosition().longitude + waypointMarkerList.get(1).getPosition().longitude) / 2;
//        droneMarker.setPosition(new LatLng(Cx,Cy));
//
//        wayPointsCompleted = "1";

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