package dji.developer.sample.utility;

import android.content.Context;

import com.dji.sdk.sample.common.utility.ApplicationContextManager;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * Created by Julia on 2017-01-25.
 */

public class TestApplicationContextManager
{
    @Test
    public void willReturnContextPassedInOnConstruction()
    {
        Context expectedContext = mock(Context.class);
        ApplicationContextManager contextManager = new ApplicationContextManager(expectedContext);

        Context actualContext = contextManager.getApplicationContext();

        assertSame(actualContext, expectedContext);
    }
}
