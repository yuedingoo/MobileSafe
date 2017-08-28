package com.yueding.mobilesafe.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by yueding on 2017/8/22.
 */

public class FocusTextView extends android.support.v7.widget.AppCompatTextView {
    public FocusTextView(Context context) {
        super(context);
    }

    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
