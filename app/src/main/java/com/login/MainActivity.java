package com.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etId, etPw;
    Button btnLogin, btnResult, btnDel, btnTrun;
    TextView tvResult;
    HttpPost httpPost = new HttpPost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        etId = (EditText) findViewById(R.id.etId);
        etPw = (EditText) findViewById(R.id.etPw);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnResult = (Button) findViewById(R.id.btnResult);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnTrun = (Button) findViewById(R.id.btnTrun);
        tvResult = ((TextView) findViewById(R.id.tvResult));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, String, String>() {
                    String id, pw;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        id = etId.getText().toString();
                        pw = etPw.getText().toString();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        httpPost.insert("id=" + id + "&pw=" + pw);
                        return null;
                    }
                }.execute();
            }
        });
        //
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText(httpPost.select().toString().replace("{", "{\n"));
            }
        });
        //
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpPost.del("id=" + etId.getText().toString());
                Toast.makeText(MainActivity.this, etId.getText().toString() + " 삭제 됐습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        //
        btnTrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpPost.trun();
                Toast.makeText(MainActivity.this, "초기화", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
