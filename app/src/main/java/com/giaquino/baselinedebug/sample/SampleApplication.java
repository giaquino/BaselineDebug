package com.giaquino.baselinedebug.sample;

import android.app.Application;
import com.giaquino.baselinedebug.BaselineDebug;
import com.giaquino.baselinedebug.BaselineDebugView;

public class SampleApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    BaselineDebug.show(this);
    BaselineDebug.getBaselineDebugView().setBaselineType(BaselineDebugView.TYPE_GRID);
  }
}
