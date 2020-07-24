package com.example.x3033171.timetable;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public interface TodoDatabase {

    default void upTodo(Map<String, String> todoMap, String lecCode) {
        Map<String, Map<String, String>> map = new HashMap<>();
        String id = todoMap.get("title") + todoMap.get("date");
        map.put(id, todoMap);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("todo");
        cr.document(lecCode)
                .set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    default void subsSnapshotListener(final Context context, String lecCode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("todo").document(lecCode)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }

                        if (value != null && value.exists()) {
                            Map<String, Object> map = value.getData();
                            for (Object o : map.values()) {
                                Map<String, String> todoMap = ((Map<String, String>) o);
                                OnGetTodoMap(todoMap);
                            }
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
    }

    default void downTodo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("todo");
    }

    void OnGetTodoMap(Map<String, String> todoMap);
}
