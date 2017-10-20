package com.giaquino.baselinedebug;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class BaselineDebugView extends View {

  public static final int TYPE_HORIZONTAL = 0;
  public static final int TYPE_VERTICAL = 1;
  public static final int TYPE_GRID = 2;

  private final float ONE_DIP;
  private final float EIGHT_DIP;
  private final Paint paint;
  private float baselineSpacing;
  private int baselineType;

  public BaselineDebugView(Context context) {
    this(context, null);
  }

  public BaselineDebugView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BaselineDebugView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    ONE_DIP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
        getResources().getDisplayMetrics());
    EIGHT_DIP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
        getResources().getDisplayMetrics());

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaselineDebugView);
    int color =
        a.getColor(R.styleable.BaselineDebugView_baselineColor, Color.parseColor("#E293C3"));
    baselineType = a.getInteger(R.styleable.BaselineDebugView_baselineType, TYPE_HORIZONTAL);
    baselineSpacing =
        a.getDimension(R.styleable.BaselineDebugView_baselineSpacing, (int) EIGHT_DIP);
    a.recycle();

    paint = new Paint();
    paint.setColor(color);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(ONE_DIP);
    paint.setStyle(Paint.Style.STROKE);
  }

  @Override public void draw(Canvas canvas) {
    super.draw(canvas);
    // draw horizontal line
    if (baselineType == TYPE_GRID || baselineType == TYPE_HORIZONTAL) {
      float y = baselineSpacing;
      while (y < getHeight()) {
        canvas.drawLine(0, y, getWidth(), y, paint);
        y += baselineSpacing;
      }
    }

    // draw vertical line
    if (baselineType == TYPE_GRID || baselineType == TYPE_VERTICAL) {
      float x = baselineSpacing;
      while (x < getWidth()) {
        canvas.drawLine(x, 0, x, getHeight(), paint);
        x += baselineSpacing;
      }
    }
  }

  public void setBaselineColor(int color) {
    paint.setColor(color);
    invalidate();
  }

  public int getBaselineColor() {
    return paint.getColor();
  }

  public float getBaselineSpacing() {
    return baselineSpacing;
  }

  public void setBaselineSpacing(float baselineSpacing) {
    this.baselineSpacing = baselineSpacing;
    invalidate();
  }

  public int getBaselineType() {
    return baselineType;
  }

  public void setBaselineType(int baselineType) {
    this.baselineType = baselineType;
    invalidate();
  }
}
