package com.example.x3033171.timetable.Todo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TodoData {
    private String date, time, name, title, lecCode;
    private boolean allDay;

    private Calendar calendar;

    public TodoData(Map<String, String> map) {
        String dateTmp = map.get("date");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

        String isAllDayTmp = map.get("isAllDay");
        allDay = false;
        if (isAllDayTmp != null) {
            allDay = Boolean.parseBoolean(isAllDayTmp);
        }
        calendar = Calendar.getInstance();
        if (dateTmp != null) {
            try {
                calendar.setTime(Objects.requireNonNull(format.parse(dateTmp)));
                date = String.valueOf(calendar.get(Calendar.DATE));
                time = (calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        name = map.get("name");
        title = map.get("title");
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }


}
