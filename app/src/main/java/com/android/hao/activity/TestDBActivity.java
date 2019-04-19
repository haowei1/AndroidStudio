package com.android.hao.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.hao.R;
import com.android.hao.db.TestDao;

import java.util.List;

public class TestDBActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TestDBActivity";
    private TestDao dao;
    private EditText username, id, password;
    private String newusername, newpassword;
    private int id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);
        dao = TestDao.getInstance(getApplicationContext());
        initUI();

    }

    private void initUI() {
        id = findViewById(R.id.id);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        findViewById(R.id.insert).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.selectAll).setOnClickListener(this);
        findViewById(R.id.selectByID).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        newusername = username.getText().toString().trim();
        newpassword = password.getText().toString().trim();
        String s = id.getText().toString().trim();
        if (s != null && s.equals("")){
            id1 = Integer.parseInt(s);
        }
        switch (v.getId()){
            case R.id.insert:
                insert();
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.update:
                update();
                break;
            case R.id.selectAll:
                selectAll();
                break;
            case R.id.selectByID:
                selectById();
                break;
        }
    }

    private void selectById() {
        List<Object> list = dao.selectById(id1);
        if (list != null && list.size() > 0) {
            Log.i(TAG, "selectById: list" + list.toString());
        }
    }

    private void selectAll() {
        List<Object> list = dao.selectAll();
        if (list != null && list.size() > 0) {
            Log.i(TAG, "selectAll: list" + list.toString());
        }
    }

    private void update() {
        int i = dao.update(id1, newusername, newpassword);
        if (i > 0) {
            Log.i(TAG, "update: successful");
        } else {
            Log.i(TAG, "update: failed");
        }
    }

    private void delete() {
        int i = dao.delete(id1);
        if (i > 0) {
            Log.i(TAG, "delete: successful");
        } else {
            Log.i(TAG, "delete: failed");
        }
    }

    private void insert() {
        long l = dao.insert(newusername, newpassword);
        if (l > 0) {
            Log.i(TAG, "insert: successful");
        } else {
            Log.i(TAG, "insert: failed");
        }
    }
}
