package com.lista.loginandregisterbyemail;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    private Button btn_forgot;
    private EditText edt_email_forgot;
    private TextView txt_timer;
    private ProgressBar progressBar_forgot;
    private FirebaseAuth mAuth;
    private long milliseconds=30000;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        btn_forgot=findViewById(R.id.btn_forgot);
        txt_timer=findViewById(R.id.txt_timer);
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

        //Close soft keyboard
        View view = this.getCurrentFocus();
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // send reset password email
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotActivity.this, "Check your email to reset password!", Toast.LENGTH_SHORT).show();
                    progressBar_forgot.setVisibility(View.GONE);
                    edt_email_forgot.setText("");
                    btn_forgot.setEnabled(false);
                    btn_forgot.setBackgroundColor(btn_forgot.getContext().getResources().getColor(R.color.disable_button));
                    txt_timer.setVisibility(View.VISIBLE);
                    timerDown();
                }else {
                    Toast.makeText(ForgotActivity.this, "Try again! Something went wrong!", Toast.LENGTH_SHORT).show();
                    progressBar_forgot.setVisibility(View.GONE);
                }
            }
        });
    }

    private void timerDown() {
        countDownTimer=new CountDownTimer(milliseconds,1000) {
            @Override
            public void onTick(long l) {
                milliseconds=l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void updateTimer() {
        int seconds=(int)milliseconds%30000/1000;

        String timeLeft;
        timeLeft="Try again in: "+seconds;
        if(seconds<1) {
            btn_forgot.setEnabled(true);
            btn_forgot.setBackgroundColor(btn_forgot.getContext().getResources().getColor(R.color.teal_200));
            txt_timer.setVisibility(View.GONE);
            timeLeft="0";
            countDownTimer.cancel();
        }

        txt_timer.setText(timeLeft);
    }


}