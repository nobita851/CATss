package com.example.cat.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cat.CustomViews.customlist;
import com.example.cat.R;
import com.example.cat.adapters.MessageAdapter;
import com.example.cat.dataModels.ChatMessage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AluminiActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static final int RC_PHOTO_PICKER = 2;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private customlist messageListview;
    private MessageAdapter messageAdapter;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumini);
            messageListview = (customlist) findViewById(R.id.list_item_messages_chat);
            List<ChatMessage> list = new ArrayList<ChatMessage>();
            messageAdapter = new MessageAdapter(this, R.layout.listitem, list);
            messageListview.setAdapter(messageAdapter);
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseStorage=FirebaseStorage.getInstance();
            databaseReference = firebaseDatabase.getReference().child("messages");
            storageReference=firebaseStorage.getReference().child("chat_photos");
            FloatingActionButton imgepicker =
                    (FloatingActionButton) findViewById(R.id.imagepicker);
            imgepicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                }
            });

            FloatingActionButton fab =
                    (FloatingActionButton) findViewById(R.id.floating_action_button);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input = (EditText) findViewById(R.id.editTextNumberDecimal);

                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    databaseReference
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName(),null)
                            );

                    // Clear the input
                    input.setText("");
                }
            });
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    messageAdapter.add(chatMessage);


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Log.v("djehd", "hiiiiii");
            Uri selectimage = data.getData();
            final StorageReference image = storageReference.child(selectimage.getLastPathSegment());
            image.putFile(selectimage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return image.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        ChatMessage friendlyMessage = new ChatMessage(null, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), downloadUri.toString());
                        databaseReference.push().setValue(friendlyMessage);
                    } else {
                        Toast.makeText(AluminiActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

    }


}