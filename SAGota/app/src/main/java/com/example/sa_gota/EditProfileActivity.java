package com.example.sa_gota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    private Button edit;
    private Button cancel;
    private EditText full_name;
    private EditText display_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edit=findViewById(R.id.confirmEditProfile);
        cancel=findViewById(R.id.cancelEditProfile);
        full_name=findViewById(R.id.profileEditPageName);
        display_name=findViewById(R.id.profileEditDisplay);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = full_name.getText().toString();
                String txt_display = display_name.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                if(!(txt_name.isEmpty()) || !(txt_display.isEmpty())) {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            MyUser user = snapshot.getValue(MyUser.class);

                            user.setDisplay(txt_display);
                            user.setName(txt_name);

                            UserProfileChangeRequest updates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(txt_display)
                                    .build();

                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) Toast.makeText(EditProfileActivity.this, "Edit successful!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            ref.removeValue();
                            ref.setValue(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(EditProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                    finish();
                }else{
                    Toast.makeText(EditProfileActivity.this, "Empty Values!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfileActivity.this, "Edit cancelled!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                finish();
            }
        });

    }
}