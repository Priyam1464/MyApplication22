package com.example.acer.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Main3Activity extends AppCompatActivity //activity containing zoomed images and full screen images
{
    PhotoViewAttacher attachingPropertiesLikeZoomingToImage;//photo attacher which helps to zoom in the image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ImageView crossButton=(ImageView)findViewById(R.id.cross);
        crossButton.setOnClickListener(new View.OnClickListener()//method called when cross button is clicked
        {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(j);
            }
        });
        ImageView clickedImage=(ImageView) findViewById(R.id.img);
        Intent j=getIntent();
        String url= j.getStringExtra("title");
        Glide.with(this).load(url).fitCenter().into(clickedImage);
        attachingPropertiesLikeZoomingToImage = new PhotoViewAttacher(clickedImage);
    }


}
