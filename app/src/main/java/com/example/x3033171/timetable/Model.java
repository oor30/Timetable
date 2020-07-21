package com.example.x3033171.timetable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;

public class Model {
    @NonNull
    String week = "null", title, name = "null", grade = "0";
    @NotNull
    Set<Integer> weeks = new TreeSet<>(), periods = new TreeSet<>();
    boolean checked = false;
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

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setWeek(@NotNull String week) {
        this.week = week;
    }

    @NotNull
    public Set<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(@NotNull Set<Integer> weeks) {
        this.weeks = weeks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @NotNull
    public Set<Integer> getPeriods() {
        return periods;
    }

    public void setPeriods(@NotNull Set<Integer> periods) {
        this.periods = periods;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @NotNull
    public Integer getType() {
        return type;
    }

    public void setType(@NotNull Integer type) {
        this.type = type;
    }

    @NotNull
    public String getWeek() {
        return week;
    }


}