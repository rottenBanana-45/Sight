package com.example.sight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sight.JavaClasses.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private ImageButton backBtn, postBtn;
    private EditText editTitle, editPost;
    String name;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        backBtn = findViewById(R.id.back_imageButton);
        postBtn = findViewById(R.id.sent_btn);
        editPost = findViewById(R.id.editText3);
        editTitle = findViewById(R.id.editText2);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, HomeActivity.class));
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users")
                        .child("student")
                        .child(uid);


                //Toast.makeText(PostActivity.this, uid, Toast.LENGTH_LONG).show();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            if(ds.getKey().equals("name")){
                                name = ds.getValue().toString();
                                flag = false;
                            }

                            if(!flag){
                                flag = true;
                                String post = editPost.getText().toString();
                                String title = editTitle.getText().toString();
                                Post data = new Post();

                                data.setName(name);


                                Calendar calendar  = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("hh:mm a, dd MMMM yyyy");
                                String time = format.format(calendar.getTime());
                                //Toast.makeText(PostActivity.this, name, 3000).show();

                                data.setTime(time);
                                data.setPost(post);
                                data.setTitle(title);


                                DatabaseReference newref =  FirebaseDatabase.getInstance().getReference().child("posts").push();
                                String pid = newref.getKey();

                                data.setPid(pid);
                                data.setStatus("New");

                                FirebaseDatabase.getInstance().getReference().child("posts").child("all").child(pid)
                                        .setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(PostActivity.this, "Posted successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                startActivity(new Intent(PostActivity.this, HomeActivity.class));
                                finish();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }
}
