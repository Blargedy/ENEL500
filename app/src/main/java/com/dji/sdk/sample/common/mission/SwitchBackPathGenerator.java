package com.dji.sdk.sample.common.mission;

import java.util.List;
import java.util.Vector;

/**
 * Created by eric on 2017-01-23.
 * Edited by Bill Skinner on 2017-02-08
 */

public class SwitchBackPathGenerator {
    /**
     * Percentage of overlap, used to determine spacing between pictures
     */
    private float percentOverlap_ = 80;

    public static List<Coordinate> generateSwitchback(Coordinate bottomLeft, Coordinate topRight, float altitude){
        List<Coordinate> switchbackPoints= new Vector<Coordinate>();



        switchbackPoints.add(new Coordinate(51.080157, -114.131933));
        return switchbackPoints;
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
