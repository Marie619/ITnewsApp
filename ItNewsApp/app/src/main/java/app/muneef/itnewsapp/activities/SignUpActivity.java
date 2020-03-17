package app.muneef.itnewsapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.models.Users;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    TextInputEditText edtTxtSignupUsername, edtTxtSignupEmail, edtTxtSignupPassword, edtTxtSignupCnfrmPassword;
    Button btnSignUp;
    TextView txtGetLogin;
    ProgressBar pbSignupLoading;
    RadioButton rdBtnMale, rdBtnFemale;

    String username, email, password, cpassword, setGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        edtTxtSignupUsername = findViewById(R.id.edtTxtSignupUsername);
        edtTxtSignupEmail = findViewById(R.id.edtTxtSignupEmail);
        edtTxtSignupPassword = findViewById(R.id.edtTxtSignupPassword);
        edtTxtSignupCnfrmPassword = findViewById(R.id.edtTxtSignupCnfrmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtGetLogin = findViewById(R.id.txtGetLogin);
        pbSignupLoading = findViewById(R.id.pbSignupLoading);
        rdBtnFemale = findViewById(R.id.rdBtnFemale);
        rdBtnMale = findViewById(R.id.rdBtnMale);

        txtGetLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                email = edtTxtSignupEmail.getText().toString();
                password = edtTxtSignupPassword.getText().toString();
                username = edtTxtSignupUsername.getText().toString();
                cpassword = edtTxtSignupCnfrmPassword.getText().toString();

                if (username.isEmpty()) {
                    edtTxtSignupUsername.setError("Please enter username");
                } else if (email.isEmpty()) {
                    edtTxtSignupEmail.setError("Please enter email");
                } else if (!email.contains("@")) {
                    edtTxtSignupEmail.setError("Please enter valid email");
                } else if (password.isEmpty()) {
                    edtTxtSignupPassword.setError("Please enter password");
                } else if (cpassword.isEmpty()) {
                    edtTxtSignupCnfrmPassword.setError("Please confirm password");
                } else if (!password.equals(cpassword)) {
                    edtTxtSignupCnfrmPassword.setError("Password not match!");
                } else {

                    setGender();
                    pbSignupLoading.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Users user = new Users(uid, username, email, setGender, null);
                                databaseReference.child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        pbSignupLoading.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        pbSignupLoading.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                            pbSignupLoading.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
    }

    private void setGender() {

        if (rdBtnMale.isChecked()) {

            setGender = "Male";
        } else if (rdBtnFemale.isChecked()) {
            setGender = "Female";
        }
    }
}
