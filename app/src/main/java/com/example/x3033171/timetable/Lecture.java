package com.example.x3033171.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Lecture extends ConstraintLayout implements View.OnClickListener {
    TextView lecName, lecRoom;
    String lecCode, grade, teacher;
    QueryDocumentSnapshot document;
    boolean isEmpty;

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
        lecName = layout.findViewById(R.id.lecName);
        lecRoom = layout.findViewById(R.id.lecRoom);
        layout.setOnClickListener(this);
        isEmpty = true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.lecLayout) {

        }
    }

    public String getLecName() {
        return lecName.getText().toString();
    }

    public String getTeacher() {
        return teacher;
    }

    public String getLecCode() {
        return lecCode;
    }

    public String getLecRoom() {
        return lecRoom.getText().toString();
    }

    public String getGrade() {
        return grade;
    }

    public void setLecName(String str) {
        lecName.setText(str);
    }

    public void setLecCode(String str) {
        lecCode = str;
    }

    public void setLecRoom(String str) {
        lecRoom.setText(str);
    }

    public void setDocument(QueryDocumentSnapshot document) {
        grade = document.getData().get("対象学年").toString();
        teacher = document.getData().get("担当教員").toString();
        isEmpty = false;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
