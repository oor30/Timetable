package com.example.x3033171.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.x3033171.timetable.Todo.TodoData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static android.content.ContentValues.TAG;

// publicなstaticメソッド用クラス
public class Fun {

    /* 講義情報・履修コードの保存・読み込みに関するメソッド */
    // 時間割表に表示する講義の履修コードを保存するメソッド：MyLectureActivityの編集後に呼び出される
    public static void writeLecCodes(Context context, Set<String> lecCodes) {
        SharedPreferences preferences = context.getSharedPreferences("pref-lecCodes", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();     // 共有プリファレンス"pref"のデータを削除
        preferences.edit().putStringSet("lecCodes", lecCodes).apply();
    }

    // 講義情報とその履修コードを保存するメソッド：SearchLectureの編集後、WebViewActivityで読み込み後呼び出される
    public static void writeLecInfo(Context context, ArrayList<Map<String, Object>> resultMaps) {
        SharedPreferences preferences = context.getSharedPreferences("pref-lecInfo", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();     // 共有プリファレンス"pref"のデータを削除
        Gson gson = new Gson();
        HashSet<String> lecCodes = new HashSet<>();     // 履修コードのHashSet

        // 1.講義情報
        for (Map<String, Object> resultMap : resultMaps) {     // すべての対象の講義に対し
            String lecCode = String.valueOf(resultMap.get("履修コード"));
            preferences.edit().putString(lecCode, gson.toJson(resultMap)).apply();
            lecCodes.add(lecCode);
        }
        // 2.履修コード
        preferences.edit().putStringSet("allLecCodes", lecCodes).apply();
    }

    // 時間割表に表示する講義の履修コードを返すメソッド：MainActivityから呼び出される
    @NotNull
    public static Set<String> readLecCodes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("pref-lecCodes", Context.MODE_PRIVATE);
        Set<String> lecCodes = preferences.getStringSet("lecCodes", null);
        if (lecCodes == null) {
            return new HashSet<>();
        }
        return lecCodes;
    }

    // 履修コードから講義情報を返すメソッド：MainActivityから呼び出される
    @NotNull
    public static ArrayList<Map<String, Object>> readLecInfo(Context context, Set<String> lecCodes) {
        ArrayList<Map<String, Object>> resultMaps = new ArrayList<>();

        SharedPreferences preferences = context.getSharedPreferences("pref-lecInfo", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        if (lecCodes != null) {
            for (String lecCode : lecCodes) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> resultMap = gson.fromJson(preferences.getString(lecCode, ""), type);    // 講義情報を取得
                resultMaps.add(resultMap);
            }
        }
        return resultMaps;
    }

    // My講義に登録した講義の履修コードを返すメソッド：SearchLectureから呼び出される
    @NotNull
    public static Set<String> readAllLecCodes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("pref-lecInfo", Context.MODE_PRIVATE);
        Set<String> lecCodes = preferences.getStringSet("allLecCodes", null);
        if (lecCodes == null) {
            return new HashSet<>();
        }
        return lecCodes;
    }

    // My講義に登録した講義情報を返すメソッド：MyLectureActivityから呼び出される
    @NotNull
    public static ArrayList<Map<String, Object>> readAllLecInfo(Context context) {
        ArrayList<Map<String, Object>> resultMaps = new ArrayList<>();

        SharedPreferences preferences = context.getSharedPreferences("pref-lecInfo", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        Set<String> lecCodes = preferences.getStringSet("allLecCodes", null);

        if (lecCodes != null) {
            for (String lecCode : lecCodes) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> resultMap = gson.fromJson(preferences.getString(lecCode, ""), type);    // 講義情報を取得
                resultMaps.add(resultMap);
            }
        }
        return resultMaps;
    }



    /* Todoに関するメソッド */
    public static boolean writeTodo(Context context, Map<String, String> todoMap) {
        SharedPreferences preferences = context.getSharedPreferences("pref-todo", Context.MODE_PRIVATE);
        String lecCode = String.valueOf(todoMap.get("lecCode"));
        ArrayList<Map<String, String>> todoMaps = readTodo(context, lecCode);
        for (Map<String, String> map : todoMaps) {
            if (Objects.equals(todoMap.get("id"), map.get("id"))) {
                return false;
            }
        }
        Gson gson = new Gson();
        todoMaps.add(todoMap);
        preferences.edit().putString(lecCode, gson.toJson(todoMaps)).apply();

        if (Objects.equals(todoMap.get("isLocal"), "false")) {
            Database.upTodo(todoMap, lecCode);
        }
        return true;
    }

    @NotNull
    public static ArrayList<Map<String, String>> readTodo(Context context, String lecCode) {
        SharedPreferences preferences = context.getSharedPreferences("pref-todo", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
        ArrayList<Map<String, String>> todoMaps = gson.fromJson(preferences.getString(lecCode, ""), type);
        if(todoMaps == null) {
            return new ArrayList<>();
        }
        return todoMaps;
    }

    @NotNull
    public static ArrayList<ArrayList<Map<String, String>>> readAllTodo(Context context) {
        Set<String> lecCodes = readAllLecCodes(context);
        ArrayList<ArrayList<Map<String, String>>> allTodo = new ArrayList<>();
        for (String lecCode : lecCodes) {
            for (Map<String, String> map : readTodo(context, lecCode)) {
                Database.upTodo(map, lecCode);
            }
            allTodo.add(readTodo(context, lecCode));
        }
        return allTodo;
    }

    public static class TodoDataComparator implements Comparator<TodoData> {
        @Override
        public int compare(TodoData o1, TodoData o2) {
            Calendar o1Cal = o1.getCalendar();
            Calendar o2Cal = o2.getCalendar();

            return o1Cal.compareTo(o2Cal);
        }
    }



    /* Viewの折りたたみに関するメソッド */
    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }



    // 同一コマに講義が重複していないかチェックするメソッド
    @NotNull
    public static Set<String> checkLecOver(Context context) {
        ArrayList<Map<String, Object>> resultMaps = readAllLecInfo(context);
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

    public static void setOutCursorListener(final Context context, final TextView textView, final View parent) {
        textView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER)
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(parent.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    parent.requestFocus();
                    textView.clearFocus();
                }
                return false;
            }
        });
    }
}
