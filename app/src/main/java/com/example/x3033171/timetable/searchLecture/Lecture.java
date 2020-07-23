package com.example.x3033171.timetable.searchLecture;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.x3033171.timetable.R;

import java.util.ArrayList;
import java.util.Map;

public class Lecture extends ConstraintLayout implements View.OnClickListener {
    TextView lecName, lecRoom;
    String lecCode, grade, teacher;
    ArrayList<String> rooms;
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
        rooms = new ArrayList<>();
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

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public String getLecName() {
        return lecName.getText().toString();
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setLecInfo(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
        lecName.setText(String.valueOf(resultMap.get("授業科目名")).split("（")[0]);
        lecCode = String.valueOf(resultMap.get("履修コード"));
        Map<String, Map<String, Object>> timeinfo = (Map<String, Map<String, Object>>) resultMap.get("timeinfo");
        if (timeinfo != null) {
            for (Map<String, Object> map : timeinfo.values()) {
                String roomTmp = String.valueOf(map.get("room"));
                if (!roomTmp.equals("null")) rooms.add(roomTmp);
            }
        }
        if (!rooms.isEmpty()) lecRoom.setText(rooms.get(0));
        grade = String.valueOf(resultMap.get("対象学年"));
        teacher = String.valueOf(resultMap.get("担当教員"));
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
