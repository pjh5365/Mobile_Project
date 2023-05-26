package com.pjh5365.servermanagementpageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText loginID, loginPW;
    Button btnLogin, btnJoin;

    String id, passwd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginID = (EditText) findViewById(R.id.loginID);
        loginPW = (EditText) findViewById(R.id.loginPW);

        btnLogin = (Button) findViewById(R.id.BtnLogin);
        btnJoin = (Button) findViewById(R.id.BtnJoin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = loginID.getText().toString();
                passwd = loginPW.getText().toString();

                Intent intent = new Intent(getApplicationContext(), BoardListActivity.class);
                startActivity(intent);
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
}