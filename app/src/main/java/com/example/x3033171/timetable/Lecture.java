package com.example.x3033171.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Lecture extends ConstraintLayout implements View.OnClickListener {
    TextView lecName, lecRoom;

    public Lecture(Context context) {
        super(context);
        init(context);
    }

    public Lecture(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Lecture(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.lecture_layout, this);
        lecName = (TextView)layout.findViewById(R.id.lecName);
        lecRoom = (TextView)layout.findViewById(R.id.lecRoom);
        layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.lecLayout) {

        }
    }

    public String getLecName() {
        return lecName.getText().toString();
    }

    public String getLecRoom() {
        return lecRoom.toString();
    }

    public void setLecName(String str) {
        lecName.setText(str);
    }

    public void setLecRoom(String str) {
        lecRoom.setText(str);
    }
}
