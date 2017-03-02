package com.dji.sdk.sample.common.mission;

import com.dji.sdk.sample.common.values.Coordinate;

import java.util.List;
import java.util.Vector;

import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

import java.lang.Math;

/**
 * Created by eric on 2017-01-23.
 * Edited by Bill on 2017-02-15
 * Edited by Bill on 2017-03-01
 */

public class SwitchBackPathGenerator {

    private static double minimumPercentImageOverlap_ = 0.80; // number between 0 and 1
    private static double minimumPercentSwathOverlap_ = 0.50; // number between 0 and 1

    public static List<Coordinate> generateSwitchback(Coordinate bottomLeft, Coordinate topRight, float altitude) {
        List<Coordinate> switchbackPoints = new Vector<Coordinate>();

        // Initial Testing
//        DJIAircraft aircraft = (DJIAircraft) DJISDKManager.getInstance().getDJIProduct();
//        double curLat = aircraft.getFlightController().getCurrentState().getAircraftLocation().getLatitude();
//        double curLon = aircraft.getFlightController().getCurrentState().getAircraftLocation().getLongitude();
//        double change = 0.000008;
//
//        switchbackPoints.add(new Coordinate(curLat + change, curLon));
//        switchbackPoints.add(new Coordinate(curLat + change * 2.0, curLon));

        //generatePathWaypoints(switchbackPoints, new Coordinate(51.081805, -114.161351),new Coordinate(51.082000, -114.160979), change);

        // Spacing calculations
        double minimumImageSpacingInMeters = (1 - minimumPercentImageOverlap_) * calculateImageLength((double) altitude);
        double minimumSwathSpacingInMeters = (1 - minimumPercentSwathOverlap_) * calculateImageWidth((double) altitude);

        Coordinate bottomRight = new Coordinate(topRight.latitude_, bottomLeft.longitude_);
        Coordinate topLeft = new Coordinate(bottomLeft.latitude_, topRight.longitude_);

        double averageSwathLengthInMeters = (bottomLeft.distanceApproximationInMeters(topLeft) + bottomRight.distanceApproximationInMeters(topRight)) / 2.0;
        double averageDistancePerpendicularToSwathInMeters = (bottomLeft.distanceApproximationInMeters(bottomRight) + topLeft.distanceApproximationInMeters(topRight)) / 2.0;

        int numberOfSwaths = 1 + (int) Math.ceil(averageDistancePerpendicularToSwathInMeters / minimumSwathSpacingInMeters);
        int numberOfImagesPerSwath = 1 + (int) Math.ceil(averageSwathLengthInMeters / minimumImageSpacingInMeters);

        // Generate Path Coordinates
        generatePathAndImageCoordinates(switchbackPoints, bottomLeft, topLeft, topRight, bottomRight, numberOfSwaths, numberOfImagesPerSwath);

        return switchbackPoints;
    }

    private static void generatePathAndImageCoordinates(List<Coordinate> switchbackPoints, Coordinate bottomLeft, Coordinate topLeft, Coordinate topRight, Coordinate bottomRight, int numberOfSwaths, int numberOfImagesPerSwath) {
        List<Coordinate> topPathCoordinates = new Vector<>();
        List<Coordinate> bottomPathCoordinates = new Vector<>();
        topPathCoordinates.add(topLeft);
        topPathCoordinates.add(topRight);
        bottomPathCoordinates.add(bottomLeft);
        bottomPathCoordinates.add(bottomRight);

        insertLinearlyDistributedCoordinates(topPathCoordinates, 0, numberOfSwaths - 2);
        insertLinearlyDistributedCoordinates(bottomPathCoordinates, 0, numberOfSwaths - 2);

        for (int i = 0; i < (numberOfSwaths); i++) {
            switch (i % 2) {
                case 0: { // Even Swaths - Bottom to Top
                    Coordinate firstCoordinate = bottomPathCoordinates.get(i);
                    Coordinate secondCoordinate = topPathCoordinates.get(i);
                    switchbackPoints.add(firstCoordinate);
                    switchbackPoints.add(secondCoordinate);
                    insertLinearlyDistributedCoordinates(switchbackPoints, switchbackPoints.size() - 2, numberOfImagesPerSwath);
                    break;
                }
                case 1: { // Odd Swaths - Top to Bottom
                    Coordinate firstCoordinate = topPathCoordinates.get(i);
                    Coordinate secondCoordinate = bottomPathCoordinates.get(i);
                    switchbackPoints.add(firstCoordinate);
                    switchbackPoints.add(secondCoordinate);
                    insertLinearlyDistributedCoordinates(switchbackPoints, switchbackPoints.size() - 2, numberOfImagesPerSwath);
                    break;
                }
            }
        } // end for loop
    }

    public static void insertLinearlyDistributedCoordinates(List<Coordinate> coordinateList, int startIndex, int numberOfCoordinatesToInsert) {
        double deltaLatitude = coordinateList.get(startIndex + 1).latitude_ - coordinateList.get(startIndex).latitude_;
        double deltaLongitude = coordinateList.get(startIndex + 1).longitude_ - coordinateList.get(startIndex).longitude_;
        int divisor = 1 + numberOfCoordinatesToInsert;
        double fractionalStep = (1.0 / ((double) divisor));
        double latitudeStep = fractionalStep * deltaLatitude;
        double longitudeStep = fractionalStep * deltaLongitude;

        for (int i = 0; i < numberOfCoordinatesToInsert; i++) {
            double newLatitude = coordinateList.get(startIndex).latitude_ + (i + 1) * latitudeStep;
            double newLongitude = coordinateList.get(startIndex).longitude_ + (i + 1) * longitudeStep;
            Coordinate newCoordinate = new Coordinate(newLatitude, newLongitude);
            coordinateList.add(startIndex + i + 1, newCoordinate);
        }
    }

    private static void generatePathWaypoints(List<Coordinate> switchbackPoints, Coordinate bottomLeft, Coordinate topRight, double spacing, int numberOfSwaths) {
        int n = 2 * numberOfSwaths;

        Vector<Double> xrow = new Vector<>();

//        for(double i = bottomLeft.longitude_; i <= topRight.longitude_; i = i + spacing){
//            xrow.add(i);
//        }

        for (int i = 0; i < numberOfSwaths; i++) {
            xrow.add(bottomLeft.longitude_ + i * spacing);
        }

        double ymin = bottomLeft.latitude_;
        double ymax = topRight.latitude_;

        double x;
        double y;

        for (int i = 0; i < n; i++) {
            int m = i % 4;

            switch (m) {
                case 1:
                    x = xrow.elementAt(2 * (int) Math.floor(i / 4));
                    y = ymin;
                    switchbackPoints.add(new Coordinate(x, y));
                    break;
                case 2:
                    x = xrow.elementAt(2 * (int) Math.floor(i / 4));
                    y = ymax;
                    switchbackPoints.add(new Coordinate(x, y));
                    break;
                case 3:
                    x = xrow.elementAt(1 + 2 * (int) Math.floor(i / 4));
                    y = ymax;
                    switchbackPoints.add(new Coordinate(x, y));
                    break;
                case 0:
                    x = xrow.elementAt(2 * (int) Math.floor(i / 4));
                    y = ymin;
                    switchbackPoints.add(new Coordinate(x, y));
                    break;
            }
        }
    }

    /**
     * Calculates distance along narrow side of rectangular picture taken vertically downward from
     * specified altitude with a DJI Phantom 4 drone.
     *
     * @param altitude altitude in meters
     * @return approximate length of short edge of picture of ground in meters
     */
    private static double calculateImageLength(double altitude) {
        return (1.2 * altitude); // factor of 1.2 based on numerical fit from test data
    }

    /**
     * Calculates distance along long side of rectangular picture taken vertically downward from
     * specified altitude with a DJI Phantom 4 drone.
     *
     * @param altitude altitude in meters
     * @return approximate width of long edge of picture of ground in meters
     */
    private static double calculateImageWidth(double altitude) {
        return (1.6 * altitude); // factor of 1.6 based on numerical fit from test data
    }
}
