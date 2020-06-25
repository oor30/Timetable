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

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

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

    public void setting(String lecCode) {
        Map<String, String> map = new HashMap<>();
        map.put("履修コード", lecCode);

        Database database = new Database(this);
        database.searchLecture(map, 2);
    }

    public void writeInfo(Task<QuerySnapshot> task) {
        for (QueryDocumentSnapshot document : task.getResult()) {
            String n = document.getData().get("授業科目名").toString();
            String t = document.getData().get("担当教員").toString();
            lecName.setText(n);
            teacher.setText(t);
        }
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
