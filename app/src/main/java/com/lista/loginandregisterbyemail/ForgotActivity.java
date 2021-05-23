package com.lista.loginandregisterbyemail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    private Button btn_forgot;
    private EditText edt_email_forgot;
    private ProgressBar progressBar_forgot;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        btn_forgot=findViewById(R.id.btn_forgot);
        edt_email_forgot=findViewById(R.id.edt_email_forgot);
        progressBar_forgot=findViewById(R.id.progressBar_forgot);

        mAuth=FirebaseAuth.getInstance();

        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email=edt_email_forgot.getText().toString().trim();
        if(email.isEmpty()){
            edt_email_forgot.setError("Email is required!");
            edt_email_forgot.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email_forgot.setError("Please provide valid email!");
            edt_email_forgot.requestFocus();
            return;
        }

        progressBar_forgot.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotActivity.this, "Check your email to reset password!", Toast.LENGTH_SHORT).show();
                    progressBar_forgot.setVisibility(View.GONE);
                }else {
                    Toast.makeText(ForgotActivity.this, "Try again! Something went wrong!", Toast.LENGTH_SHORT).show();
                    progressBar_forgot.setVisibility(View.GONE);
                }
            }
        });
    }
}