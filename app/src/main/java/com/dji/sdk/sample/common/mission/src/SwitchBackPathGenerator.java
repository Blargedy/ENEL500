package com.dji.sdk.sample.common.mission.src;

import com.dji.sdk.sample.common.values.Coordinate;

import java.util.List;
import java.util.Vector;

import java.lang.Math;

/**
 * Created by eric on 2017-01-23.
 * Edited by Bill on 2017-02-15
 * Edited by Bill on 2017-03-01
 * Edited by Bill on 2017-03-08
 */

public class SwitchBackPathGenerator {

    private static double minimumPercentImageOverlap_ = 0.80; // number between 0 and 1
    private static double minimumPercentSwathOverlap_ = 0.50; // number between 0 and 1

    private Coordinate bottomLeft_;
    private Coordinate topLeft_;
    private Coordinate topRight_;
    private Coordinate bottomRight_;

    private float altitude_;
    private int numberOfSwaths_;
    private int numberOfImagesPerSwath_;

    private Vector<Coordinate> leftOrBottomSwathEndpointCoordinates_ = null;
    private Vector<Coordinate> rightOrTopSwathEndpointCoordinates_ = null;
    private Vector<Coordinate> switchbackPoints_ = null;

    public SwitchBackPathGenerator(Coordinate bottomLeft, Coordinate topRight, float altitude) {
        altitude_ = altitude;

        bottomLeft_ = bottomLeft;
        topRight_ = topRight;

        // Determine bottomRight and topLeft coordinates assuming rectangular area
        bottomRight_ = new Coordinate(bottomLeft.latitude_, topRight.longitude_);
        topLeft_ = new Coordinate(topRight.latitude_, bottomLeft.longitude_);
    }

    public Vector<Coordinate> generateSwitchback() {
        switchbackPoints_ = new Vector<Coordinate>();

        // Generate path coordinates choosing longer swaths
        double leftRightDistance = bottomLeft_.distanceApproximationInMeters(bottomRight_);
        double bottomTopDistance = bottomLeft_.distanceApproximationInMeters(topLeft_);
        if (leftRightDistance > bottomTopDistance) {
            // Compute the numbers of swaths and images per swath assuming rectangular area and east-west swaths
            numberOfSwaths_ = calculateNumberOfSwaths(bottomLeft_, topLeft_, altitude_);
            numberOfImagesPerSwath_ = calculateNumberOfImagesPerSwath(bottomLeft_, bottomRight_, altitude_);
            createLeftRightSwathEndpointCoordinates();
        } else {
            // Compute the numbers of swaths and images per swath assuming rectangular area and north-south swaths
            numberOfSwaths_ = calculateNumberOfSwaths(bottomLeft_, bottomRight_, altitude_);
            numberOfImagesPerSwath_ = calculateNumberOfImagesPerSwath(bottomLeft_, topLeft_, altitude_);
            createBottomTopSwathEndpointCoordinates();
        }
        generatePathAndImageCoordinates();

        return switchbackPoints_;
    }

    private void createLeftRightSwathEndpointCoordinates() {
        // Create Swath Endpoints for left to right swaths
        leftOrBottomSwathEndpointCoordinates_ = new Vector<>();
        rightOrTopSwathEndpointCoordinates_ = new Vector<>();

        // Left Edge
        leftOrBottomSwathEndpointCoordinates_.add(bottomLeft_);
        leftOrBottomSwathEndpointCoordinates_.add(topLeft_);

        // Right Edge
        rightOrTopSwathEndpointCoordinates_.add(bottomRight_);
        rightOrTopSwathEndpointCoordinates_.add(topRight_);

        insertLinearlyDistributedCoordinates(leftOrBottomSwathEndpointCoordinates_, 0, numberOfSwaths_ - 2);
        insertLinearlyDistributedCoordinates(rightOrTopSwathEndpointCoordinates_, 0, numberOfSwaths_ - 2);
    }

    private void createBottomTopSwathEndpointCoordinates() {
        // Create Swath Endpoints for bottom to top swaths
        leftOrBottomSwathEndpointCoordinates_ = new Vector<>();
        rightOrTopSwathEndpointCoordinates_ = new Vector<>();

        // Bottom Edge
        leftOrBottomSwathEndpointCoordinates_.add(bottomLeft_);
        leftOrBottomSwathEndpointCoordinates_.add(bottomRight_);

        // Top Edge
        rightOrTopSwathEndpointCoordinates_.add(topLeft_);
        rightOrTopSwathEndpointCoordinates_.add(topRight_);

        int numberOfCoordinatesToInsert = numberOfSwaths_ - 2; // total minus endpoints
        int startIndex = 0; // inserts after startIndex
        insertLinearlyDistributedCoordinates(leftOrBottomSwathEndpointCoordinates_, startIndex, numberOfCoordinatesToInsert);
        insertLinearlyDistributedCoordinates(rightOrTopSwathEndpointCoordinates_, startIndex, numberOfCoordinatesToInsert);
    }

    private void generatePathAndImageCoordinates() {
        // Create swaths and append them to switchbackPoints
        for (int i = 0; i < (numberOfSwaths_); i++) {
            switch (i % 2) {
                case 0: { // Even Swaths - Left to Right
                    // Set coordinates defining start and end of a swath
                    Coordinate swathStartingCoordinate = leftOrBottomSwathEndpointCoordinates_.get(i); // start at left or bottom
                    Coordinate swathEndingCoordinate = rightOrTopSwathEndpointCoordinates_.get(i);
                    appendSwath(switchbackPoints_, swathStartingCoordinate, swathEndingCoordinate, numberOfImagesPerSwath_);
                    break;
                }
                case 1: { // Odd Swaths - Right to Left
                    // Set coordinates defining start and end of a swath
                    Coordinate swathStartingCoordinate = rightOrTopSwathEndpointCoordinates_.get(i); // start at right or top
                    Coordinate swathEndingCoordinate = leftOrBottomSwathEndpointCoordinates_.get(i);
                    appendSwath(switchbackPoints_, swathStartingCoordinate, swathEndingCoordinate, numberOfImagesPerSwath_);
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

    private static void appendSwath(List<Coordinate> coordinateList, Coordinate swathStartingCoordinate, Coordinate swathEndingCoordinate, int numberOfImagesPerSwath) {

        // Append starting and ending coordinates to end of list
        coordinateList.add(swathStartingCoordinate);
        coordinateList.add(swathEndingCoordinate);

        // Fill with intermediate coordinates between start and end
        int indexToBeginInsertion = coordinateList.size() - 2; // insert just before last coordinate
        insertLinearlyDistributedCoordinates(coordinateList, indexToBeginInsertion, numberOfImagesPerSwath - 2);
    }

    private static int calculateNumberOfSwaths(Coordinate startingCoordinate, Coordinate endingCoordinate, float altitude) {
        double maximumSwathSpacingInMeters = calculateSwathSpacing(minimumPercentSwathOverlap_, altitude);
        double distanceInMeters = startingCoordinate.distanceApproximationInMeters(endingCoordinate);
        return (2 + calculateNumberOfIntermediatePoints(distanceInMeters, maximumSwathSpacingInMeters));
    }

    private static int calculateNumberOfImagesPerSwath(Coordinate swathStartingCoordinate, Coordinate swathEndingCoordinate, float altitude) {
        double maximumImageSpacingInMeters = calculateImageSpacing(minimumPercentImageOverlap_, altitude);
        double swathLengthInMeters = swathStartingCoordinate.distanceApproximationInMeters(swathEndingCoordinate);
        return (2 + calculateNumberOfIntermediatePoints(swathLengthInMeters, maximumImageSpacingInMeters));
    }

    /**
     * Returns number of linearly spaced intermediate points needed to ensure no more than maximum
     * spacing between any two points
     *
     * @param distance       e.g. 10
     * @param maximumSpacing e.g. 4
     * @return eg. 2
     */
    private static int calculateNumberOfIntermediatePoints(double distance, double maximumSpacing) {
        return (int) Math.floor(distance / maximumSpacing);
    }

    /**
     * Calculates the minimum distance between two swaths of aerial images taken at a specific
     * altitude to have the specified swath overlap.
     *
     * @param percentSwathOverlap_ a number between 0 and 1, e.g. 0.45 for 45% swath overlap
     * @param altitude             altitude in meters
     * @return distance in meters between two swaths taken at the same specified altitude to achieve specified overlap
     */
    private static double calculateSwathSpacing(double percentSwathOverlap_, float altitude) {
        return (1 - percentSwathOverlap_) * calculateImageWidth((double) altitude);
    }

    /**
     * Calculates the minimum distance between two aerial images taken at a specific altitude to
     * have the specified image overlap.
     *
     * @param percentImageOverlap_ a number between 0 and 1, e.g. 0.8 for 80% image overlap
     * @param altitude             altitude in meters
     * @return distance in meters between two images taken at the same specified altitude to achieve specified overlap
     */
    private static double calculateImageSpacing(double percentImageOverlap_, float altitude) {
        return (1 - percentImageOverlap_) * calculateImageLength((double) altitude);
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
