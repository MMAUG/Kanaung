package org.gdgyangon.kanaung;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import java.util.HashMap;

/**
 * Created by yemyatthu on 4/9/15.
 */
public class Kanaung extends Application{
  @Override public void onCreate() {
    super.onCreate();

  }
  synchronized Tracker getTracker(TrackerName trackerId) {
    if (!mTrackers.containsKey(trackerId)) {

      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
     if(trackerId == TrackerName.APP_TRACKER) {
       Tracker t = analytics.newTracker(R.xml.app_tracker);
       mTrackers.put(trackerId, t);
     }


    }
    return mTrackers.get(trackerId);
  }
  public enum TrackerName {
    APP_TRACKER, // Tracker used only in this app.
    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
    ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
  }

  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
}

