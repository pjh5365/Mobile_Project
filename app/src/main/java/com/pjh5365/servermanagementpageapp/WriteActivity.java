package com.pjh5365.servermanagementpageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends AppCompatActivity {
    EditText editTitle, editContent;
    Button btnWrite;

    String title, content, id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);

        //로그인에서 ID을 전달받음
        Intent intent = getIntent();
        id = intent.getStringExtra("userID");

        editTitle = (EditText) findViewById(R.id.text_title);
        editContent = (EditText) findViewById(R.id.test_content);

        btnWrite = (Button) findViewById(R.id.BtnWrite);

        btnWrite.setOnClickListener(new View.OnClickListener() {    //작성버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                title = editTitle.getText().toString();   //제목 가져오기
                content = editContent.getText().toString();   //내용 가져오기

                sendRequest();  //로그인 요청
            }
        });
    }

    public void sendRequest() {
        String url = "userURL";    //작동해야하는 php주소
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("네트워크 연결 성공");

                if(response.equals("1")) {  //DB에 입력이 정상적으로 되었다면
                    Intent intent = new Intent(getApplicationContext(), BoardListActivity.class);
                    intent.putExtra("userID", id);  //세션을 유지하는 대신 아이디 전달
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "글을 작성하였습니다.", Toast.LENGTH_SHORT).show();
                }
                else {  //DB에 값을 넣을 수 없다면
                    Toast.makeText(getApplicationContext(), "글을 작성할 수 없습니다.", Toast.LENGTH_SHORT).show();
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
                parmas.put("userID", id);   //전달받은 아이디 입력
                parmas.put("title", title);   //제목 해쉬맵에 넣기
                parmas.put("content", content);   //내용 해쉬맵에 넣기
                return parmas;
            }
        };
        stringRequest.setShouldCache(false);    //이전결과가 있어도 새로 요청하여 응답을 보여줌
        requestQueue = Volley.newRequestQueue(this);    //requestQueue 초기화
        requestQueue.add(stringRequest);    //파라미터 넣기
    }
}
