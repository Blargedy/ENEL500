package com.dji.sdk.sample.common.utility;

/**
 * Created by Peter on 2017-03-14.
 */

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class CachedMapNotifyingThread extends Thread {
    private final Set<I_CachedMapThreadCompletedListener> listeners
            = new CopyOnWriteArraySet<I_CachedMapThreadCompletedListener>();

    public final void addListener(final I_CachedMapThreadCompletedListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final I_CachedMapThreadCompletedListener listener) {
        listeners.remove(listener);
    }

    private final void notifyListeners(boolean cachedMapLoadedSuccessfully) {
        for (I_CachedMapThreadCompletedListener listener : listeners) {
            if (cachedMapLoadedSuccessfully) {
                listener.notifyOfThreadComplete(this);
            }
        }
    }

    @Override
    public final void run() {

        try {
            doRun();
            notifyListeners(true); // only pass tile provider if no exception
        } catch (Exception ex) {
            notifyListeners(false);
        }

    }

    public abstract void doRun() throws Exception;
}