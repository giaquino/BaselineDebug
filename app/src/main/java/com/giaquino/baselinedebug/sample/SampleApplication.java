package com.giaquino.baselinedebug.sample;

import android.app.Application;
import com.giaquino.baselinedebug.BaselineDebug;

public class SampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    BaselineDebug.show(this);
  }
}
