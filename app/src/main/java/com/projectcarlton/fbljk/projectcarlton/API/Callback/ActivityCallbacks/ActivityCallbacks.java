package com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityCallbacks {

    private static Map<Integer, ArrayList<ActivityCallback>> callbacks = new HashMap<Integer, ArrayList<ActivityCallback>>();

    public static void registerActivityCallback(ActivityCallback callback, int activityCallbackType) {
        if (callbacks.containsKey(activityCallbackType)) {
            if (callbacks.get(activityCallbackType) != null) {
                callbacks.get(activityCallbackType).add(callback);
            }
        } else {
            ArrayList<ActivityCallback> cbs = new ArrayList<ActivityCallback>();
            cbs.add(callback);
            callbacks.put(activityCallbackType, cbs);
        }
    }

    public static void deregisterActivityCallback(ActivityCallback callback, int activityCallbackType) {
        if (callbacks.containsKey(activityCallbackType)) {
            if (callbacks.get(activityCallbackType) != null && !callbacks.get(activityCallbackType).isEmpty()) {
                callbacks.get(activityCallbackType).remove(callback);
            }
        }
    }

    public static void request(int activityCallbackType, Object... options) {
        if (callbacks.containsKey(activityCallbackType)) {
            if (callbacks.get(activityCallbackType) != null && !callbacks.get(activityCallbackType).isEmpty()) {
                for (int i = 0; i < callbacks.get(activityCallbackType).size(); i++) {
                    ActivityCallback callback = callbacks.get(activityCallbackType).get(i);
                    callback.callbackActivity(activityCallbackType, options);
                }
            }
        }
    }
}