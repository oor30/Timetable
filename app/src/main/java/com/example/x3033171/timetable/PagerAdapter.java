package com.example.x3033171.timetable;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_NUM = 2;

    private String lecCode;

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        Log.d("test", "adapter getItem");
        bundle.putString("lecCode", lecCode);
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new ResultFragment();
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }

    public void setLecCode(String lecCode) {
        this.lecCode = lecCode;
    }
}
