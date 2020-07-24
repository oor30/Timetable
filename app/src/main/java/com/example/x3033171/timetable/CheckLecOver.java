package com.example.x3033171.timetable;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;

public interface CheckLecOver {

    @NotNull
    default Set<String> checkLecOver(ArrayList<Map<String, Object>> resultMaps) {
        Map<String, Set<String>> weekPeriod_lecCode = new HashMap<>();
        for (Map<String, Object> resultMap : resultMaps) {
            String lecCode = String.valueOf(resultMap.get("履修コード"));
            Map<String, Map<String, Object>> timeinfo = (Map<String, Map<String, Object>>) resultMap.get("timeinfo");
            if (timeinfo != null) {
                for (Map<String, Object> value : timeinfo.values()) {
                    String week = String.valueOf(value.get("week"));
                    String period = String.valueOf(value.get("period"));
                    String weekPeriod = week + period;
                    Log.d(TAG, "checkLecOver: " + weekPeriod);
                    if (week.equals("null") && period.equals("null")) {
                        if (weekPeriod_lecCode.containsKey(weekPeriod)) {
                            Log.d(TAG, "checkLecOver: " + lecCode);
                            weekPeriod_lecCode.get(weekPeriod).add(lecCode);
                        } else {
                            weekPeriod_lecCode.put(weekPeriod, new HashSet<>(Collections.singletonList(lecCode)));
                        }
                    }
                }
            }
        }

        Set<String> overLecCodes = new HashSet<>();
        for (Set<String> value : weekPeriod_lecCode.values()) {
            if (value.size() > 1) {
                overLecCodes.addAll(value);
            }
        }
        return overLecCodes;
    }
}
