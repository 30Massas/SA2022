package com.example.sa_gota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email,display_name,password,name,course;
    private Button register;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth=FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("users");

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        display_name=findViewById(R.id.display_name);
        name=findViewById(R.id.name);
        course=findViewById(R.id.course);

        register=findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_name = name.getText().toString();
                String txt_course = course.getText().toString();
                String txt_display = display_name.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_display) || TextUtils.isEmpty(txt_course)){
                    Toast.makeText(RegisterActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                    Toast.makeText(RegisterActivity.this, "Invalid email address!", Toast.LENGTH_SHORT).show();
                }else{
                    auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                MyUser user = new MyUser(txt_email,
                                        txt_name,
                                        txt_course,
                                        txt_display);
                                ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "User Successfully Registered!", Toast.LENGTH_SHORT).show();

                                            UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(txt_display)
                                                    .build();

                                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(updates);
                                        }
                                    }
                                });
                                startActivity(new Intent(RegisterActivity.this, StartActivity.class));
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void registerUser(String txt_email, String txt_password, String txt_name, String txt_course, String txt_display) {

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}