package com.easymorse.android_load_imageView;

import android.app.Activity;
import android.os.Bundle;

import com.util.AsyncImageView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final AsyncImageView imageView=(AsyncImageView)findViewById(R.id.image);
        imageView.setUrl("http://api.art.china.cn/pics/exhibition/20130510030535264_c1.jpg", R.drawable.calendar_loading);
    }
}
