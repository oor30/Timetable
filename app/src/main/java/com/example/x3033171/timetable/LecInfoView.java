package com.example.x3033171.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.constraintlayout.widget.ConstraintLayout;

public class LecInfoView extends ConstraintLayout {

    TextView lecName, teacher, room, text, grade;
    Button button;
    ViewFlipper flipper;
    Animation rightIn, leftOut;
    Context context;

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
        teacher = findViewById(R.id.teacher);
        room = findViewById(R.id.room);
        text = findViewById(R.id.text);
        grade = findViewById(R.id.grade);

        rightIn = AnimationUtils.loadAnimation(context, R.anim.right_in);
        leftOut = AnimationUtils.loadAnimation(context, R.anim.left_out);


        flipper = findViewById(R.id.viewFlipper);
        button = findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flipper.setInAnimation(rightIn);
                flipper.setOutAnimation(leftOut);
                flipper.showNext();
            }
        });
        setFocusable(true);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setLecture(Lecture lecture) {
        lecName.setText(lecture.getLecName());
        teacher.setText(lecture.getTeacher());
        room.setText(lecture.getLecRoom());
        grade.setText(lecture.getGrade());
    }

    public void setContent(Context context) {
        this.context = context;
    }
}
