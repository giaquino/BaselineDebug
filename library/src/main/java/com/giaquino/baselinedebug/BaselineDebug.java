package com.giaquino.baselinedebug;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

public class BaselineDebug {

  private static BaselineDebugView view;
  private static boolean showing;

  private BaselineDebug() {
  }

  public static void show(Application application) {
    if (showing) return;
    if (needsPermission(application)) return;

    WindowManager.LayoutParams lp =
        new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT, getWindowType(), getWindowFlags(),
            PixelFormat.TRANSLUCENT);

    if (view == null) view = new BaselineDebugView(application);

    WindowManager manager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    if (manager != null) manager.addView(view, lp);
    showing = true;
  }

  public static void hide(Application application) {
    WindowManager manager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    if (manager != null) manager.removeView(view);
    showing = false;
  }

  public static BaselineDebugView getBaselineDebugView() {
    return view;
  }

  public static boolean isShowing() {
    return showing;
  }

  private static int getWindowFlags() {
    return WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
  }

  private static int getWindowType() {
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    }
    return WindowManager.LayoutParams.TYPE_PHONE;
  }

  private static boolean needsPermission(Context context) {
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (!Settings.canDrawOverlays(context)) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
      }
    }
    return false;
  }
}
