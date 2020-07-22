package com.example.x3033171.timetable;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Set;

import static android.content.ContentValues.TAG;

public class Database {
    FirebaseFirestore db;
    SearchLectureActivity sl;
    WebViewActivity wv;

    Database(SearchLectureActivity sl) {
        db = FirebaseFirestore.getInstance();
        this.sl = sl;
    }

    Database(WebViewActivity wv) {
        db = FirebaseFirestore.getInstance();
        this.wv = wv;
    }

    public void searchLecture(Set<String> lecCodes) {
        CollectionReference cr = db.collection("lectures5");
        Query query = cr;
        Log.d(TAG, "searchLecture: " + lecCodes);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    wv.writePref(task);
                }
            }
        });
    }

    public void searchLecture(String faculty) {
        CollectionReference cr = db.collection("lectures4");
        cr = cr.document(faculty).collection(faculty + "_lectures");
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Database", "タスク完了");
                    sl.makeResults(task);
                }
            }
        });
//        sl.makeResults(cr.get());
//        Query query = cr;
//        int cnt = 0;
//        String[] timeinfoKeys = {"semester", "week", "period", "room"};
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            if (Arrays.asList(timeinfoKeys).contains(entry.getKey())) {
//                key = "timeinfo.0." + key;
//            }
//            List<String> values = Arrays.asList(value.split("、"));
//            if (values.size() > 1) {
//                if (cnt == 0) {
//                    query = cr.whereIn(key, values);
//                } else if (cnt > 0) {
//                    query = query.whereIn(key, values);
//                }
//            } else {
//                if (cnt == 0) {
//                    query = cr.whereEqualTo(key, value);
//                } else if (cnt > 0) {
//                    query = query.whereEqualTo(key, value);
//                }
//            }
//            cnt++;
//        }
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    sl.makeResults(task);
//                } else {
//                    Log.w("TAG", "Error getting documents.", task.getException());
//                }
//            }
//        });
    }
}
