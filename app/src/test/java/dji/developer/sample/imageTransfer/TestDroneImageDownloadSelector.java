package dji.developer.sample.imageTransfer;

import com.dji.sdk.sample.common.imageTransfer.DroneImageDownloadSelector;

import org.junit.Test;

import java.util.ArrayList;

import dji.sdk.camera.DJIMedia;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-02-17.
 */

public class TestDroneImageDownloadSelector
{
    private DroneImageDownloadSelector patient_ = new DroneImageDownloadSelector();

    @Test
    public void willReturnAnEmptyListForDownloadWhenDroneMediaListIsUninitializedAndCurrentListIsEmpty()
    {
        ArrayList<DJIMedia> currentMediaList = new ArrayList<>();

        ArrayList<DJIMedia> imagesToDownload =
                patient_.determineImagesForDownloadFromMediaList(currentMediaList);

        assertTrue(imagesToDownload.isEmpty());
    }

    @Test
    public void willReturnAnEmptyListForDownloadWhenTheCurrentMediaListMatchesThePastMediaList()
    {
        ArrayList<DJIMedia> pastMediaList = makeMediaList(2);
        patient_.initializeDroneMediaList(pastMediaList);

        ArrayList<DJIMedia> imagesToDownload =
                patient_.determineImagesForDownloadFromMediaList(pastMediaList);

        assertTrue(imagesToDownload.isEmpty());
    }

    @Test
    public void willReturnImagesInCurrentMediaThatAreNotInMediaListThatItWasInitializedWith()
    {
        ArrayList<DJIMedia> pastMediaList = makeMediaList(1, 2);
        ArrayList<DJIMedia> currentMediaList = makeMediaList(1, 4);
        ArrayList<DJIMedia> expectedMediaList = makeMediaList(3, 4);
        patient_.initializeDroneMediaList(pastMediaList);

        ArrayList<DJIMedia> imagesToDownload =
                patient_.determineImagesForDownloadFromMediaList(currentMediaList);

        assertEquals(expectedMediaList.size(), imagesToDownload.size());
        for(int i = 0; i < expectedMediaList.size(); i++)
        {
            assertEquals(
                    expectedMediaList.get(i).getFileName(),
                    imagesToDownload.get(i).getFileName());
        }
    }

    @Test
    public void willReturnImagesInCurrentMediaThatAreNotInPastMediaList()
    {
        ArrayList<DJIMedia> pastMediaList = makeMediaList(1, 2);
        ArrayList<DJIMedia> currentMediaList = makeMediaList(1, 4);
        ArrayList<DJIMedia> expectedMediaList = makeMediaList(3, 4);
        patient_.determineImagesForDownloadFromMediaList(pastMediaList);

        ArrayList<DJIMedia> imagesToDownload =
                patient_.determineImagesForDownloadFromMediaList(currentMediaList);

        assertEquals(expectedMediaList.size(), imagesToDownload.size());
        for(int i = 0; i < expectedMediaList.size(); i++)
        {
            assertEquals(
                    expectedMediaList.get(i).getFileName(),
                    imagesToDownload.get(i).getFileName());
        }
    }

    private ArrayList<DJIMedia> makeMediaList(int size)
    {
        return makeMediaList(0, size);
    }

    private ArrayList<DJIMedia> makeMediaList(int lowerIndex, int upperIndex)
    {
        ArrayList<DJIMedia> currentMediaList = new ArrayList<>();
        for (int i = lowerIndex; i <= upperIndex; i++)
        {
            DJIMedia media = mock(DJIMedia.class);
            when(media.getFileName()).thenReturn("name" + i);
            currentMediaList.add(media);
        }
        return currentMediaList;
    }
}
