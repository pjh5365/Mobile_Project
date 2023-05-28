package com.pjh5365.servermanagementpageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class UpdateActivity extends AppCompatActivity {

    Button btnUpdate;
    EditText tv_title, tv_content;

    String getTitle, getContent, getListNum, getUserID;
    String updateTitle, updateContent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)    {  //홈버튼 추가를 위함
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //버튼을 눌러 리스트로 이동
        switch (item.getItemId())
        {
            case R.id.home_action:
                Intent intent = new Intent(getApplicationContext(), BoardListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);  //액티비티 스택제거
                intent.putExtra("userID", getUserID);  //세션을 유지하는 대신 아이디 전달
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        Intent intent = getIntent();
        getTitle = intent.getStringExtra("title");
        getContent = intent.getStringExtra("content");
        getListNum = intent.getStringExtra("listNum");
        getUserID = intent.getStringExtra("userID");

        btnUpdate = (Button) findViewById(R.id.BtnUpdateAction);
        tv_title = (EditText) findViewById(R.id.edittext_title_update);
        tv_content = (EditText) findViewById(R.id.edittext_content_update);

        tv_title.setText(getTitle);
        tv_content.setText(getContent);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTitle = tv_title.getText().toString();
                updateContent = tv_content.getText().toString();
                sendRequest();
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

                if(response.equals("1")) {  //DB에 입력이 정상적으로 되었다면
                    Intent intent = new Intent(getApplicationContext(), ReadActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);  //액티비티 스택제거
                    intent.putExtra("userID", getUserID);  //세션을 유지하는 대신 아이디 전달
                    intent.putExtra("listNum", getListNum);  //돌아가서 같은 글을 보여줘야 하므로 listNum 전달
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "글을 수정하였습니다.", Toast.LENGTH_SHORT).show();
                }
                else {  //DB에 값을 넣을 수 없다면
                    Toast.makeText(getApplicationContext(), "글을 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
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
                parmas.put("listNum", getListNum);   //전달받은 아이디 입력
                parmas.put("title", updateTitle);   //제목 해쉬맵에 넣기
                parmas.put("content", updateContent);   //내용 해쉬맵에 넣기
                return parmas;
            }
        };
        stringRequest.setShouldCache(false);    //이전결과가 있어도 새로 요청하여 응답을 보여줌
        requestQueue = Volley.newRequestQueue(this);    //requestQueue 초기화
        requestQueue.add(stringRequest);    //파라미터 넣기
    }
}
