package com.example.x3033171.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.text.Normalizer;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Lecture[][] lectures;
    Lecture lec11, lec12, lec13, lec14, lec15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lectures = new Lecture[5][5];
        lectures[0][0] = findViewById(R.id.lec11);
        lectures[0][1] = findViewById(R.id.lec12);
        lectures[0][2] = findViewById(R.id.lec13);
        lectures[0][3] = findViewById(R.id.lec14);
        lectures[0][4] = findViewById(R.id.lec15);
        lectures[1][0] = findViewById(R.id.lec21);
        lectures[1][1] = findViewById(R.id.lec22);
        lectures[1][2] = findViewById(R.id.lec23);
        lectures[1][3] = findViewById(R.id.lec24);
        lectures[1][4] = findViewById(R.id.lec25);
        lectures[2][0] = findViewById(R.id.lec31);
        lectures[2][1] = findViewById(R.id.lec32);
        lectures[2][2] = findViewById(R.id.lec33);
        lectures[2][3] = findViewById(R.id.lec34);
        lectures[2][4] = findViewById(R.id.lec35);
        lectures[3][0] = findViewById(R.id.lec41);
        lectures[3][1] = findViewById(R.id.lec42);
        lectures[3][2] = findViewById(R.id.lec43);
        lectures[3][3] = findViewById(R.id.lec44);
        lectures[3][4] = findViewById(R.id.lec45);
        lectures[4][0] = findViewById(R.id.lec51);
        lectures[4][1] = findViewById(R.id.lec52);
        lectures[4][2] = findViewById(R.id.lec53);
        lectures[4][3] = findViewById(R.id.lec54);
        lectures[4][4] = findViewById(R.id.lec55);

        db = FirebaseFirestore.getInstance();
        Map<String, String> map = new HashMap<>();
//        map.put("科目区分", "コース科目");
        map.put("科目分類", "情報コース、電気電子・情報工学科");    // or検索
        map.put("対象学年", "２年生");
        // or検索は１項目のみ
        searchLecture(db, map);
    }

    void searchLecture(FirebaseFirestore db, Map<String, String> map) {
        CollectionReference cr = db.collection("lectures");
        Query query = null;
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
        assert query != null;
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    logData(task);
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }

    void logData(Task<QuerySnapshot> task) {
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