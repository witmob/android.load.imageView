package com.easymorse.android_load_imageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import com.util.AsyncImageLoader;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ImageView imageView=(ImageView)findViewById(R.id.image);

        AsyncImageLoader imageLoader = new AsyncImageLoader();
        imageLoader.loadLoadingDrawable("http://api.art.china.cn/pics/exhibition/20130510030535264_c1.jpg", new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Bitmap mBitmap, String imageUrl) {
                Log.v("imageTag","-----111--->>"+mBitmap);
                if (mBitmap != null) {
                    imageView.setImageBitmap(mBitmap);
                }
            }
        });
    }
}
