package com.lista.loginandregisterbyemail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lista.loginandregisterbyemail.Model.User;

public class DashBordActivity extends AppCompatActivity {

    private Button btn_logout;
    private TextView txt_email_dash,txt_fullName_dash,txt_age_dash,txt_back;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_bord);


        txt_email_dash=findViewById(R.id.txt_email_dash);
        txt_fullName_dash=findViewById(R.id.txt_fullName_dash);
        txt_age_dash=findViewById(R.id.txt_age_dash);
        txt_back=findViewById(R.id.txt_back);
        btn_logout=findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashBordActivity.this,MainActivity.class));
            }
        });

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());
        userId=user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userModel=snapshot.getValue(User.class);
                if(userModel!=null){
                    txt_back.setText("Welcome back "+userModel.getUserName());
                    txt_email_dash.setText(userModel.getEmail());
                    txt_fullName_dash.setText(userModel.getUserName());
                    txt_age_dash.setText(userModel.getAge());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DashBordActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

    }
}