package com.example.jonathan.dallascowboysmvps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText editText_id;
    EditText editText_password;

    FirebaseAuth mAuth;
    FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_id = findViewById(R.id.id);
        editText_password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
    }

    public void CreateAccount(View view) {

        String email = editText_id.getText().toString();
        String password = editText_password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Account Created!",Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Failed to Create Account...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void Login(View view) {

        String email = editText_id.getText().toString();
        String password = editText_password.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Login Successful!",Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),"Failed to Login...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void SignOut(View view) {
        mAuth.signOut();
        user = null;
        Toast.makeText(this, "User Logged Out!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

}
