package com.example.x3033171.timetable.main;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.x3033171.timetable.R;
import com.google.android.material.tabs.TabLayout;

class LecInfoView extends ConstraintLayout {

    private String name, lecCode;
    private TextView lecName;
    private TabLayout tabLayout;
    private MainActivity main;

    public LecInfoView(Context context) {
        super(context);
        init(context);
    }

    public LecInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LecInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.lec_info_view, this);
        lecName = findViewById(R.id.name);
        tabLayout = findViewById(R.id.tabLayout);
        Button btMakeTodo = findViewById(R.id.btMakeTodo);
        btMakeTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeTodoDialogFragment fragment = new MakeTodoDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("lecCode", lecCode);
                fragment.setArguments(bundle);
                fragment.show(main.getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    void setLecName(String lecName) {
        name = lecName;
        this.lecName.setText(lecName);
    }

    public TextView getLecName() {
        return lecName;
    }

    public void setLecCode(String lecCode) {
        this.lecCode = lecCode;
    }

    public String getLecCode() {
        return lecCode;
    }

    void setMain(MainActivity main) {
        this.main = main;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }
}
