package com.pjh5365.servermanagementpageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    EditText loginName, loginID, loginPW;
    Button btnJoin;

    String name, id, passwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        loginName = (EditText) findViewById(R.id.loginName);
        loginID = (EditText) findViewById(R.id.loginID);
        loginPW = (EditText) findViewById(R.id.loginPW);

        btnJoin = (Button) findViewById(R.id.BtnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = loginName.getText().toString();
                id = loginID.getText().toString();
                passwd = loginPW.getText().toString();

                Intent intent = new Intent(getApplicationContext(), BoardListActivity.class);
                startActivity(intent);
            }
        });
    }
}