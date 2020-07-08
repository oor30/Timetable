package com.example.x3033171.timetable;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Result extends ConstraintLayout {

    String name, lecClass, lecCode;
    int grade;
    ArrayList<Integer> weeks, periods;
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
        name = resultMap.get("授業科目名").toString();
        lecClass = resultMap.get("科目分類").toString();
        lecCode = resultMap.get("履修コード").toString();
        Map<String, Map<String, String>> timeinfo = (Map<String, Map<String, String>>) resultMap.get("timeinfo");

        weeks = new ArrayList<>();
        periods = new ArrayList<>();
        for (Map<String, String> map : timeinfo.values()) {
            int week = Integer.parseInt(map.get("week"));
            if (!weeks.contains(week)) {
                weeks.add(week);
            }

            int period = Integer.parseInt(map.get("period"));
            if (!periods.contains(period)) {
                periods.add(period);
            }
        }
//        period = Integer.parseInt(timeinfo.get("0").get("period"));
        grade = Integer.parseInt(document.get("grade").toString());
        resName.setText(name);
        resWeek.setText(weeks.toString().replace("1", "月").replace("2", "火")
        .replace("3", "水").replace("4", "木").replace("5", "金")
        .replace("[", "").replace("]", ""));
        resPeriod.setText(periods.toString().replace("[", "").replace("]", ""));
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
                if (checked) {
                    setBackgroundColor(Color.parseColor("#FFEEEE"));
                } else {
                    setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
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

    public ArrayList<Integer> getWeeks() {
        return weeks;
    }

    public ArrayList<Integer> getPeriods() {
        return periods;
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
