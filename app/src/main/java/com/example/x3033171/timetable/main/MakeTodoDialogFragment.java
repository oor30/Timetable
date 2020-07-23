package com.example.x3033171.timetable.main;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.x3033171.timetable.Fun;
import com.example.x3033171.timetable.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class MakeTodoDialogFragment extends DialogFragment {
    NestedScrollView scrollView;
    String name, lecCode;
    RadioGroup radioGroup;
    RadioButton radioTask, radioExam;
    TextView txName, txTitle, txDate, txTime, txMemo;
    Switch aSwitch;
    ConstraintLayout timeLayout;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        name = getArguments().getString("name");
        lecCode = getArguments().getString("lecCode");

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_make_todo_dialog);
        dialog.setTitle("課題作成");
        radioTask = dialog.findViewById(R.id.radioTask);
        radioExam = dialog.findViewById(R.id.radioExam);

        scrollView = dialog.findViewById(R.id.scrollView);
        radioGroup = dialog.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int colorWhite = Color.WHITE;
                int colorOrange = getResources().getColor(R.color.colorAccent);
//                int colorOrange = Color.parseColor("#FFFDE7");
                ValueAnimator colorAnimation;

                if (checkedId == R.id.radioTask) {
                    colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorOrange, colorWhite);
                } else {
                    colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorWhite, colorOrange);
                }

                colorAnimation.setDuration(300);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        scrollView.setBackgroundColor((int) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            }
        });
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
                                txTime.setText(String.format("%d : %02d" , hourOfDay, minute));
                            }
                        },
                        date.get(Calendar.HOUR),
                        date.get(Calendar.MINUTE),
                        true
                );
                timePickerDialog.show();

            }
        });

        aSwitch = dialog.findViewById(R.id.switch1);
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

        txMemo = dialog.findViewById(R.id.txMemo);

        dialog.findViewById(R.id.btPos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txTitle.getText().length() > 0) {
                    Map<String, String> todoMap = new HashMap<>();
                    String isTask = String.valueOf(radioTask.isChecked());
                    String title = txTitle.getText().toString();
                    String date = txDate.getText().toString();
                    String isAllDay = String.valueOf(aSwitch.isChecked());
                    String time = txTime.getText().toString();
                    String memo = txMemo.getText().toString();

                    todoMap.put("lecCode", lecCode);
                    todoMap.put("isTask", isTask);
                    todoMap.put("title", title);
                    todoMap.put("date", date);
                    todoMap.put("isAllDay", isAllDay);
                    todoMap.put("time", time);
                    todoMap.put("memo", memo);

                    boolean isSuccess = Fun.writeTodo(Objects.requireNonNull(getContext()), todoMap);
                    if(isSuccess) {
                        dialog.dismiss();
                    } else {
                        String selectedRadio;
                        if (radioTask.isChecked()) {
                            selectedRadio = "課題";
                        } else {
                            selectedRadio = "試験";
                        }
                        // ガイドメッセージ
                        new AlertDialog.Builder(getContext())
                                .setMessage("タイトル・日付が同じ" + selectedRadio + "がすでに存在しています")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    // ガイドメッセージ
                    new AlertDialog.Builder(getContext())
                            .setMessage("タイトルを入力してください")
                            .setPositiveButton("OK", null)
                            .show();
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