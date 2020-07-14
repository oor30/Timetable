package com.example.x3033171.timetable;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_NUM = 2;

    private CharSequence[] tabTitles = {"about", "to do"};
    private Activity main;

    public PagerAdapter(@NonNull FragmentManager fm, Activity activity) {
        super(fm);
        main = activity;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable drawable;
        switch (position) {
            case 0:
                drawable = main.getDrawable(R.drawable.ic_baseline_menu_book_24);
                break;
            case 1:
                drawable = main.getDrawable(R.drawable.ic_baseline_create_24);
                break;
            default:
                drawable = main.getDrawable(R.drawable.ic_baseline_search_24);
                break;
        }

        SpannableStringBuilder sb = new SpannableStringBuilder("    " + tabTitles[position]);
        try {
            drawable.setBounds(5, 5, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        } catch (Exception e) {
            // TODO: handle exception
            return "tab name";
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ResultFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}
