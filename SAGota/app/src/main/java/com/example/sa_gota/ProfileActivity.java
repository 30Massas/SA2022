package com.example.sa_gota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference ref;
    private String userEmail;
    private ImageView menu;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        menu=(ImageView) findViewById(R.id.dropdown_menu);

        menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        edit= findViewById(R.id.editProfile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("users");

        final TextView fullNameView = (TextView) findViewById(R.id.editName);
        final TextView displayNameView = (TextView) findViewById(R.id.editDisplay);

        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyUser userProfile = snapshot.getValue(MyUser.class);

                if(userProfile != null){
                    String fullName = userProfile.getName();
                    String display = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                    fullNameView.setText(fullName);
                    displayNameView.setText(display);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(ProfileActivity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.profile){
                    Toast.makeText(ProfileActivity.this, "Already here!", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId() == R.id.logout){
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(ProfileActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this,StartActivity.class));
                    finish();
                }
                else if(menuItem.getItemId() == R.id.chat){
                    startActivity(new Intent(ProfileActivity.this,ChatActivity.class));
                }else if(menuItem.getItemId() == R.id.home){
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                }
                return true;
            }
        });

        popupMenu.show();
    }
}