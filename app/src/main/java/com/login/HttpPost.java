package com.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by USER on 2016-10-27.
 */

public class HttpPost {
    private String pageEnc = "UTF-8";

    public HttpPost() {}

    public void insert(String varQuery) {
        String urlAdrs = "http://hihost.dothome.co.kr/login/reg_proc.php";
        postURL(urlAdrs, varQuery);
    }

    public String select() {
        String urlAdrs = "http://hihost.dothome.co.kr/login/sel_proc.php";
        return postURL(urlAdrs);
    }

    public void del(String varQuery) {
        String urlAdrs = "http://hihost.dothome.co.kr/login/del_proc.php";
        postURL(urlAdrs, varQuery);
    }

    public void trun() {
        String urlAdrs = "http://hihost.dothome.co.kr/login/trun_proc.php";
        openURL(urlAdrs);
    }

    public void openURL(final String urlString) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setDefaultUseCaches(false);
                    httpConn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public String postURL(final String urlAdrs) {
        return postURL(urlAdrs, null, null);
    }

    public String postURL(final String urlAdrs, final String varQuery) {
        return postURL(urlAdrs, varQuery, null);
    }

    /**
     * @param urlAdrs
     * @param varQuery = id=myId&pw=myPw
     * @return loadPageData
     */
    public String postURL(final String urlAdrs, final String varQuery, final String enc) {
        final StringBuilder tmpResult = new StringBuilder();
        if (enc != null) pageEnc = enc;
        Thread webConnThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAdrs);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setDefaultUseCaches(false);
                    httpConn.setDoInput(true);
                    httpConn.setDoOutput(true);
                    httpConn.setRequestMethod("POST");
                    httpConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    if (varQuery != null) {
                        OutputStreamWriter outStream = new OutputStreamWriter(httpConn.getOutputStream(), pageEnc);
                        PrintWriter writer = new PrintWriter(outStream);
                        writer.write(varQuery);
                        writer.flush();
                    }
                    InputStreamReader tmp = new InputStreamReader(httpConn.getInputStream(), pageEnc);
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    while ((str = reader.readLine()) != null)
                        tmpResult.append(str + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        webConnThread.start();

        try {
            webConnThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tmpResult.toString();
    }

    public String jsonToStr(String originData) {

        StringBuilder resultBuilder = new StringBuilder();
        try {
            JSONObject json = new JSONObject(originData);
            JSONArray array = json.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String id = jsonObject.getString("id");
                String pw = jsonObject.getString("pw");
                final String print = "id :" + id + ", pw :" + pw;
                resultBuilder.append(print);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultBuilder.toString();
    }
}
