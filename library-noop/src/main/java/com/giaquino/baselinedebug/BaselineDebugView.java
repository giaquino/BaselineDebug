package com.giaquino.baselinedebug;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class BaselineDebugView extends View {

  private int color = Color.MAGENTA;

  public BaselineDebugView(Context context) {
    this(context, null);
  }

  public BaselineDebugView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BaselineDebugView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaselineDebugView);
    color = a.getColor(R.styleable.BaselineDebugView_baselineColor, Color.MAGENTA);
    a.recycle();
  }

  public void setColor(int color) {
    this.color = color;
  }

  public int getColor() {
    return color;
  }
}
