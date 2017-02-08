package dji.developer.sample;

import android.test.mock.MockContext;
import android.util.AttributeSet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by Julia on 2017-01-16.
 */

public class TestSimpleDemoView
{
    // TODO figure out how to programatically add views rather than using view inflation from XML
    private MockContext context = new MockContext();
    private AttributeSet attributes;

    @Test
    public void willDisableHoverNowButtonOnConstruction() throws Exception
    {

    }
}
