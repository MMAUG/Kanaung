package org.mmaug.kanaung;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yemyatthu on 4/7/15.
 */
public class Util {
  private static Map<Float, Float> pxCache = new HashMap<>();

  public static float calculateDpToPixel(float dp, Context context) {

    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return px;
  }

  public static float convertDpToPixel(float dp, final Context context) {

    Float f = pxCache.get(dp);
    if (f == null) {
      synchronized (pxCache) {
        f = calculateDpToPixel(dp, context);
        pxCache.put(dp, f);
      }
    }

    return f;
  }
}
