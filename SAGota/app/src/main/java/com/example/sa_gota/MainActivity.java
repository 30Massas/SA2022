package com.example.sa_gota;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private DatabaseReference dataRef;
    private TextView num;
    private Button update;
    private ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout=findViewById(R.id.logout);
        num=(TextView) findViewById(R.id.numTotal);
        update=findViewById(R.id.update);
        menu=(ImageView) findViewById(R.id.dropdown_menu);
        dataRef= FirebaseDatabase.getInstance().getReference().child("ProbeData");
        update=findViewById(R.id.update);

        //updateValue();

        update.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                updateValue();
            }
        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(MainActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this,StartActivity.class));
//            }
//        });

        menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

    }

    private void updateValue() {
        dataRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                for(DataSnapshot d : snapshot.getChildren()){
                    count += d.child("probes").getChildrenCount();
                }
                updateCount(Integer.toString(count));
                Toast.makeText(MainActivity.this, "Count Updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Number updating!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCount(String count) {
        num.setText(count);
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.home){
                    Toast.makeText(MainActivity.this, "Already here!", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId() == R.id.logout){
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MainActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,StartActivity.class));
                    finish();
                }
                else if(menuItem.getItemId() == R.id.chat){
                    startActivity(new Intent(MainActivity.this,ChatActivity.class));
                }else if(menuItem.getItemId() == R.id.profile){
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                }
                return true;
            }
        });

        popupMenu.show();
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }
}