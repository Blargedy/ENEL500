package com.dji.sdk.sample.common.mission;

import com.dji.sdk.sample.common.values.Coordinate;

import java.util.List;
import java.util.Vector;

import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

import java.lang.Math;

/**
 * Created by eric on 2017-01-23.
 * Edited by Bill Skinner on 2017-02-15
 */

public class SwitchBackPathGenerator {
    /**
     * Percentage of overlap, used to determine spacing between pictures
     */
    private float percentOverlap_ = 80;

    public static List<Coordinate> generateSwitchback(Coordinate bottomLeft, Coordinate topRight, float altitude){
        List<Coordinate> switchbackPoints= new Vector<Coordinate>();

        DJIAircraft aircraft = (DJIAircraft) DJISDKManager.getInstance().getDJIProduct();
        double curLat = aircraft.getFlightController().getCurrentState().getAircraftLocation().getLatitude();
        double curLon = aircraft.getFlightController().getCurrentState().getAircraftLocation().getLongitude();
        double change = 0.000008;

        switchbackPoints.add(new Coordinate(curLat+change, curLon));
        switchbackPoints.add(new Coordinate(curLat+change*2.0, curLon));

        //generatePathWaypoints(switchbackPoints, new Coordinate(51.081805, -114.161351),new Coordinate(51.082000, -114.160979), change);


        return switchbackPoints;
    }

    private static void generatePathWaypoints(List<Coordinate> switchbackPoints, Coordinate bottomLeft, Coordinate topRight, double spacing){
        double margin = spacing/2;
        double nx = Math.ceil((topRight.longitude_ - bottomLeft.longitude_)/spacing);
        double n = 2*nx;

        Vector<Double> xrow = new Vector();

//        for(double i = bottomLeft.longitude_; i <= topRight.longitude_; i = i + spacing){
//            xrow.add(i);
//        }

        for(int i = 0; i < nx; i++){
            xrow.add(bottomLeft.longitude_ + i * spacing);
        }

        double ymin = bottomLeft.latitude_ + margin;
        double ymax = topRight.latitude_ - margin;

        double x;
        double y;

        for(int i = 0; i < n; i++) {
            int m = i % 4;

            switch (m) {
                case 1:
                    x = xrow.elementAt(2 * (int) Math.floor(i / 4));
                    y = ymin;
                    switchbackPoints.add(new Coordinate(x,y));
                    break;
                case 2:
                    x = xrow.elementAt(2 * (int) Math.floor(i / 4));
                    y = ymax;
                    switchbackPoints.add(new Coordinate(x,y));
                    break;
                case 3:
                    x = xrow.elementAt(1 + 2 * (int) Math.floor(i / 4));
                    y = ymax;
                    switchbackPoints.add(new Coordinate(x,y));
                    break;
//                case 0:
//                    x = xrow.elementAt( 2 * (int) Math.floor(i / 4));
//                    y = ymin;
//                    switchbackPoints.add(new Coordinate(x,y));
//                    break;
            }
        }
    }

    /**
     * Calculates distance along narrow side of rectangular picture taken vertically downward from
     * specified altitude with a DJI Phantom 4 drone.
     *
     * @param altitude altitude in meters
     * @return approximate width of short edge of picture of ground in meters
     */
    private float calculateImageWidth(float altitude) {
        return 0; //needs to be completed
    }

    /**
     * Calculates distance along long side of rectangular picture taken vertically downward from
     * specified altitude with a DJI Phantom 4 drone.
     *
     * @param altitude altitude in meters
     * @return approximate length of long edge of picture of ground in meters
     */
    private float calculateImageLength(float altitude) {
        return 0; //needs to be completed
    }

}
