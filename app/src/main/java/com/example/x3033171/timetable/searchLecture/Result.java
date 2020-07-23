package com.example.x3033171.timetable.searchLecture;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

public class Result {

    private String name, lecClass, lecCode;
    private int grade;
    private TreeSet<Integer> weeks, periods;
    private ArrayList<String> rooms;
    private boolean checked;
    private Map<String, Object> resultMap;

    public Result(QueryDocumentSnapshot document) {
        try {
            resultMap = document.getData();
            name = String.valueOf(resultMap.get("授業科目名"));      // nullなら"null"が代入される
            lecClass = String.valueOf(resultMap.get("科目分類"));
            lecCode = String.valueOf(resultMap.get("履修コード"));
            Map<String, Map<String, Object>> timeinfo = (Map<String, Map<String, Object>>) resultMap.get("timeinfo");
            checked = false;

            weeks = new TreeSet<>();
            periods = new TreeSet<>();
            rooms = new ArrayList<>();
            if (timeinfo != null) {
                for (Map<String, Object> map : timeinfo.values()) {

                    String weekTmp = String.valueOf(map.get("week"));
                    int week;
                    if (!weekTmp.equals("null")) week = Integer.parseInt(weekTmp);
                    else week = 0;
                    weeks.add(week);

                    String periodTmp = String.valueOf(map.get("period"));
                    int period;
                    if (!periodTmp.equals("null")) period = Integer.parseInt(periodTmp);
                    else period = 0;
                    periods.add(period);

                    String roomTmp = String.valueOf(map.get("room"));
                    if (rooms.contains(roomTmp) && !roomTmp.equals("null")) rooms.add(roomTmp);
                }
            }
            if (weeks.size() == 0) {
                weeks.add(0);
            }
            if (periods.size() == 0) {
                periods.add(0);
            }
            String gradeTmp = String.valueOf(document.get("grade"));
            try {
                grade = Integer.parseInt(gradeTmp);
            } catch (NullPointerException e) {
                grade = 0;
            }
        } catch (Exception e) {
            Log.d("Result", name);
            Log.e("Result", e.toString());
        }
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

    @NonNull
    public TreeSet<Integer> getWeeks() {
        return weeks;
    }

    @NonNull
    public TreeSet<Integer> getPeriods() {
        return periods;
    }

    @NonNull
    public ArrayList<String> getRooms() {
        return rooms;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked (boolean b) {
        checked = b;
    }

    public Map<String, Object> getResultMap () {
        return resultMap;
    }
}
