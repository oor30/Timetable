package com.example.x3033171.timetable.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.x3033171.timetable.Fun;
import com.example.x3033171.timetable.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MakeTodoDialogFragment extends DialogFragment {
    TextView txName, txTitle, txDate, txTime;
    ConstraintLayout timeLayout;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String name = getArguments().getString("name");

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_make_todo_dialog);
        dialog.setTitle("課題作成");
        RadioButton radioTask = dialog.findViewById(R.id.radioTask);
        RadioButton radioExam = dialog.findViewById(R.id.radioExam);

        timeLayout = dialog.findViewById(R.id.timeLayout);
        Fun.collapse(timeLayout);

        txName = dialog.findViewById(R.id.txName);
        txName.setText(name);

        txTitle = dialog.findViewById(R.id.txTitle);
        txDate = dialog.findViewById(R.id.date);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy / MM / dd", Locale.getDefault());
        txDate.setText(sdf.format(date));
        txDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar date = Calendar.getInstance();

                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                txDate.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                            }
                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );

                //dialogを表示
                datePickerDialog.show();
            }
        });

        txTime = dialog.findViewById(R.id.txTime);
        sdf = new SimpleDateFormat("hh : mm", Locale.getDefault());
        txTime.setText(sdf.format(date));
        txTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar date = Calendar.getInstance();

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                txTime.setText(String.format("%d : %d" , hourOfDay, minute));
                            }
                        },
                        date.get(Calendar.HOUR),
                        date.get(Calendar.MINUTE),
                        true
                );
                timePickerDialog.show();

            }
        });

        Switch aSwitch = dialog.findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Fun.collapse(timeLayout);
                } else {
                    Fun.expand(timeLayout);
                }
            }
        });

        dialog.findViewById(R.id.btPos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txTitle.getText().length() > 0) {
                    String title = txTitle.getText().toString();

                }
            }
        });

        dialog.findViewById(R.id.btNeg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return dialog;
    }
}