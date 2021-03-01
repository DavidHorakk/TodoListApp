package cz.dahor.todolistapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CancellationSignal;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WelcomeActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_welcome);

                Executor executor = Executors.newSingleThreadExecutor();

        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this).setTitle("Fingerprint Authentification").setSubtitle("Please verify your fingerprint to continue.").setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        }).build();

        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent homeIntent = new Intent( WelcomeActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });


//                new Handler().postDelayed(new Runnable(){
//                    public void run(){
//                        Intent homeIntent = new Intent(WelcomeActivity.this, MainActivity.class);
//                        startActivity(homeIntent);
//                        finish();
//                    }
//                },SPLASH_TIME_OUT);
    }


}


