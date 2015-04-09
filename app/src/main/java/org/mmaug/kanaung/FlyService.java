package org.mmaug.kanaung;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import org.gdgyangon.kanaung.R;

import static android.view.GestureDetector.SimpleOnGestureListener;

public class FlyService extends Service {
  private PopupWindow popupWindow;
  private ImageView chatHead;
  private WindowManager windowManager;
  private GestureDetector gestureDetector;
  private int tempx = 10;
  private int tempy = 100;

  private static final String TAG = "Kanaung";

  private static final int DEFAULTX = 10;
  private static final int DEFAULTY = 200;

  private WindowManager.LayoutParams params;

  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    createNotification();

    params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
    params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
    params.gravity = Gravity.TOP | Gravity.LEFT;
    params.x = DEFAULTX;
    params.y = DEFAULTY;
    params.width = Math.round(Util.calculateDpToPixel(60, getApplicationContext()));
    params.height = Math.round(Util.calculateDpToPixel(60, getApplicationContext()));
    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    chatHead = (ImageView) inflater.inflate(R.layout.chat_head, null);

    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    windowManager.addView(chatHead, params);

    gestureDetector = new GestureDetector(this, new FlyGestureListener());
    chatHead.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
      }
    });
    PopupViewHelper popupViewHelper = new PopupViewHelper(this) {
      @Override public void onClickFullScreen() {
        WindowManager windowManager =
            (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        int height = (int) Math.round(display.getHeight() / 1.2);
        popupWindow.update(width, height);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
      }

      @Override public void onClickNormalScreen() {
        WindowManager windowManager =
            (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight() / 2;
        popupWindow.update(width, height);
      }
    };
    View popupView = popupViewHelper.getPopUpView();

    Display display = windowManager.getDefaultDisplay();
    int width = display.getWidth() - 40;
    int height = display.getHeight() / 2;
    popupWindow = new PopupWindow(popupView, width, height);
    popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_border));
    popupWindow.setFocusable(true);
    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
      @Override public void onDismiss() {
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            params.x = tempx;
            params.y = tempy;
            windowManager.updateViewLayout(chatHead, params);
          }
        }, 200);
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    NotificationManager mNotificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.cancelAll();
    if (chatHead != null) {
      windowManager.removeView(chatHead);
    }
  }

  class FlyGestureListener extends SimpleOnGestureListener {
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    private int previousX;
    private int previousY;

    @Override
    public boolean onDown(MotionEvent event) {
      //Log.d(TAG, "Motion Down;");
      initialX = params.x;
      initialY = params.y;
      initialTouchX = event.getRawX();
      initialTouchY = event.getRawY();
      return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
      Intent i = new Intent(getApplicationContext(), MainActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      getApplicationContext().startActivity(i);
      return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
      if (popupWindow.isShowing()) {
        params.x = tempx;
        params.y = tempy;
        popupWindow.dismiss();
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            windowManager.updateViewLayout(chatHead, params);
          }
        }, 200);
      } else {
        tempx = ((WindowManager.LayoutParams) chatHead.getLayoutParams()).x;
        tempy = ((WindowManager.LayoutParams) chatHead.getLayoutParams()).y;
        params.x = 10;
        params.y = 10;
        windowManager.updateViewLayout(chatHead, params);
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            popupWindow.showAsDropDown(chatHead, 10, 10);
          }
        }, 200);
      }

      return true;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
      if (popupWindow.isShowing()) popupWindow.dismiss();

      params.x = initialX + (int) (e2.getRawX() - initialTouchX);
      params.y = initialY + (int) (e2.getRawY() - initialTouchY);
      windowManager.updateViewLayout(chatHead, params);

      return true;
    }
  }

  public void createNotification() {
    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Kanaung is running.");
    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    stackBuilder.addParentStack(MainActivity.class);
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    mBuilder.setContentIntent(resultPendingIntent);
    NotificationManager mNotificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.notify(2, mBuilder.build());
  }
}
