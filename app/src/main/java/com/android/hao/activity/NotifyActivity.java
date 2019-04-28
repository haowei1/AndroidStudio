package com.android.hao.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.hao.R;

public class NotifyActivity extends AppCompatActivity {

    private Button btnDialog, btnViewPager, btnNotify;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initUI();
    }

    private void initUI() {
        btnDialog = findViewById(R.id.btn_dialog);
        btnNotify = findViewById(R.id.btn_notify);
        btnViewPager = findViewById(R.id.btn_viewPager);
        
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotify();
            }
        });

        btnViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViewPage();
            }
        });
    }

    private void showViewPage() {
        startActivity(new Intent(getApplicationContext(), ViewPageActivity.class));
    }

    private void showNotify() {
        NotificationCompat.Builder notify = new NotificationCompat.Builder(this);
//        Notification.Builder notify = new Notification.Builder(this);sss
        notify.setSmallIcon(R.drawable.ic_launcher_background);
        notify.setContentTitle("你有一条新的通知");
        notify.setContentText("Good Good Study! Day Day Up!");
        notify.setAutoCancel(true);
        notify.setTicker("新消息！");
        notify.setWhen(System.currentTimeMillis());
        Notification notification = notify.build();
        notificationManager.notify(100, notification);
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_launcher_background);
        dialog.setTitle("充值");
        dialog.setCancelable(false);
        dialog.setMessage("确定充值100元？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"充值成功", Toast.LENGTH_LONG).show();
            }
        });

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"充值取消", Toast.LENGTH_LONG).show();
            }
        });

//        AlertDialog alertDialog = dialog.create();
        dialog.create().show();
    }
}
