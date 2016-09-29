package com.example.acer.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener//viewholder for images
{
    ArrayList<total> listContainingTheImages =new ArrayList<>();//array list containing the uri of images and their keys
public ImageView imageViewContainingTheImage, imageViewGreyColoured;//image view containing the image and the image view grey coloured
    Context context;
    public ImageHolder(View itemView, Context text, ArrayList<total>listContainingTheImages) {
        super(itemView);
        itemView.setOnClickListener(this);
         imageViewContainingTheImage =(ImageView)itemView.findViewById(R.id.imageView);
        imageViewGreyColoured =(ImageView)itemView.findViewById(R.id.imageView3);
        context=text;
        this.listContainingTheImages =listContainingTheImages;
    }

    @Override
    public void onClick(View v) //method called when the image is clicked
    {
        int clickedImageOrChatPosition=getPosition();
        total clickedImage= listContainingTheImages.get(clickedImageOrChatPosition);
        Intent activityContainingZoomedImage=new Intent(context,Main3Activity.class);
        activityContainingZoomedImage.putExtra("title",clickedImage.uri);
        Log.d("ImageHolder",""+this.context);
        this.context.startActivity(activityContainingZoomedImage);

        //ImageHolder.this.context.startActivity(activityContainingZoomedImage);
        /*int clickedImageOrChatPosition=getPosition();
        total information=listContainingTheImages.get(clickedImageOrChatPosition);
        Intent k= new Intent(v.getContext(),Main3Activity.class);
        k.putExtra("Url",information.uri);
        v.getContext().startActivity(k);*/
    }
}
