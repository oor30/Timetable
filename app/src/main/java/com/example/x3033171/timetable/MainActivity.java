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

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        Map<String, String> map = new HashMap<>();
//        map.put("科目区分", "コース科目");
        map.put("科目分類", "情報コース、電気電子・情報工学科");    // or検索
        map.put("対象学年", "３年生");
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
                for (String keyValue : keyValues) {
                    Map<String, String> timeinfoMap = new HashMap<>();
                    String[] keyAndValue = keyValue.split("=");
                    String key = keyAndValue[0];
                    String value = keyAndValue[1];
                    timeinfoMap.put(key, value);
                    timeinfoArray.add(timeinfoMap);
                }
            }
            Log.d("情報コース", name + timeinfo);
        }
    }
}