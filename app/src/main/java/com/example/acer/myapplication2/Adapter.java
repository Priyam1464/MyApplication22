package com.example.acer.myapplication2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by acer on 8/26/2016.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> //adapter containing the list passed from the main activity
{
    //  int k;
    ArrayList<total> totalList = new ArrayList<>();
    ArrayList<total> imagesList = new ArrayList<>();
    ArrayList<total> chatsList = new ArrayList<>();
    Context context;
    private final int imageViewType = 1;
    private final int chatViewType = 0;

    public Adapter(ArrayList<total> totalList, Context context) {
        this.totalList = totalList;
        for (total eachObjectFromTheTotalList : totalList) {
            if (eachObjectFromTheTotalList.key == 1) {
                imagesList.add(eachObjectFromTheTotalList);
            } else {
                chatsList.add(eachObjectFromTheTotalList);
            }
        }
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == imageViewType) {
            LayoutInflater imagesInflater = LayoutInflater.from(parent.getContext());
            View imageView = imagesInflater.inflate(R.layout.file, parent, false);
            ImageHolder imageHolder = new ImageHolder(imageView, context, totalList);
            return imageHolder;
        } else {
            LayoutInflater chatsInflater = LayoutInflater.from(parent.getContext());
            View chatView = chatsInflater.inflate(R.layout.chat, parent, false);
            ChatHolder chatHolder = new ChatHolder(chatView);
            return chatHolder;
        }
        // final ImageView imageButton=(ImageView)v.findViewById(R.id.imageButton);
       /* imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton.setVisibility(View.INVISIBLE);
               progressBar.setVisibility(View.VISIBLE);
                progressBar.isShown();
                if(k==1){
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });*/

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        switch (getItemViewType(position)) {
            case imageViewType:
                ImageHolder imageHolder = (ImageHolder) holder;
                total imageCurrentlyBeingAddedIntoTheRecyclerView = totalList.get(position);
                Glide.with(context).load(imageCurrentlyBeingAddedIntoTheRecyclerView.uri).into(imageHolder.imageViewContainingTheImage);
                imageHolder.imageViewGreyColoured.setVisibility(View.INVISIBLE);
                break;
            case chatViewType:
                ChatHolder chatHolder = (ChatHolder) holder;
                total chits = totalList.get(position);
                chatHolder.chatMessageAddedInTheRecyclerView.setText(chits.uri);
                break;

            //  k=1;
        }
    }

    @Override
    public int getItemCount() {
        return totalList.size();
    }

    @Override
    public int getItemViewType(int position)//method called for every view it returns an int which tells whether the viewholder is of image or of chat type
    {
        if (totalList.get(position).key == 1) {
            return imageViewType;
        } else {
            return chatViewType;
        }
    }

    public class ChatHolder extends RecyclerView.ViewHolder//viewholder for chat images
    {
        public TextView chatMessageAddedInTheRecyclerView;

        public ChatHolder(View itemView) {
            super(itemView);
            chatMessageAddedInTheRecyclerView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
