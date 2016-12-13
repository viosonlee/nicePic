package lee.vioson.utils;

import android.app.Application;
import android.util.Log;

/**
 * Author:李烽
 * Date:2016-12-13
 * FIXME
 * Todo
 */

public class App {
    public static final Application INSTANCE;
    public static final String TAG = "lee.vioson.utils.App";

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(TAG, "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(TAG, "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            INSTANCE = app;
        }
    }
}
