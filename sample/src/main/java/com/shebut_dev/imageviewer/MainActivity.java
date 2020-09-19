package com.shebut_dev.imageviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageViewer imageViewer = findViewById(R.id.image_viewer);
        //imageViewer.setViewableImage(ContextCompat.getDrawable(this,
                //R.drawable.test_image));
        imageViewer.setViewableImage(BitmapFactory.decodeResource(getResources(),
                R.drawable.test_image));
    }
}