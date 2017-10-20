package com.giaquino.baselinedebug.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import com.giaquino.baselinedebug.BaselineDebug;

public class SampleActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sample_activity);
    findViewById(R.id.btn_baseline_type).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (BaselineDebug.getBaselineDebugView() != null) {
          int type = BaselineDebug.getBaselineDebugView().getBaselineType();
          BaselineDebug.getBaselineDebugView().setBaselineType((type + 1) % 3);
        }
      }
    });
    findViewById(R.id.btn_baseline_toggle).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!BaselineDebug.isShowing()) {
          BaselineDebug.show(getApplication());
        } else {
          BaselineDebug.hide(getApplication());
        }
      }
    });
    final EditText color = findViewById(R.id.input_baseline_color);
    color.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(Editable s) {
        if (!s.toString().startsWith("#")) s.insert(0, "#");
        try {
          int color = Color.parseColor(s.toString());
          if (BaselineDebug.getBaselineDebugView() != null) {
            BaselineDebug.getBaselineDebugView().setBaselineColor(color);
          }
        } catch (Exception ignore) {
        }
      }
    });
    final EditText spacing = findViewById(R.id.input_baseline_spacing);
    spacing.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(Editable s) {
        if (BaselineDebug.getBaselineDebugView() != null) {
          try {
            float spacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                Float.parseFloat(s.toString()), getResources().getDisplayMetrics());
            if (spacing <= 1) return;
            BaselineDebug.getBaselineDebugView().setBaselineSpacing(spacing);
          } catch (Exception ignored) {
          }
        }
      }
    });
  }
}
