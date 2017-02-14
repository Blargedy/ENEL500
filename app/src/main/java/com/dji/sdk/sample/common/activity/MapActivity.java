package com.dji.sdk.sample.common.activity;

import android.graphics.Color;
import android.os.Bundle;
import com.dji.sdk.sample.R;
import com.dji.sdk.sample.common.utility.ApplicationContextManager;
import com.dji.sdk.sample.common.utility.UserPermissionRequester;
import com.dji.sdk.sample.common.view.MapView;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polyline;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback
{
    private UserPermissionRequester permissionRequester_;
    private ApplicationContextManager contextManager_;
    private MapView mapView_;
    private GoogleMap mMap;
    private LatLng curlatLng;
    private LatLng curlatLngpt1;
    private LatLng curlatLngpt2;
    private boolean boolpt1picked = false;
    private boolean boolpt2picked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionRequester_ = new UserPermissionRequester();
        contextManager_ = new ApplicationContextManager(this);

        mapView_ = new MapView(this);
        permissionRequester_.requestPermissions(this);
        setContentView(mapView_);

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng calgaryLatLng = new LatLng(51, -114);
        mMap.addMarker(new MarkerOptions().position(calgaryLatLng).title("Marker in Calgary"));
        LatLng sydneyLatLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydneyLatLng).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(calgaryLatLng));



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                //save current location
                // curlatLng = point;
                if (boolpt1picked && boolpt2picked){
                    boolpt1picked = false;
                    boolpt2picked = false;
                    mMap.clear();
                }

                if (!boolpt1picked) {
                    curlatLngpt1 = point;
                    boolpt1picked = true;
                }else if(!boolpt2picked){
                    curlatLngpt2 = point;
                    boolpt2picked = true;
                    // draw the rectangle
                    // north line
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(curlatLngpt1,new LatLng(curlatLngpt2.latitude, curlatLngpt1.longitude))
                            .width(5)
                            .color(Color.RED));
                    // east line
                    line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(curlatLngpt2.latitude, curlatLngpt1.longitude),curlatLngpt2)
                            .width(5)
                            .color(Color.RED));
                    // south line
                    line = mMap.addPolyline(new PolylineOptions()
                            .add(curlatLngpt2,new LatLng(curlatLngpt1.latitude, curlatLngpt2.longitude))
                            .width(5)
                            .color(Color.RED));
                    // west line
                    line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(curlatLngpt1.latitude, curlatLngpt2.longitude),curlatLngpt1)
                            .width(5)
                            .color(Color.RED));

                }
            }

        });

       // final Button button = (Button) findViewById(R.id.btn_begin_survey);
        //button.setOnClickListener(new View.OnClickListener() {
           // public void onClick(View v) {


                // Polyline line = mMap.addPolyline(new PolylineOptions()
                //         .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
                //          .width(5)
                //         .color(Color.RED));
                // Add a marker in Sydney and move the camera
                //LatLng sydney = new LatLng(34, -151);
                //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

         //   }
        //});

    }
}