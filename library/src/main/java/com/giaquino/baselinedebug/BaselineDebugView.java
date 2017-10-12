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

  private final float ONE_DIP;
  private final float FOUR_DIP;
  private final Paint paint;

  public BaselineDebugView(Context context) {
    this(context, null);
  }

  public BaselineDebugView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BaselineDebugView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaselineDebugView);
    int color = a.getColor(R.styleable.BaselineDebugView_baselineColor, Color.MAGENTA);
    a.recycle();

    ONE_DIP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
        getResources().getDisplayMetrics());
    FOUR_DIP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
        getResources().getDisplayMetrics());

    paint = new Paint();
    paint.setColor(color);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(ONE_DIP);
    paint.setStyle(Paint.Style.STROKE);
  }

  @Override public void draw(Canvas canvas) {
    super.draw(canvas);
    float y = FOUR_DIP;
    while (y < getHeight()) {
      canvas.drawLine(0, y, getWidth(), y, paint);
      y += FOUR_DIP;
    }
  }

  public void setColor(int color) {
    paint.setColor(color);
    invalidate();
  }

  public int getColor() {
    return paint.getColor();
  }
}
