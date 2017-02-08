package com.dji.sdk.sample.common.mission;

import java.util.List;
import java.util.Vector;

/**
 * Created by eric on 2017-01-23.
 */

public class SwitchBackPathGenerator {
    public static List<Coordinate> generateSwitchback(Coordinate bottomLeft, Coordinate topRight, float altitude){
        List<Coordinate> switchbackPoints= new Vector<Coordinate>();
        switchbackPoints.add(new Coordinate(51.080157, -114.131933));
        return switchbackPoints;
    }
}
