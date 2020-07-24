package com.example.x3033171.timetable.Todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.x3033171.timetable.Fun;
import com.example.x3033171.timetable.LecturePref;
import com.example.x3033171.timetable.R;
import com.example.x3033171.timetable.TodoDatabase;
import com.example.x3033171.timetable.TodoPref;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TodoActivity extends AppCompatActivity implements TodoPref, LecturePref, TodoDatabase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        List<TodoData> dataList = new ArrayList<>();
        ArrayList<ArrayList<Map<String, String>>> todo = readAllTodo(this, readLecCodes(this));
        for (ArrayList<Map<String, String>> maps : todo) {
            for (Map<String, String> map : maps) {
                dataList.add(new TodoData(map));
            }
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TodoRecyclerViewAdapter adapter = new TodoRecyclerViewAdapter(dataList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnCompleteTodoMap(Map<String, String> todoMap, String lecCode) {
        upTodo(todoMap, lecCode);
    }

    @Override
    public void OnGetTodoMap(Map<String, String> todoMap) {

    }
}