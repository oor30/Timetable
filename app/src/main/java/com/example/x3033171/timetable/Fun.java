package com.example.x3033171.timetable;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// staticメソッド用クラス
public class Fun {
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
    public static Set<String> readLecCodes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("pref-lecCodes", Context.MODE_PRIVATE);
        return preferences.getStringSet("lecCodes", null);
    }

    // 履修コードから講義情報を返すメソッド：MainActivityから呼び出される
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
    public static Set<String> readAllLecCodes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("pref-lecInfo", Context.MODE_PRIVATE);
        return preferences.getStringSet("allLecCodes", null);
    }

    // My講義に登録した講義情報を返すメソッド：MyLectureActivityから呼び出される
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
}
