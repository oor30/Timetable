package com.example.x3033171.timetable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private WebView webView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WebViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WebViewFragment newInstance(String param1, String param2) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mInflate = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = mInflate.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(this, "activity");
        webView.loadUrl("https://alss-portal.gifu-u.ac.jp/");
        return mInflate;
    }

    @JavascriptInterface
    public void viewSource(final String src) {
        String file = "timetable.html";
        getLecCodes(src);
    }

    private void getLecCodes(String src) {
        Document doc = Jsoup.parse(src);
        Element element = doc.getElementById("redrawArea");
        Elements elementsA = element.getElementsByTag("a");
        Log.d(TAG, "getLecCodes: " + elementsA);
        List<String> list = Arrays.asList(elementsA.toString().split("kougicd="));
        List<String> lecCodes = new ArrayList<>();
        for (String s : list.subList(1, list.size())) {
            String lecCode = s.substring(0, 10);
            lecCodes.add(lecCode);
        }
        Log.d(TAG, "getLecCodes: " + lecCodes);
    }
}