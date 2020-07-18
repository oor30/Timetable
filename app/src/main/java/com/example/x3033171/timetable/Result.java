package com.example.x3033171.timetable;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

//public class Result extends ConstraintLayout {
//
//    String name, lecClass, lecCode;
//    int grade;
//    ArrayList<Integer> weeks, periods;
//    TextView resName, resWeek, resPeriod;
//    CheckBox checkBox;
//    boolean checked;
//    Map<String, Object> resultMap;
//
//    public Result(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public Result(Context context, QueryDocumentSnapshot document) {
//        super(context);
//        init(context);
//
//        resultMap = document.getData();
//        name = resultMap.get("授業科目名").toString();
//        lecClass = resultMap.get("科目分類").toString();
//        lecCode = resultMap.get("履修コード").toString();
//        Map<String, Map<String, String>> timeinfo = (Map<String, Map<String, String>>) resultMap.get("timeinfo");
//
//        weeks = new ArrayList<>();
//        periods = new ArrayList<>();
//        for (Map<String, String> map : timeinfo.values()) {
//            int week = Integer.parseInt(map.get("week"));
//            if (!weeks.contains(week)) {
//                weeks.add(week);
//            }
//
//            int period = Integer.parseInt(map.get("period"));
//            if (!periods.contains(period)) {
//                periods.add(period);
//            }
//        }
////        period = Integer.parseInt(timeinfo.get("0").get("period"));
//        grade = Integer.parseInt(document.get("grade").toString());
//        resName.setText(name);
//        resWeek.setText(weeks.toString().replace("1", "月").replace("2", "火")
//        .replace("3", "水").replace("4", "木").replace("5", "金")
//        .replace("[", "").replace("]", ""));
//        resPeriod.setText(periods.toString().replace("[", "").replace("]", ""));
//    }
//
//    void init(Context context) {
//        LayoutInflater.from(context).inflate(R.layout.result_layout, this);
//        resName = findViewById(R.id.resName);
//        resWeek = findViewById(R.id.resWeek);
//        resPeriod = findViewById(R.id.resPeriod);
//        checked = false;
//        checkBox = findViewById(R.id.addLecCB);
//        checkBox.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checked = checkBox.isChecked();
//                if (checked) {
//                    setBackgroundColor(Color.parseColor("#FFEEEE"));
//                } else {
//                    setBackgroundColor(Color.parseColor("#FFFFFF"));
//                }
//            }
//        });
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getLecClass() {
//        return lecClass;
//    }
//
//    public String getLecCode() {
//        return lecCode;
//    }
//
//    public int getGrade() {
//        return grade;
//    }
//
//    public ArrayList<Integer> getWeeks() {
//        return weeks;
//    }
//
//    public ArrayList<Integer> getPeriods() {
//        return periods;
//    }
//
//    public boolean getChecked() {
//        return checked;
//    }
//
//    public void setChecked (boolean b) {
//        checked = b;
//        checkBox.setChecked(b);
//    }
//
//    public Map<String, Object> getResultMap () {
//        return resultMap;
//    }
//}

public class Result {

    private String name, lecClass, lecCode;
    private int grade;
    private TreeSet<Integer> weeks, periods;
    private ArrayList<String> rooms;
    private boolean checked;
    private Map<String, Object> resultMap;

    public Result(QueryDocumentSnapshot document) {
        try {
            resultMap = document.getData();
            name = String.valueOf(resultMap.get("授業科目名"));      // nullなら"null"が代入される
            lecClass = String.valueOf(resultMap.get("科目分類"));
            lecCode = String.valueOf(resultMap.get("履修コード"));
            Map<String, Map<String, Object>> timeinfo = (Map<String, Map<String, Object>>) resultMap.get("timeinfo");
            checked = false;

            weeks = new TreeSet<>();
            periods = new TreeSet<>();
            rooms = new ArrayList<>();
            if (timeinfo != null) {
                for (Map<String, Object> map : timeinfo.values()) {

                    String weekTmp = String.valueOf(map.get("week"));
                    int week;
                    if (!weekTmp.equals("null")) week = Integer.parseInt(weekTmp);
                    else week = 0;
                    weeks.add(week);

                    String periodTmp = String.valueOf(map.get("period"));
                    int period;
                    if (!periodTmp.equals("null")) period = Integer.parseInt(periodTmp);
                    else period = 0;
                    periods.add(period);

                    String roomTmp = String.valueOf(map.get("room"));
                    if (rooms.contains(roomTmp) && !roomTmp.equals("null")) rooms.add(roomTmp);
                }
            }
            else {
                weeks.add(0);
                periods.add(0);
            }
            String gradeTmp = String.valueOf(document.get("grade"));
            try {
                grade = Integer.parseInt(gradeTmp);
            } catch (NullPointerException e) {
                grade = 0;
            }
        } catch (Exception e) {
            Log.d("Result", name);
            Log.e("Result", e.toString());
        }
//        period = Integer.parseInt(timeinfo.get("0").get("period"));
//        resName.setText(name);
//        resWeek.setText(weeks.toString().replace("1", "月").replace("2", "火")
//        .replace("3", "水").replace("4", "木").replace("5", "金")
//        .replace("[", "").replace("]", ""));
//        resPeriod.setText(periods.toString().replace("[", "").replace("]", ""));
    }

    public String getName() {
        return name;
    }

    public String getLecClass() {
        return lecClass;
    }

    public String getLecCode() {
        return lecCode;
    }

    public int getGrade() {
        return grade;
    }

    @NonNull
    public TreeSet<Integer> getWeeks() {
        return weeks;
    }

    @NonNull
    public TreeSet<Integer> getPeriods() {
        return periods;
    }

    @NonNull
    public ArrayList<String> getRooms() {
        return rooms;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked (boolean b) {
        checked = b;
    }

    public Map<String, Object> getResultMap () {
        return resultMap;
    }
}
