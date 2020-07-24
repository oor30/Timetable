package com.example.x3033171.timetable;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.x3033171.timetable.Todo.TodoData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public interface TodoPref {
    /* Todoに関するメソッド */
    default boolean writeTodo(Context context, Map<String, String> todoMap) {
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
            OnCreateGlobalTodo(todoMap, lecCode);
//            TodoDatabase.upTodo(todoMap, lecCode);
        }
        return true;
    }

    @NotNull
    default ArrayList<Map<String, String>> readTodo(Context context, String lecCode) {
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
    default ArrayList<ArrayList<Map<String, String>>> readAllTodo(Context context, Set<String> lecCodes) {
        ArrayList<ArrayList<Map<String, String>>> allTodo = new ArrayList<>();
        for (String lecCode : lecCodes) {
            for (Map<String, String> map : readTodo(context, lecCode)) {
                OnCompleteTodoMap(map, lecCode);
//                Database.upTodo(map, lecCode);
            }
            allTodo.add(readTodo(context, lecCode));
        }
        return allTodo;
    }

    default void OnCreateGlobalTodo(@NotNull Map<String, String> todoMap, String lecCode){}

    default void OnCompleteTodoMap(Map<String, String> todoMap, String lecCode){}
}
