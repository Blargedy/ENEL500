package com.dji.sdk.sample.common.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * Created by Peter on 2017-03-14.
 */

public class CachedMapOfflineTileProvider implements TileProvider {
    private FragmentActivity fragmentActivity;
    private HashMap<String, CachedMapTileHolder> tileHashMap;

    CachedMapOfflineTileProvider(FragmentActivity fragmentActivity) throws Exception {
        // do this in a sep thread
        tileHashMap = new HashMap<String, CachedMapTileHolder>();
        try {
            this.fragmentActivity = fragmentActivity;
            // make the HashMap using worker thread
            String line = "";
            String cvsSplitBy = ",";

            // Get Index File Handle
            String csvFile = Environment.getExternalStorageDirectory().toString() + "/mapcachebinaryIndex.csv";
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String binFilestr = Environment.getExternalStorageDirectory().toString() + "/mapcachebinaryData.bin";
            RandomAccessFile randomAccessBinaryData = new RandomAccessFile(binFilestr, "r");
            br.readLine(); // ignore header line
            // Load in all tile cache index lines
            String[] csvL;
            String keyString = "";
            while ((line = br.readLine()) != null) {
                // use comma as separator
                csvL = line.split(cvsSplitBy);
                keyString = "x" + csvL[3] + "y" + csvL[2] + "z" + csvL[1];
                int xx = Integer.parseInt(csvL[3]);
                int yy = Integer.parseInt(csvL[2]);
                int zz = Integer.parseInt(csvL[1]);
                int startIndex = Integer.parseInt(csvL[4]);
                int endIndex = Integer.parseInt(csvL[5]);
                int sizeInBytes = Integer.parseInt(csvL[6]);
                byte[] fileBytes = null;
                this.tileHashMap.put(keyString, new CachedMapTileHolder(zz, yy, xx, startIndex, endIndex, sizeInBytes, fileBytes));
            }
            br.close();

        } catch (Exception ex) {
            Log.e("CachedMapOfflineTilePro", "Parsing Cached Map Data Failed! Make sure the files mapcachebinaryData.bin and mapcachebinaryIndex.csv are both in the /root/ directory.");
            throw new Exception("Parsing Cached Map Data Failed! Make sure the files mapcachebinaryData.bin and mapcachebinaryIndex.csv are both in the /root/ directory.");
        }

    }

    @Override
    public Tile getTile(int x, int y, int z) {
        Tile tile = NO_TILE;

        try {
            // Find the bitmap data given the xyz key
            String binFilestr = Environment.getExternalStorageDirectory().toString() + "/mapcachebinaryData.bin";
            RandomAccessFile randomAccessBinaryData = new RandomAccessFile(binFilestr, "r");
            String keyString = "x" + String.valueOf(x) + "y" + String.valueOf(y) + "z" + String.valueOf(z);
            int size = tileHashMap.get(keyString).getSizeInBytes();
            int startIndex = tileHashMap.get(keyString).getStartIndex();
            byte[] bytes = new byte[size];
            randomAccessBinaryData.seek(startIndex);
            randomAccessBinaryData.readFully(bytes, 0, size); // array index out of bounds
            Bitmap tileBitmap = BitmapFactory.decodeByteArray(bytes, 0, size);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            tileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] fileBytes = stream.toByteArray();
            tile = new Tile(256, 256, fileBytes);
            randomAccessBinaryData.close();
            Log.d("MapPresenter", "Found and used: " + x + ", " + y + ", " + z);
        } catch (
                Exception e) {
            Log.e("MapPresenter", "Error can't find tile file. x = " + x + " y = " + y + " z = " + z);
        }
        return tile;
    }
}