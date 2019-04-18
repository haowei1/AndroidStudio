package com.android.hao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.hao.db.TestDao;

import java.util.List;

public class TestDBActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TestDBActivity";
    private TestDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);
        dao = TestDao.getInstance(getApplicationContext());
        initUI();

    }

    private void initUI() {
        findViewById(R.id.insert).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);
        findViewById(R.id.selectAll).setOnClickListener(this);
        findViewById(R.id.selectByID).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
        List<Object> list = dao.selectById(1);
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
        int i = dao.update(1, "haoweiya", "123123");
        if (i > 0) {
            Log.i(TAG, "update: successful");
        } else {
            Log.i(TAG, "update: failed");
        }
    }

    private void delete() {
        int i = dao.delete(1);
        if (i > 0) {
            Log.i(TAG, "delete: successful");
        } else {
            Log.i(TAG, "delete: failed");
        }
    }

    private void insert() {
        long l = dao.insert(1, "haoweiya", "123456");
        if (l > 0) {
            Log.i(TAG, "insert: successful");
        } else {
            Log.i(TAG, "insert: failed");
        }
    }
}
