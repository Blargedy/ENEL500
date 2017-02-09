package com.dji.sdk.sample.common.mission;

import java.util.List;
import java.util.Vector;

import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by eric on 2017-01-23.
 */

public class SwitchBackPathGenerator {
    public static List<Coordinate> generateSwitchback(Coordinate bottomLeft, Coordinate topRight, float altitude){
        List<Coordinate> switchbackPoints= new Vector<Coordinate>();

        DJIAircraft aircraft = (DJIAircraft) DJISDKManager.getInstance().getDJIProduct();
        double curLat = aircraft.getFlightController().getCurrentState().getAircraftLocation().getLatitude();
        double curLon = aircraft.getFlightController().getCurrentState().getAircraftLocation().getLongitude();
        double change = 0.000008;

        switchbackPoints.add(new Coordinate(curLat+change, curLon));
        switchbackPoints.add(new Coordinate(curLat+change*2.0, curLon));
        return switchbackPoints;
    }
}
