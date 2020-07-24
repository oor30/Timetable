//package com.example.x3033171.timetable;
//
//import android.content.Context;
//import android.util.Log;
//import android.webkit.WebView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.example.x3033171.timetable.searchLecture.SearchLectureActivity;
//import com.example.x3033171.timetable.webView.WebViewActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.Query;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.SetOptions;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//import static android.content.ContentValues.TAG;
//
//public class Database implements TodoPref {
//    FirebaseFirestore db;
//    SearchLectureActivity sl;
//    WebViewActivity wv;
//
//    public Database(SearchLectureActivity sl) {
//        db = FirebaseFirestore.getInstance();
//        this.sl = sl;
//    }
//
//    public Database(WebViewActivity wv) {
//        db = FirebaseFirestore.getInstance();
//        this.wv = wv;
//    }
//
//    public static void upTodo(Map<String, String> todoMap, String lecCode) {
//        Map<String, Map<String, String>> map = new HashMap<>();
//        String id = todoMap.get("title") + todoMap.get("date");
//        map.put(id, todoMap);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference cr = db.collection("todo");
//        cr.document(lecCode)
//                .set(map, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
//    }
//
//    public static void subsSnapshotListener(final Context context, String lecCode) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("todo").document(lecCode)
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.w(TAG, "Listen failed.", error);
//                            return;
//                        }
//
//                        if (value != null && value.exists()) {
//                            Map<String, Object> map = value.getData();
//                            for (Object o : map.values()) {
//                                Map<String, String> todoMap = ((Map<String, String>) o);
//                                boolean wrote = writeTodo(context, todoMap);
//                                if (wrote) {
//                                    Log.d(TAG, "課題を追加：" + todoMap);
//                                } else {
//                                    Log.d(TAG, "課題がすでに存在しています：" + todoMap);
//                                }
//                            }
//                        } else {
//                            Log.d(TAG, "Current data: null");
//                        }
//                    }
//                });
//    }
//
//    public void downTodo() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("todo");
//    }
//
////    public void searchLecture(Set<String> lecCodes) {
////        CollectionReference cr = db.collection("lectures5");
////        Query query = cr;
////        Log.d(TAG, "searchLecture: " + lecCodes);
////        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                if (task.isSuccessful()) {
////                    wv.writePref(task);
////                }
////            }
////        });
////    }
////
////    public void searchLecture(String faculty) {
////        CollectionReference cr = db.collection("lectures4");
////        cr = cr.document(faculty).collection(faculty + "_lectures");
////        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                if (task.isSuccessful()) {
////                    Log.d("Database", "タスク完了");
////                    sl.makeResults(task);
////                }
////            }
////        });
////        sl.makeResults(cr.get());
////        Query query = cr;
////        int cnt = 0;
////        String[] timeinfoKeys = {"semester", "week", "period", "room"};
////        for (Map.Entry<String, String> entry : map.entrySet()) {
////            String key = entry.getKey();
////            String value = entry.getValue();
////            if (Arrays.asList(timeinfoKeys).contains(entry.getKey())) {
////                key = "timeinfo.0." + key;
////            }
////            List<String> values = Arrays.asList(value.split("、"));
////            if (values.size() > 1) {
////                if (cnt == 0) {
////                    query = cr.whereIn(key, values);
////                } else if (cnt > 0) {
////                    query = query.whereIn(key, values);
////                }
////            } else {
////                if (cnt == 0) {
////                    query = cr.whereEqualTo(key, value);
////                } else if (cnt > 0) {
////                    query = query.whereEqualTo(key, value);
////                }
////            }
////            cnt++;
////        }
////        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                if (task.isSuccessful()) {
////                    sl.makeResults(task);
////                } else {
////                    Log.w("TAG", "Error getting documents.", task.getException());
////                }
////            }
////        });
////    }
//}
