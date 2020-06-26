package com.example.x3033171.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class Result extends ConstraintLayout {

    String name, lecClass, lecCode;
    int week, period, grade;
    TextView resName, resWeek, resPeriod;
    CheckBox checkBox;
    boolean checked;
    Map<String, Object> resultMap;

    public Result(Context context) {
        super(context);
        init(context);
    }

    public Result(Context context, QueryDocumentSnapshot document) {
        super(context);
        init(context);

        resultMap = document.getData();
        name = document.getData().get("授業科目名").toString();
        lecClass = document.getData().get("科目分類").toString();
        lecCode = document.getData().get("履修コード").toString();
        week = Integer.parseInt(document.get("timeinfo.0.week").toString());
        period = Integer.parseInt(document.get("timeinfo.0.period").toString());
        grade = Integer.parseInt(document.get("grade").toString());
        resName.setText(name);
        resWeek.setText(String.valueOf(week).replace("1", "月").replace("2", "火")
        .replace("3", "水").replace("4", "木").replace("5", "金"));
        resPeriod.setText(String.valueOf(period));
    }

    void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.result_layout, this);
        resName = findViewById(R.id.resName);
        resWeek = findViewById(R.id.resWeek);
        resPeriod = findViewById(R.id.resPeriod);
        checked = false;
        checkBox = findViewById(R.id.addLecCB);
        checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = checkBox.isChecked();
            }
        });
    }

    public String getName() {
        return name;
    }

    public String getLecClass() {
        return lecClass;
    }

    public String getLecCode() {
        return lecCode;
    }

    public int getGrade() {
        return grade;
    }

    public int getWeek() {
        return week;
    }

    public int getPeriod() {
        return period;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked (boolean b) {
        checked = b;
        checkBox.setChecked(b);
    }

    public Map<String, Object> getResultMap () {
        return resultMap;
    }
}
