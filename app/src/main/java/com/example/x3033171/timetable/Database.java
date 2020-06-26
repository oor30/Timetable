package com.example.x3033171.timetable;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Database {
    FirebaseFirestore db;
    SearchLecture sl;

    Database(SearchLecture sl) {
        db = FirebaseFirestore.getInstance();
        this.sl = sl;
    }

    public void searchLecture(Map<String, String> map) {
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
                    sl.makeResults(task);
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }
}
