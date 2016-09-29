package com.example.acer.myapplication2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DatabaseReference database;//reference to the realtime chats and images database
    FirebaseStorage chatsAndImagesStorageInstance;//reference to storage link  of images and chats
    Button sendChatMessage;//button to send chat messages
    RecyclerView chatsAndImagesContainingRecyclerView;//RecyclerView containing chats and images
    Adapter adapterContainingTotalListOfChatsAndImages;
    ArrayList<total> chatsAndImages = new ArrayList<>();
    StorageReference chatsAndImagesStorage;//reference to child of the chatsAndImagesStorageInstance
    StorageReference chatsAndImageChildReference;//reference to each individual child of chatsAndImagesStorage
    UploadTask uploadingChatAndImageToServer;//button to upload chat or image to server
    EditText chatMessage;//chat message user wants to send
    Button importFromCameraButton;//button to import image from the camera

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendChatMessage = (Button) findViewById(R.id.button2);
        database = FirebaseDatabase.getInstance().getReference();
        chatsAndImagesStorageInstance = FirebaseStorage.getInstance();
        chatMessage = (EditText) findViewById(R.id.edit);
        chatsAndImagesStorage = chatsAndImagesStorageInstance.getReferenceFromUrl("gs://app1-1d896.appspot.com");
        chatsAndImagesContainingRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapterContainingTotalListOfChatsAndImages = new Adapter(chatsAndImages, this);
        chatsAndImagesContainingRecyclerView.setAdapter(adapterContainingTotalListOfChatsAndImages);
        chatsAndImagesContainingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        importFromCameraButton = (Button) findViewById(R.id.button);
        sendChatMessage = (Button) findViewById(R.id.button2);
        sendChatMessage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) //method to send chat message to the server
            {
                total chatMessageCurrentlyClicked = new total(chatMessage.getText().toString(), 0);
                //chatsAndImages.add(chatMessageCurrentlyClicked);
               // adapterContainingTotalListOfChatsAndImages.notifyDataSetChanged();
                String key = database.child("links").push().getKey();
                Map<String, Object> chatMap = chatMessageCurrentlyClicked.toMap();
                Map<String, Object> childUpdate = new HashMap<String, Object>();
                childUpdate.put("/links/" + key, chatMap);
                database.updateChildren(childUpdate);
            }
        });
        Query chatsAndImagesQuery = database.child("links").orderByKey();
        chatsAndImagesQuery.addChildEventListener(new ChildEventListener()//query the images and chat messages from the firebase realtime database
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              total retrievedChatAndImage = new total(dataSnapshot.child("uri").getValue(String.class),dataSnapshot.child("key").getValue(Integer.class));
              chatsAndImages.add(retrievedChatAndImage);
                adapterContainingTotalListOfChatsAndImages.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        importFromCameraButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)//method to import image from the camera
            {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) //method called the image is selected from the gallery of the mobile phone
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            Uri selectedImage = data.getData();
            try {
                Bitmap importedBitmapFromCamera = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream streamContainingImage = new ByteArrayOutputStream();
                importedBitmapFromCamera.compress(Bitmap.CompressFormat.JPEG, 20, streamContainingImage);
                byte[] date = streamContainingImage.toByteArray();
                chatsAndImageChildReference = chatsAndImagesStorage.child("images/" + selectedImage.getPath());
                uploadingChatAndImageToServer = chatsAndImageChildReference.putBytes(date);
                uploadingChatAndImageToServer.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)//method called when image upload is successfull
                    {
                        try {
                            total imageUploaded = new total(String.valueOf(taskSnapshot.getDownloadUrl()),1);
                           // chatsAndImages.add(imageUploaded);
                           // adapterContainingTotalListOfChatsAndImages.notifyDataSetChanged();
                            String key = database.child("links").push().getKey();
                            Map<String, Object> chatMap = imageUploaded.toMap();
                            Map<String, Object> childUpdate = new HashMap<String, Object>();
                            childUpdate.put("/links/" + key,chatMap);
                            database.updateChildren(childUpdate);
                        } catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),"image uploading to the server failed ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(),"image is not found on the mobile phone",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"image error",Toast.LENGTH_LONG).show();
            }
        }
    }
}






