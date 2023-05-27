package com.pjh5365.servermanagementpageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class BoardListActivity extends AppCompatActivity {

    Button btnWrite;
    String id;
    ListView listView;
    ArrayList<ListViewItem> list;
    ListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_list);
        //로그인에서 ID을 전달받음
        Intent intent = getIntent();
        id = intent.getStringExtra("userID");
        btnWrite = (Button) findViewById(R.id.BtnWrite);
        list = new ArrayList<>();
        adapter = new ListViewAdapter(this, list);
        listView = (ListView) findViewById(R.id.listView);

        sendRequest();
        System.out.println("함수종료");
        for(int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getListNum());
            adapter.addItem(list.get(i));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                intent.putExtra("userID", id);  //세션을 유지하는 대신 아이디 전달
                startActivity(intent);
            }
        });
    }
    public void sendRequest() {
        String url = "userURL";    //작동해야하는 php주소
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("네트워크 연결 성공");
                try {
                    for(int i = 0; i < response.length(); i++) {    //Json데이터를 전부가져옴
                        String title = response.getJSONObject(i).getString("title");
                        String userID = response.getJSONObject(i).getString("userID");
                        String listNum = response.getJSONObject(i).getString("listNum");
                        list.add(new ListViewItem("제목 : " + title, "작성자 : " + userID, listNum));
                    }
                    listView.setAdapter(adapter);   //함수안에서 안하면 sleep을 길게 걸어주어야 화면이 정상적으로 출력됨... 아마 쓰레드 동기화하면 될듯?
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
        });
        requestQueue.add(jsonArrayRequest);
    }
}
