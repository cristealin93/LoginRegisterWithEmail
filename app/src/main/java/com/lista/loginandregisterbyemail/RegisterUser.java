package com.lista.loginandregisterbyemail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lista.loginandregisterbyemail.Model.User;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_full_name_register,edt_age_register,edt_email_register,edt_password_register;
    private Button btn_register;
    private ProgressBar progressBar_register;
    private ImageView img_logo;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        edt_full_name_register=findViewById(R.id.edt_full_name_register);
        edt_age_register=findViewById(R.id.edt_age_register);
        edt_email_register=findViewById(R.id.edt_email_register);
        edt_password_register=findViewById(R.id.edt_password_register);
        btn_register=findViewById(R.id.btn_register);
        progressBar_register=findViewById(R.id.progressBar_register);
        img_logo=findViewById(R.id.img_logo);

        img_logo.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_logo:
                startActivity(new Intent(this,MainActivity.class));
            case R.id.btn_register:
                addNewUser();
        }
    }

    private void addNewUser() {

        String name=edt_full_name_register.getText().toString().trim();
        String age=edt_age_register.getText().toString().trim();
        String email=edt_email_register.getText().toString().trim();
        String password=edt_password_register.getText().toString().trim();

        if(name.isEmpty()){
            edt_full_name_register.setError("Full name is required!");
            edt_full_name_register.requestFocus();
            return;
        }

        if(age.isEmpty()){
            edt_age_register.setError("Age is required!");
            edt_age_register.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edt_email_register.setError("Email is required!");
            edt_email_register.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email_register.setError("Please provide valid email!");
            edt_email_register.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edt_password_register.setError("Password is required!");
            edt_password_register.requestFocus();
            return;
        }

        if(password.length()<6){
            edt_password_register.setError("Minimum password length is 6 characters!");
            edt_password_register.requestFocus();
            return;
        }

        progressBar_register.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(name,age,email);
                            FirebaseDatabase.getInstance().getReference(User.class.getSimpleName())
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                    progressBar_register.setVisibility(View.GONE);
                                    startActivity(new Intent(RegisterUser.this,MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                    progressBar_register.setVisibility(View.GONE);
                                }
                            });
                        }else {
                            Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                            progressBar_register.setVisibility(View.GONE);
                        }
                    }
                });
    }
}