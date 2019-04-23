package com.android.hao.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.hao.R;

public class RedioViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redio_view);

        VideoView vv = findViewById(R.id.vv);

        //设置有进度条可以拖动快进
        MediaController localMediaController = new MediaController(this);
        vv.setMediaController(localMediaController);
        String uri = ("android.resource://" + getPackageName() + "/" + R.raw.wm);
        vv.setVideoURI(Uri.parse(uri));
        vv.start();
    }
}
