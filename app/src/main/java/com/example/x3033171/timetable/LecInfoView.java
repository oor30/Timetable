package com.example.x3033171.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class LecInfoView extends ConstraintLayout {

    TextView lecName;

    public LecInfoView(Context context) {
        super(context);
        init(context);
    }

    public LecInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LecInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.lec_info_view, this);
        lecName = findViewById(R.id.lecName);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setLecName(String lecName) {
        this.lecName.setText(lecName);
    }
}
