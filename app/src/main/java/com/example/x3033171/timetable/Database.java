package com.example.x3033171.timetable;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    FirebaseFirestore db;
    Lecture[][] lectures;
    SearchLecture sl;

    Database(Lecture[][] l) {
        lectures = l;
        db = FirebaseFirestore.getInstance();
//        Map<String, String> map = new HashMap<>();
////        map.put("科目区分", "コース科目");
//        map.put("科目分類", "情報コース、電気電子・情報工学科");    // or検索
//        map.put("対象学年", "２年生");
//        // or検索は１項目のみ
//        queryLecture(db, map, 0);
    }

    Database(SearchLecture sl) {
        db = FirebaseFirestore.getInstance();
        this.sl = sl;
    }

    public void searchLecture(Map<String, String> map, int mode) {
        db = FirebaseFirestore.getInstance();
        queryLecture(db, map, mode);
    }

    private void queryLecture(FirebaseFirestore db, Map<String, String> map, final int mode) {
        CollectionReference cr = db.collection("lectures");
        Query query = cr;
        int cnt = 0;
        String[] timeinfoKeys = {"semester", "week", "period", "room"};
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (Arrays.asList(timeinfoKeys).contains(entry.getKey())) {
                key = "timeinfo.0." + key;
            }
            List<String> values = Arrays.asList(value.split("、"));
            if (values.size() > 1) {
                if (cnt == 0) {
                    query = cr.whereIn(key, values);
                } else if (cnt > 0) {
                    query = query.whereIn(key, values);
                }
            } else {
                if (cnt == 0) {
                    query = cr.whereEqualTo(key, value);
                } else if (cnt > 0) {
                    query = query.whereEqualTo(key, value);
                }
            }
            cnt++;
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (mode == 0) {
                        logData(task);
                    } else if (mode == 1) {
                        sl.makeResults(task);
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }

    private void logData(Task<QuerySnapshot> task) {
        for (QueryDocumentSnapshot document : task.getResult()) {
            String name = document.getData().get("授業科目名").toString();
            String timeinfo = document.getData().get("timeinfo").toString();
            int inMap = 0;
            ArrayList<String> timeinfoArrayStr = new ArrayList<>();
            int startIndex = 1;
            for (int i=0; i<timeinfo.length(); i++) {
                char c = timeinfo.charAt(i);
                if (c == '{'){
                    inMap++;
                    startIndex = i;
                }
                else if (c == '}') inMap--;
                if (inMap == 1) {
                    if (c == '}'){
                        timeinfoArrayStr.add(timeinfo.substring(startIndex, i+1));
                    }
                }
            }

            ArrayList<Map<String, String>> timeinfoArray = new ArrayList<>();
            for (String str : timeinfoArrayStr) {
                String[] keyValues = str.split(", ");
                Map<String, String> timeinfoMap = new HashMap<>();
                for (String keyValue : keyValues) {
                    String[] keyAndValue = keyValue.replace("{", "").replace("}", "").split("=");
                    String key = keyAndValue[0];
                    String value = keyAndValue[1];
                    timeinfoMap.put(key, value);
                }
                timeinfoArray.add(timeinfoMap);
            }

            ArrayList<Map<String, Object>> convertedTimeinfoArray = convertBin(timeinfoArray);
            Log.d("情報コース", name + convertedTimeinfoArray);

            setLecture(convertedTimeinfoArray, name);
        }
    }

    private ArrayList<Map<String, Object>> convertBin(ArrayList<Map<String, String>> strArray) {
        ArrayList<Map<String, Object>> convertedArray = new ArrayList<>();

        Map<String, Object> convertMap = new HashMap<>();
        convertMap.put("１時限", 1);
        convertMap.put("２時限", 2);
        convertMap.put("３時限", 3);
        convertMap.put("４時限", 4);
        convertMap.put("５時限", 5);
        convertMap.put("月曜日", 1);
        convertMap.put("火曜日", 2);
        convertMap.put("水曜日", 3);
        convertMap.put("木曜日", 4);
        convertMap.put("金曜日", 5);
        convertMap.put("前学期", 1);
        convertMap.put("後学期", 2);

        for (Map<String, String> map: strArray) {
            Map<String, Object> convertedMap = new HashMap<>();
            String period = map.get("period");
            String week = map.get("week");
            String semester = map.get("semester");
            convertedMap.put("period", convertMap.get(period));
            convertedMap.put("week", convertMap.get(week));
            convertedMap.put("semester", convertMap.get(semester));
            convertedMap.put("room", map.get("room"));
            convertedArray.add(convertedMap);
        }

        return convertedArray;
    }

    private void setLecture(ArrayList<Map<String, Object>> array, String name) {
        for (Map<String, Object> map : array) {
            int period = Integer.parseInt(String.valueOf(map.get("period")));
            int week = Integer.parseInt(String.valueOf(map.get("week")));
            String room = String.valueOf(map.get("room"));
            room = Normalizer.normalize(room, Normalizer.Form.NFKC);
            Lecture lec = lectures[week-1][period-1];
            lec.setLecName(cutParenthesis(name));
            lec.setLecRoom(room);
        }
    }

    private String cutParenthesis(String name) {
        String[] nameSlited = name.split("（");
        return nameSlited[0];
    }

}
