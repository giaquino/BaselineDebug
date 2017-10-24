package com.giaquino.baselinedebug.sample.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.giaquino.baselinedebug.sample.R;
import java.util.ArrayList;
import java.util.List;

/**
 * An extension of {@link BaselineGridTextView} which add a leading space appropriate for
 * the current text size base on material design guidelines.
 *
 * If the next view on the hierarchy after this is a {@link TextView} it will compute the
 * leading space taking the text size and padding in to consideration to achieve the
 * appropriate space.
 *
 * https://material.io/guidelines/style/typography.html#typography-line-height
 */
public class LeadingTextView extends BaselineGridTextView
    implements View.OnAttachStateChangeListener {

  public static int LINE_SPEC_TYPE_A = 1;
  public static int LINE_SPEC_TYPE_B = 2;

  private final List<LineSpec> lineSpecA = new ArrayList<>();
  private final List<LineSpec> lineSpecB = new ArrayList<>();

  private TextView nextTextView;

  private float leading;
  private int extraBottomPadding;
  private int lineSpecType = LINE_SPEC_TYPE_A;

  public LeadingTextView(Context context) {
    this(context, null);
  }

  public LeadingTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LeadingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeadingTextView);
    lineSpecType = a.getInt(R.styleable.LeadingTextView_lineSpecType, LINE_SPEC_TYPE_A);
    a.recycle();

    lineSpecA.add(new LineSpec(1, getDimensionSP(45), getDimensionDP(48))); // Display 2
    lineSpecA.add(new LineSpec(1, getDimensionSP(34), getDimensionDP(40))); // Display 1
    lineSpecA.add(new LineSpec(1, getDimensionSP(24), getDimensionDP(32))); // Headline
    lineSpecA.add(new LineSpec(1, getDimensionSP(15), getDimensionDP(24))); // Subheading 1
    lineSpecA.add(new LineSpec(1, getDimensionSP(16), getDimensionDP(24))); // Subheading 1
    lineSpecA.add(new LineSpec(1, getDimensionSP(13), getDimensionDP(20))); // Body 1
    lineSpecA.add(new LineSpec(1, getDimensionSP(14), getDimensionDP(20))); // Body 1

    lineSpecB.add(new LineSpec(2, getDimensionSP(15), getDimensionDP(28))); // Subheading 2
    lineSpecB.add(new LineSpec(2, getDimensionSP(16), getDimensionDP(28))); // Subheading 2
    lineSpecB.add(new LineSpec(2, getDimensionSP(13), getDimensionDP(24))); // Body 2
    lineSpecB.add(new LineSpec(2, getDimensionSP(14), getDimensionDP(24))); // Body 2

    leading = computeLeadingSpace();
    setLineHeightHint(leading);
  }

  @Override public void onViewAttachedToWindow(View v) {
  }

  @Override public void onViewDetachedFromWindow(View v) {
    nextTextView.removeOnAttachStateChangeListener(this);
    nextTextView = null;
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    /* Check if the next view is a TextView */
    ViewGroup parent = (ViewGroup) getParent();
    final int index = parent.indexOfChild(this) + 1;
    if (index < parent.getChildCount()) {
      View child = parent.getChildAt(index);
      if (child instanceof TextView && !(child instanceof Button) && !(child instanceof EditText)) {
        nextTextView = (TextView) child;
        nextTextView.addOnAttachStateChangeListener(this);
      }
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (nextTextView != null) {
      nextTextView.removeOnAttachStateChangeListener(this);
      nextTextView = null;
    }
  }

  @Override public void setTextSize(float size) {
    super.setTextSize(size);
    leading = computeLeadingSpace();
    setLineHeightHint(leading);
  }

  @Override public int getCompoundPaddingBottom() {
    return super.getCompoundPaddingBottom() + extraBottomPadding;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    leading = computeLeadingSpace();
    leading -= getBottomSpacingFromBaseline(this);
    if (nextTextView != null) {
      leading -= getTopSpacingFromBaseline(nextTextView);
    }
    extraBottomPadding = Math.max(0, Math.round(leading));
    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + extraBottomPadding);
  }

  private float getDimensionSP(int dip) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dip,
        getResources().getDisplayMetrics());
  }

  private float getDimensionDP(int dip) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
        getResources().getDisplayMetrics());
  }

  /**
   * Compute the leading space base on text size or 0 if not found.
   */
  private float computeLeadingSpace() {
    LineSpec nearest = null;
    if (lineSpecType == LINE_SPEC_TYPE_B) {
      nearest = getExactLineSpec(lineSpecB, getTextSize());
    }
    if (nearest == null) {
      nearest = getExactLineSpec(lineSpecA, getTextSize());
    }
    return nearest != null ? nearest.leading : 0;
  }

  /**
   * Get line spec for the given size
   */
  private LineSpec getExactLineSpec(List<LineSpec> specs, float size) {
    for (LineSpec spec : specs) {
      if (spec.size == size) return spec;
    }
    return null;
  }

  /**
   * Returns the spacing from the last line baseline to the view's bottom
   */
  private static float getBottomSpacingFromBaseline(TextView view) {
    Paint.FontMetrics fm = view.getPaint().getFontMetrics();
    return fm.bottom + fm.leading + view.getCompoundPaddingBottom();
  }

  /**
   * Returns the spacing from the first line baseline to the view's top
   */
  private static float getTopSpacingFromBaseline(TextView view) {
    Paint.FontMetrics fm = view.getPaint().getFontMetrics();
    return Math.abs(fm.ascent - fm.descent) + view.getCompoundPaddingTop();
  }

  private static class LineSpec {
    /**
     * Either 1 or 2 for A/B
     */
    final int type;
    /**
     * The size of the text in SP
     */
    final float size;
    /**
     * The leading size for the given text size in DP
     */
    final float leading;

    public LineSpec(int type, float size, float leading) {
      this.type = type;
      this.size = size;
      this.leading = leading;
    }
  }
}
