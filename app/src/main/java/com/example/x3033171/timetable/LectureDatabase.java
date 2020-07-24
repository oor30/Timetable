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

public interface LectureDatabase {

    default void searchLecture(Set<String> lecCodes) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("lectures5");
        Query query = cr;
        Log.d(TAG, "searchLecture: " + lecCodes);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    makeResults(task);
                }
            }
        });
    }

    default void searchLecture(String faculty) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("lectures4");
        cr = cr.document(faculty).collection(faculty + "_lectures");
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Database", "タスク完了");
                    makeResults(task);
                }
            }
        });
    }

    void makeResults(@NonNull Task<QuerySnapshot> task);
}
