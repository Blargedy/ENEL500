package com.dji.sdk.sample.common.utility;

import android.content.Context;

/**
 * Created by Julia on 2017-01-25.
 */

public class ApplicationContextManager
        implements I_ApplicationContextManager
{
    private Context context_;

    public ApplicationContextManager(Context context)
    {
        context_ = context;
    }

    @Override
    public Context getApplicationContext() {
        return context_;
    }
}
