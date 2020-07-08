package com.example.x3033171.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Map;

public class Lecture extends ConstraintLayout implements View.OnClickListener {
    TextView lecName, lecRoom;
    String lecCode, grade, teacher;
    boolean isEmpty;
    Map<String, Object> resultMap;

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
        lecName = layout.findViewById(R.id.lecNamename);
        lecRoom = layout.findViewById(R.id.lecRoom);
        layout.setOnClickListener(this);
        isEmpty = true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.lecLayout) {

        }
    }

    public String getLecCode() {
        return lecCode;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setLecInfo(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
        lecName.setText(resultMap.get("授業科目名").toString().split("（")[0]);
        lecCode = resultMap.get("履修コード").toString();
        Map<String, Object> timeinfo = (Map<String, Object>) resultMap.get("timeinfo");
        Map<String, Object> timeinfo0 = (Map<String, Object>) timeinfo.get("0");
        lecRoom.setText(timeinfo0.get("room").toString());
        grade = resultMap.get("対象学年").toString();
        teacher = resultMap.get("担当教員").toString();
        isEmpty = false;
    }

    public void reset() {
        resultMap = null;
        lecName.setText("");
        lecCode = null;
        lecRoom.setText("");
        grade = null;
        teacher = null;
        isEmpty = true;
    }
}
