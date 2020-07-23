package com.example.x3033171.timetable.main;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.Window;

import com.example.x3033171.timetable.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class GradeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_grade_dialog);
        RangeSeekBar seekBar = dialog.findViewById(R.id.rangeSeekBar);
        seekBar.setRangeValues(0, 10);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                int min = (int) bar.getSelectedMinValue();
                int max = (int) bar.getSelectedMaxValue();
            }
        });
        return dialog;
    }
}