package com.dji.sdk.sample.common.utility;

/**
 * Created by Peter on 2017-03-14.
 */

// Offline Map tile container class
public class CachedMapTileHolder {
    private int z;
    private int y;
    private int x;
    private int startIndex;
    private int endIndex;
    private int sizeInBytes;
    private byte[] tileBitmapBytes;

    CachedMapTileHolder(int z, int y, int x, int startIndex, int endIndex, int sizeInBytes, byte[] tileBitmapBytes) {
        this.z = z;
        this.y = y;
        this.x = x;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.sizeInBytes = sizeInBytes;
        this.tileBitmapBytes = tileBitmapBytes;
    }

    public int getStartIndex(){
        return this.startIndex;
    }
    public int getSizeInBytes(){
        return this.sizeInBytes;
    }
}

