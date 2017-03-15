package com.dji.sdk.sample.common.utility;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.model.TileProvider;

import static java.lang.Thread.currentThread;

/**
 * Created by Peter on 2017-03-14.
 */

public class CachedMapLoaderThread extends CachedMapNotifyingThread implements Runnable {

    private TileProvider offLineTileProvider;
    private FragmentActivity fragmentActivity;
    @Override
    public void doRun() throws Exception {
        //Log.d("CachedMapLoaderThread", " ID: " + currentThread().getName());
        try {
            offLineTileProvider = new CachedMapOfflineTileProvider(fragmentActivity);
        }catch(Exception ex){
            throw new Exception("Parsing Cached Map Data Failed! Make sure the files mapcachebinaryData.bin and mapcachebinaryIndex.csv are both in the /root/ directory.");
        }
        }
public void setFragmentActivity(FragmentActivity fragmentActivity){
        this.fragmentActivity = fragmentActivity;
    }
    public TileProvider getTileProvider(){
        return offLineTileProvider;
    }
}