package com.lista.loginandregisterbyemail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_register;
    private EditText edt_email_login,edt_password_login;
    private Button btn_login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_register=findViewById(R.id.txt_register);
        edt_email_login=findViewById(R.id.edt_email_login);
        edt_password_login=findViewById(R.id.edt_password_login);
        progressBar_login=findViewById(R.id.progressBar_login);
        btn_login=findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();

        txt_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.txt_register:
                startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.btn_login:
                loginUser();

        }
    }

    private void loginUser() {
        String email=edt_email_login.getText().toString().trim();
        String password=edt_password_login.getText().toString().trim();

        if(email.isEmpty()){
            edt_email_login.setError("Email is required!");
            edt_email_login.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email_login.setError("Please provide valid email!");
            edt_email_login.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edt_password_login.setError("Password is required!");
            edt_password_login.requestFocus();
            return;
        }

        if(password.length()<6){
            edt_password_login.setError("Minimum password length is 6 characters!");
            edt_password_login.requestFocus();
            return;
        }

        progressBar_login.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this,DashBordActivity.class));
                        Toast.makeText(MainActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                        progressBar_login.setVisibility(View.GONE);
                        edt_email_login.setText("");
                        edt_password_login.setText("");

                    }

                }else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials!", Toast.LENGTH_LONG).show();
                    progressBar_login.setVisibility(View.GONE);
                }
            }
        });
    }
}