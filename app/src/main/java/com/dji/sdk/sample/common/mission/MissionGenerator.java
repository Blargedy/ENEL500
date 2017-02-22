package com.dji.sdk.sample.common.mission;

import android.widget.Toast;

import com.dji.sdk.sample.common.entity.GeneratedMissionModel;
import com.dji.sdk.sample.common.entity.InitialMissionModel;
import com.dji.sdk.sample.common.utility.I_ApplicationContextManager;
import com.dji.sdk.sample.common.values.Coordinate;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.missionmanager.DJICustomMission;
import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIMissionManager;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.missionmanager.missionstep.DJIMissionStep;
import dji.sdk.missionmanager.missionstep.DJIWaypointStep;
import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by Julia on 2017-01-15.
 */

public class MissionGenerator implements I_MissionGenerator
{
    private InitialMissionModel initialMissionModel_;
    private GeneratedMissionModel generatedMissionModel_;
    private I_ApplicationContextManager contextManager_;

    public MissionGenerator(I_ApplicationContextManager contextManager, InitialMissionModel initialMissionModel, GeneratedMissionModel generatedMissionModel){
        initialMissionModel_ = initialMissionModel;
        generatedMissionModel_ = generatedMissionModel;
        contextManager_ = contextManager;
    }

    public void generateMission(){
        List<Coordinate> switchbackVector= SwitchBackPathGenerator.generateSwitchback(initialMissionModel_.missionBoundary().bottomLeft(),
                initialMissionModel_.missionBoundary().topRight(), initialMissionModel_.altitude());


        //produce List of waypoints
        Vector<DJIWaypoint> waypoints = new Vector<DJIWaypoint>();
        Vector<DJIWaypointMission> waypointMissions = new Vector<DJIWaypointMission>();
        List<DJIMissionStep> missionSteps = new Vector<DJIMissionStep>();

        Iterator switchBackIter = switchbackVector.iterator();
        while(switchBackIter.hasNext()){
            Coordinate nextPoint = (Coordinate) switchBackIter.next();
            waypoints.add(new DJIWaypoint(nextPoint.latitude_, nextPoint.longitude_, initialMissionModel_.altitude()));
        }

        Iterator waypointIter = waypoints.iterator();
        int waypointCount = 0;
        int wayPointMissionIndex = -1;

        while(waypointIter.hasNext()) {
            if(wayPointMissionIndex == -1 || waypointCount >= 100){
                waypointCount = 0;
                DJIWaypointMission waypointMission = new DJIWaypointMission();
                waypointMission.autoFlightSpeed = 10;

                waypointMissions.add(waypointMission);
                wayPointMissionIndex++;
                waypointMissions.elementAt(waypointCount).addWaypoint((DJIWaypoint) waypointIter.next());
            }
            else {
                waypointMissions.elementAt(wayPointMissionIndex).addWaypoint((DJIWaypoint) waypointIter.next());
                waypointCount++;
            }
        }

        Iterator waypointMissionIter = waypointMissions.iterator();

        while(waypointMissionIter.hasNext()){
            DJIMissionStep nextStep = new DJIWaypointStep((DJIWaypointMission) waypointMissionIter.next(), new DJICommonCallbacks.DJICompletionCallback(){
                @Override
                public void onResult(DJIError error) {
                    if (error == null) {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "Successfully reached Waypoint", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contextManager_.getApplicationContext(),
                                "could not reach waypoint", Toast.LENGTH_LONG).show();
                    }
                }
            });

            missionSteps.add(nextStep);
        }

        //set entity
        generatedMissionModel_.djiMission_ =  new DJICustomMission(missionSteps);

        DJIAircraft aircraft = (DJIAircraft) DJISDKManager.getInstance().getDJIProduct();
        DJIMissionManager missionManager = aircraft.getMissionManager();

        DJIMission.DJIMissionProgressHandler progressHandler = new DJIMission.DJIMissionProgressHandler()
        {
            @Override
            public void onProgress(DJIMission.DJIProgressType type, float progress) {}
        };

        aircraft.getFlightController().setHomeLocationUsingAircraftCurrentLocation(new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
                if (djiError == null)
                {
                    Toast.makeText(contextManager_.getApplicationContext(), "set home location current" , Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(contextManager_.getApplicationContext(), "can't set home location", Toast.LENGTH_LONG).show();
                }
            }
        });

        missionManager.prepareMission(generatedMissionModel_.djiMission_, progressHandler, new DJICommonCallbacks.DJICompletionCallback()
        {
            @Override
            public void onResult(DJIError error) {
                if (error == null)
                {
                    Toast.makeText(contextManager_.getApplicationContext(), "Prepared Mission Successfully", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(contextManager_.getApplicationContext(), "Failed to Prepare Mission " + error.getDescription(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
