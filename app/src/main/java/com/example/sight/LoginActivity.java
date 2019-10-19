package com.example.sight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button loginbtn;
    TextView createIDbtn;
    TextInputLayout emailField, passField;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.login_buttonID);
        emailField = findViewById(R.id.editTextEmail);
        passField = findViewById(R.id.editTextPass);
        createIDbtn = findViewById(R.id.create_accountID);

        mAuth = FirebaseAuth.getInstance();

        createIDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailField.getEditText().getText().toString().trim();
                final String pass = passField.getEditText().getText().toString();

                if(!checkInvalidInput(email, pass)){
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Logging in");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    emailField.setError(null);

                    login(email, pass);

                }
            }
        });


    }

    private boolean checkValidEmail(String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
        if(email.matches(emailPattern)){
            return true;
        }
        return false;
    }

    private boolean checkInvalidInput(String email, String pass){

        boolean ret = false;

        if(pass.isEmpty()){
            //passField.setError("Field can't be empty!");
            ret = true;
        } else {
            passField.setError(null);
        }

        if(!checkValidEmail(email) || email.isEmpty()){
            if(email.isEmpty()){
                //emailField.setError("Field can't be empty!");
            } else {
                //emailField.setError("Invalid Email");
            }
            ret = true;
        } else {
            emailField.setError(null);
        }

        return ret;
    }

    private void login(String email, String pass){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();


                    Intent loggedInIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    loggedInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loggedInIntent);
                    finish();

                } else {
                    progressDialog.dismiss();

                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_LONG).show();
                    }
                    else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                        Toast.makeText(LoginActivity.this, "User is not registered!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sorry! Couldn't log in!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
