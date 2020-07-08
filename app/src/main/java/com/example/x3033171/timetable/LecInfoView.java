package com.example.x3033171.timetable;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

public class LecInfoView extends ConstraintLayout {

    MainActivity main;
    TextView lecName, teacher, room, text, grade, result;
    Animation rightIn, leftOut;
    private ViewPager pager;
    private PagerAdapter adapter;
    private int currentPage;
    private HomeFragment homeFragment;
    private ResultFragment resultFragment;


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
        lecName = findViewById(R.id.lecNamename);
        teacher = findViewById(R.id.teacher);
        room = findViewById(R.id.room);
        text = findViewById(R.id.text);
        grade = findViewById(R.id.grade);

        rightIn = AnimationUtils.loadAnimation(context, R.anim.right_in);
        leftOut = AnimationUtils.loadAnimation(context, R.anim.left_out);

        result = findViewById(R.id.result);
        setFocusable(true);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setLecture(String lecCode, int week, int period) {
        pager = findViewById(R.id.viewPager);
        adapter = new PagerAdapter(main.getSupportFragmentManager());
        adapter.setLecCode(lecCode);
        currentPage = 0;

        //instantiateItem()で今のFragmentを取得
        homeFragment = (HomeFragment) adapter.instantiateItem(pager, 0);
        resultFragment = (ResultFragment) adapter.instantiateItem(pager, 1);

        Log.d("LecInfoView#setLecture", "LecInfoView#setLecture");
        Bundle bundle = new Bundle();
        bundle.putString("lecCode", lecCode);
        bundle.putInt("week", week);
        bundle.putInt("period", period);
        homeFragment.setArguments(bundle);
        pager.setAdapter(adapter);
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }
}
