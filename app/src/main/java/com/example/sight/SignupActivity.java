package com.example.sight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sight.JavaClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private ImageView logo;

    private TextInputLayout nameField, emailField, passField, confirmPassField, studentIDField;
    private ProgressDialog progressDialog;
    private Spinner spinner;

    //Firebase Authentication
    private FirebaseAuth mAuth;

    //Firebase Database
    private DatabaseReference rootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameField = findViewById(R.id.fieldName);
        emailField = findViewById(R.id.fieldEmail);
        passField = findViewById(R.id.fieldPass);
        confirmPassField = findViewById(R.id.confirmPass);
        studentIDField = findViewById(R.id.studentId);

        spinner = findViewById(R.id.signupSpinner);

        Button signupButton = findViewById(R.id.signupbtn);

        mAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameField.getEditText().getText().toString().trim();
                final String email = emailField.getEditText().getText().toString().trim();
                final String sID = studentIDField.getEditText().getText().toString().trim();
                String pass = passField.getEditText().getText().toString();
                String confirmPass = confirmPassField.getEditText().getText().toString();
                final String type = spinner.getSelectedItem().toString();

                if(!checkInvalidInput(name, email, pass, confirmPass)){
                    progressDialog = new ProgressDialog(SignupActivity.this);
                    progressDialog.setTitle("Signing up");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                rootReference = FirebaseDatabase.getInstance().getReference();

                                User data = new User();
                                data.setAdmin("false");
                                data.setName(name);
                                data.setEmail(email);
                                if(type.equals("Student")) data.setType("student");
                                data.setUid(uid);
                                data.setStudentID(""+sID);

                                rootReference.child("users").child("student").child(uid).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();
                                            FirebaseAuth.getInstance().signOut();

                                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                            Toast.makeText(SignupActivity.this,
                                                    "Registration completed. Please log in.",
                                                    Toast.LENGTH_LONG).show();

                                            startActivity(i);
                                        } else {
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(SignupActivity.this, "This Email is already registered",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean checkValidEmail(String email){

        //Valid Email Pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
        if(email.matches(emailPattern)){
            return true;
        }
        return false;
    }

    private boolean checkInvalidInput(String name, String email, String pass, String confirmPass){

        boolean ret = false;

        if(name.isEmpty()){
            //nameField.setError("Field can't be empty!");
            ret = true;
        } else {
            nameField.setError(null);
        }

        if(pass.length() < 6){
            if(pass.isEmpty()){
                //passField.setError("Field can't be empty!");
            } else {
                //passField.setError("Password length must be at least 6");
            }
            ret = true;
        } else {
            passField.setError(null);
        }

        if(!pass.equals(confirmPass) || confirmPass.isEmpty()){
            if(confirmPass.isEmpty()){
                //confirmPassField.setError("Field can't be empty!");
            } else {
                //passField.setError("Passwords didn't match!");
                //confirmPassField.setError("Passwords didn't match!");
            }
            ret = true;
        } else {
            confirmPassField.setError(null);
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
}
