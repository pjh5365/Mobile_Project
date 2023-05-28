package com.pjh5365.servermanagementpageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText loginID, loginPW;
    Button btnLogin, btnJoin;

    String userID, passwd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.INTERNET"}, 0);

        loginID = (EditText) findViewById(R.id.loginID);
        loginPW = (EditText) findViewById(R.id.loginPW);

        btnLogin = (Button) findViewById(R.id.BtnLogin);
        btnJoin = (Button) findViewById(R.id.BtnJoin);

        btnLogin.setOnClickListener(new View.OnClickListener() {    //로그인 버튼 클릭했을때
            @Override
            public void onClick(View v) {
                userID = loginID.getText().toString().trim();   //아이디 공백없이 가져오기
                passwd = loginPW.getText().toString().trim();   //비밀번호 공백없이 가져오기

                sendRequest();  //로그인 요청
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    public void sendRequest() {
        String url = "";    //작동해야하는 php주소
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("네트워크 연결 성공");

                if(response.equals("1")) {  //DB에 정보가 있다면 서버에서 1을 반환함
                    Intent intent = new Intent(getApplicationContext(), BoardListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);  //액티비티 스택제거
                    intent.putExtra("userID", userID);  //세션을 유지하는 대신 아이디 전달
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "로그인에 성공 했습니다.", Toast.LENGTH_SHORT).show();
                }
                else {  //DB에 정보가 없다면 실행
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //서버에 연결할 수 없을 때
                System.out.println(error.getMessage());
                Toast.makeText(getApplicationContext(), "서버에 연결할 수 없습니다 네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parmas = new HashMap<String, String>();
                parmas.put("userID", userID);   //보낼 아이디 해쉬맵에 넣기
                parmas.put("userPW", passwd);   //보낼 비밀번호 해쉬맵에 넣기
                return parmas;
            }
        };
        stringRequest.setShouldCache(false);    //이전결과가 있어도 새로 요청하여 응답을 보여줌
        requestQueue = Volley.newRequestQueue(this);    //requestQueue 초기화
        requestQueue.add(stringRequest);    //파라미터 넣기
    }
}