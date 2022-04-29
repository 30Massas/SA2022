package com.example.sa_gota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText display_name;
    private EditText password;
    private EditText name;
    private EditText course;
    private Button register;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        display_name=findViewById(R.id.display_name);
        name=findViewById(R.id.name);
        course=findViewById(R.id.course);
        register=findViewById(R.id.register);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("users");

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_name = name.getText().toString();
                String txt_course = course.getText().toString();
                String txt_display = display_name.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(txt_email, txt_password);
                    MyUser user = new MyUser(txt_email,txt_name,txt_course,txt_display);
                    Map<String,MyUser> users = new HashMap<>();
                    users.put(txt_email.split("@")[0],user);
                    ref.setValue(users);
                }
            }
        });

    }

    private void registerUser(String txt_email, String txt_password) {
        auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User successfully registered!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, StartActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}