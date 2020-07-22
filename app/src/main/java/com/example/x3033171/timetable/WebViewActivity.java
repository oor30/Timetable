package com.example.x3033171.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private WebView webView;
    private Set<String> lecCodes;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        Button btReadHtml = findViewById(R.id.btReadHtml);
        btReadHtml.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        drawerLayout = findViewById(R.id.drawerLayout3);
        navigationView = findViewById(R.id.navigationView3);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.readHtml);   // メニューの時間割を選択済みに

        // ツールバー・検索ボックス
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.search);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new ViewClient());
        webView.addJavascriptInterface(this, "activity");
        webView.loadUrl("https://alss-portal.gifu-u.ac.jp/");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AlertDialog.Builder(this)
                .setTitle("使い方")
                .setMessage("ログイン後、My時間割を表示してからボタンを押してください")
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl("javascript:window.activity.viewSource(document.documentElement.outerHTML);");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @JavascriptInterface
    public void viewSource(final String src) {
        getLecCodes(src);
    }

    private final class ViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void getLecCodes(String src) {
        try {
            Document doc = Jsoup.parse(src);
            Element element = doc.getElementById("redrawArea");
            Elements elementsA = element.getElementsByTag("a");
            Log.d(TAG, "getLecCodes: " + elementsA);
            List<String> list = Arrays.asList(elementsA.toString().split("kougicd="));
            lecCodes = new HashSet<>();
            for (String s : list.subList(1, list.size())) {
                String lecCode = s.substring(0, 10);
                lecCodes.add(lecCode);
            }

            if (!lecCodes.isEmpty()) {
                Database database = new Database(this);
                database.searchLecture(lecCodes);
                return;
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "getLecCodes: HTMLから正しく要素を取得できませんでした");
        }
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "講義が見つかりませんでした", Toast.LENGTH_SHORT).show();
    }

    public void writePref(Task<QuerySnapshot> task) {
        ArrayList<Map<String, Object>> resultMaps = new ArrayList<>();
        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
            Map<String, Object> map = document.getData();
            String lecCode = String.valueOf(map.get("履修コード"));
            if (lecCodes.contains(lecCode)) {
                resultMaps.add(map);
            }
        }
        Fun.writeLecInfo(this, resultMaps);
//        // 共有プリファレンスに選択した講義の①情報（Map）と②履修コード（String）を保存
//        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
//        preferences.edit().clear().apply();     // 共有プリファレンス"pref"のデータを削除
//        Gson gson = new Gson();
//        Map<String, Object> map;
//
//        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//            map = document.getData();
//            String lecCode = String.valueOf(map.get("履修コード"));
//            if (lecCodes.contains(lecCode)) {
//                // ①講義情報
//                preferences.edit().putString(String.valueOf(map.get("履修コード")), gson.toJson(map)).apply();  // 履修コードをKeyとしてGsonを用いて保存
//                lecCodes.add(lecCode);
//            }
//        }
//        // ②履修コード
//        preferences.edit().putStringSet("lecCodes", lecCodes).apply();

        // MainActivityに戻る
        progressBar.setVisibility(View.INVISIBLE);
        finish();
    }
}