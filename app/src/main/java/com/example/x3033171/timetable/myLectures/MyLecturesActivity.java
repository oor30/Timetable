package com.example.x3033171.timetable.myLectures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.x3033171.timetable.LecturePref;
import com.example.x3033171.timetable.searchLecture.Model;
import com.example.x3033171.timetable.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyLecturesActivity extends AppCompatActivity implements LecturePref {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private MyLecRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lectures);

        drawerLayout = findViewById(R.id.drawerLayout);
        // ツールバー
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search);
        toolbar.setTitle("MyLectures");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // サイドメニュー
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.myLec);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        drawerLayout.closeDrawers();
                        finish();
                        return true;
                    case R.id.myLec:
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyLecRecyclerViewAdapter(readMyLec());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Set<String> lecCodes = new HashSet<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            Model model = adapter.getList().get(i);
            if (model.isChecked()) {
                lecCodes.add(model.getLecCode());
            }
        }
        writeLecCodes(this, lecCodes);
    }

    private List<Model> readMyLec() {
        Set<String> lecCodes = readLecCodes(this);
        List<Model> dataset = new ArrayList<>();

        if (lecCodes != null) {
            ArrayList<Map<String, Object>> resultMaps = readAllLecInfo(this);
            for (Map<String, Object> resultMap : resultMaps) {
                if (lecCodes.contains(String.valueOf(resultMap.get("履修コード")))) {
                    dataset.add(new Model(resultMap, true));
                }
                else {
                    dataset.add(new Model(resultMap, false));
                }
            }
        }
        return dataset;
    }
}