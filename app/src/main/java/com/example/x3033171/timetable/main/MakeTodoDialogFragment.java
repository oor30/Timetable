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

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.x3033171.timetable.R;
import com.example.x3033171.timetable.TodoDatabase;
import com.example.x3033171.timetable.TodoPref;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class MakeTodoDialogFragment extends DialogFragment implements TodoPref, TodoDatabase {
    NestedScrollView scrollView;
    String name, lecCode;
    int week;
    RadioGroup radioGroup, radioGroup2;
    RadioButton radioTask, radioExam, radioLocal, radioGlobal;
    TextView txName, txTitle, txDate, txTime, txMemo;
    Switch aSwitch;
    ConstraintLayout timeLayout;

    private Calendar calendar;
    SimpleDateFormat dateFormat, timeFormat;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        name = getArguments().getString("name");
        lecCode = getArguments().getString("lecCode");
        week = getArguments().getInt("week");

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

        radioGroup2 = dialog.findViewById(R.id.radioGroup2);
        radioLocal = dialog.findViewById(R.id.radioLocal);
        radioGlobal = dialog.findViewById(R.id.radioGlobal);

        timeLayout = dialog.findViewById(R.id.timeLayout);
        collapse(timeLayout);

        txName = dialog.findViewById(R.id.txName);
        txName.setText(name);

        txTitle = dialog.findViewById(R.id.txTitle);
        txDate = dialog.findViewById(R.id.date);

        Log.d("calender", "getInstance()");
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, week + 2);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 7);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        dateFormat = new SimpleDateFormat("M/d E", Locale.getDefault());
        txDate.setText(dateFormat.format(calendar.getTime()));

        txDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatePickerDialogインスタンスを取得
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                calendar.set(year, month, dayOfMonth);
                                txDate.setText(dateFormat.format(calendar.getTime()));
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE)
                );

                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
                //dialogを表示
                datePickerDialog.show();
            }
        });

        txTime = dialog.findViewById(R.id.txTime);
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        txTime.setText(timeFormat.format(calendar.getTime()));
        txTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                txTime.setText(timeFormat.format(calendar.getTime()));
                            }
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
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
                    collapse(timeLayout);
                } else {
                    expand(timeLayout);
                }
            }
        });

        txMemo = dialog.findViewById(R.id.txMemo);

        dialog.findViewById(R.id.btPos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                if (txTitle.getText().length() > 0 && calendar.after(now)) {
                    Map<String, String> todoMap = new HashMap<>();
                    String isTask = String.valueOf(radioTask.isChecked());
                    String title = txTitle.getText().toString();
                    String isAllDay = String.valueOf(aSwitch.isChecked());
                    String memo = txMemo.getText().toString();
                    String isLocal = String.valueOf(radioLocal.isChecked());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
                    String date = format.format(calendar.getTime());



                    todoMap.put("id", title + date);
                    todoMap.put("name", name);
                    todoMap.put("lecCode", lecCode);
                    todoMap.put("isTask", isTask);
                    todoMap.put("title", title);
                    todoMap.put("date", date);
                    todoMap.put("isAllDay", isAllDay);
                    todoMap.put("memo", memo);
                    todoMap.put("isLoacl", isLocal);

                    boolean isSuccess = writeTodo(Objects.requireNonNull(getContext()), todoMap);
                    if(isSuccess) {
                        upTodo(todoMap, lecCode);
                        ((MainActivity) getContext()).resultFragment.setLecCode(lecCode);
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

    @Override
    public void OnCreateGlobalTodo(@NotNull Map<String, String> todoMap, String lecCode) {
        upTodo(todoMap, lecCode);
    }

    @Override
    public void OnGetTodoMap(Map<String, String> todoMap) {

    }

    /* Viewの折りたたみに関するメソッド */
    private static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}