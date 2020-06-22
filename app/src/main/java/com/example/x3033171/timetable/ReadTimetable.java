//package com.example.x3033171.timetable;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.googlecode.tesseract.android.TessBaseAPI;
//
//public class ReadTimetable extends AppCompatActivity {
//
//    private TessBaseAPI tessBaseAPI;
//    String filepath;
//    Bitmap bitmap;
//
//
//    ReadTimetable() {
//        tessBaseAPI = new TessBaseAPI();
//        filepath = getFilesDir() + "/tesseract";
//
//        tessBaseAPI.init(filepath, "jpn");
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
//        tessBaseAPI.setImage(bitmap);
//        String getstr = tessBaseAPI.getUTF8Text();
//        Log.d("読み込み結果", getstr);
//    }
//}
