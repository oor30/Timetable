package com.example.x3033171.timetable.searchLecture;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Model {
    @NonNull
    String week = "null", title, name = "null", grade = "0";
    String lecCode;
    @NotNull
    Set<Integer> weeks = new TreeSet<>(), periods = new TreeSet<>();
    boolean checked = false;

    @NonNull
    public String getWeek() {
        return week;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getGrade() {
        return grade;
    }

    @NotNull
    public Integer getType() {
        return type;
    }

    @NotNull
    Integer type;

    public Model(@NotNull String week) {
        if (!week.isEmpty()) {
            this.week = week;
        }
        title = week;
        type = ItemType.Header;
    }

    public Model(@NotNull Result result) {
        week = String.valueOf(result.getWeeks().iterator().next());
        weeks = result.getWeeks();
        periods = result.getPeriods();
        name = result.getName();
        title = result.getLecCode();
        grade = result.getGrade() + "年";
        checked = result.getChecked();
        type = ItemType.Post;
    }

    public Model(Map<String, Object> resultMap, boolean checked) {
        name = String.valueOf(resultMap.get("授業科目名"));
        Map<String, Map<String, Object>> timeinfo = (Map<String, Map<String, Object>>) resultMap.get("timeinfo");
        if (timeinfo != null) {
            for (Map<String, Object> map : timeinfo.values()) {

                String weekTmp = String.valueOf(map.get("week"));
                int week;
                if (!weekTmp.equals("null")) week = (int) Double.parseDouble(weekTmp);
                else week = 0;
                weeks.add(week);

                String periodTmp = String.valueOf(map.get("period"));
                int period;
                if (!periodTmp.equals("null")) period = (int) Double.parseDouble(periodTmp);
                else period = 0;
                periods.add(period);
            }
        }
        if (weeks.size() == 0) {
            weeks.add(0);
        }
        if (periods.size() == 0) {
            periods.add(0);
        }
        lecCode = String.valueOf(resultMap.get("履修コード"));
        this.checked = checked;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Set<Integer> getWeeks() {
        return weeks;
    }

    @NotNull
    public Set<Integer> getPeriods() {
        return periods;
    }

    public String getLecCode() {
        return lecCode;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}