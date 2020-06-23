package com.example.x3033171.timetable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.Normalizer;

public class Result extends ConstraintLayout {

    String name, lecClass, lecCode;
    int week, period, grade;
    TextView resName, resWeek, resPeriod;
    CheckBox checkBox;
    boolean checked;

    public Result(Context context) {
        super(context);
        init(context);
    }

    public Result(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Result(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Result(Context context, QueryDocumentSnapshot document) {
        super(context);
        init(context);

        name = document.getData().get("授業科目名").toString();
        lecClass = document.getData().get("科目分類").toString();
        lecCode = document.getData().get("履修コード").toString();
        String weekStr = document.get("timeinfo.0.week").toString().substring(0,1);
        String periodStr = document.get("timeinfo.0.period").toString().substring(0,1);
        String gradeStr = document.get("対象学年").toString().substring(0,1);
        resName.setText(name);
        resWeek.setText(weekStr);
        resPeriod.setText(periodStr);

        week = Integer.parseInt(weekStr.replace('月', '1').replace('火', '2')
        .replace('水', '3').replace('木', '4').replace('金', '5'));
        period = Integer.parseInt(Normalizer.normalize(periodStr, Normalizer.Form.NFKC));
        grade = Integer.parseInt(Normalizer.normalize(gradeStr, Normalizer.Form.NFKC));
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
}
