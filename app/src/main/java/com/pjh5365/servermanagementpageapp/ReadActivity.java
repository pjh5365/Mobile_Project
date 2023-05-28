package com.pjh5365.servermanagementpageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class ReadActivity extends AppCompatActivity {

    Button btnUpdate, btnDelete;
    TextView tv_title, tv_content, tv_userID, tv_time;
    String getUserID, getListNum;
    String title, content, userID, time;

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
                intent.putExtra("userID", getUserID); //userID 넘겨주기
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        Intent intent = getIntent();
        getUserID = intent.getStringExtra("userID");
        getListNum = intent.getStringExtra("listNum");

        btnUpdate = (Button) findViewById(R.id.BtnUpdate);
        btnDelete = (Button) findViewById(R.id.BtnDelete);

        btnUpdate.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);

        tv_title = (TextView) findViewById(R.id.tv_title_board);
        tv_content = (TextView) findViewById(R.id.tv_content_board);
//        tv_userID = (TextView) findViewById(R.id.tv_userid);
//        tv_time = (TextView) findViewById(R.id.tv_time);

        sendRequest();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);  //액티비티 스택제거
                intent.putExtra("title", title);  //제목을 넘겨줌
                intent.putExtra("content", content);  //내용을 넘겨줌
                intent.putExtra("listNum", getListNum); //listNum 넘겨주기
                intent.putExtra("userID", getUserID); //userID 넘겨주기
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDeleteRequest();
            }
        });

    }
    public void sendRequest() {
        String url = "";    //작동해야하는 php주소
        String param = "?listNum=" + getListNum;    //GET방식으로 받아오기 위해 파라미터 추가
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url+param, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("네트워크 연결 성공");
                try {
                    title = response.getJSONObject(0).getString("title");
                    content = response.getJSONObject(0).getString("content");
                    userID = response.getJSONObject(0).getString("userID");
                    time = response.getJSONObject(0).getString("time");

                    tv_title.setText("제목 : " + title);
                    tv_content.setText("작성자 : "+ userID + "\t\t\t\t" + "작성시간 : " + time + "\n\n내용\n\n" +content);

                    if(userID.equals(getUserID)) {  //작성자와 로그인한 유저가 같을때만 보이게
                        btnUpdate.setVisibility(View.VISIBLE);
                        btnDelete.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //서버에 연결할 수 없을 때
                System.out.println(error.getMessage());
                Toast.makeText(getApplicationContext(), "서버에 연결할 수 없습니다 네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }) ;
        requestQueue.add(jsonArrayRequest);
    }

    public void sendDeleteRequest() {
        String url = "";    //작동해야하는 php주소
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("네트워크 연결 성공");

                if(response.equals("1")) {  //DB에 입력이 정상적으로 되었다면
                    Intent intent = new Intent(getApplicationContext(), BoardListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);  //액티비티 스택제거
                    intent.putExtra("userID", getUserID);  //세션을 유지하는 대신 아이디 전달
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "글을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                }
                else {  //DB에 값을 넣을 수 없다면
                    Toast.makeText(getApplicationContext(), "글을 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
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
                parmas.put("listNum", getListNum);   //listNum 넣기
                return parmas;
            }
        };
        stringRequest.setShouldCache(false);    //이전결과가 있어도 새로 요청하여 응답을 보여줌
        requestQueue = Volley.newRequestQueue(this);    //requestQueue 초기화
        requestQueue.add(stringRequest);    //파라미터 넣기
    }
}