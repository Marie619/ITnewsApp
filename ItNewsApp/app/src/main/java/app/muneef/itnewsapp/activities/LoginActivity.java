package app.muneef.itnewsapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.preferences.PreferenceManager;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edtTxtLoginEmail, edtTxtLoginPassword;
    Button btnLogin;
    TextView txtGetRegistered;
    ProgressBar pbLoginLoading;

    FirebaseAuth auth;
    PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(this);

        if (preferenceManager.userIsLogged()) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

        edtTxtLoginEmail = findViewById(R.id.edtTxtLoginEmail);
        edtTxtLoginPassword = findViewById(R.id.edtTxtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtGetRegistered = findViewById(R.id.txtGetRegistered);
        pbLoginLoading = findViewById(R.id.pbLoginLoading);

        txtGetRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtTxtLoginEmail.getText().toString();
                final String password = edtTxtLoginPassword.getText().toString();

                if (email.isEmpty()) {
                    edtTxtLoginEmail.setError("Please enter email");
                } else if (!email.contains("@")) {
                    edtTxtLoginEmail.setError("Please enter valid email");
                } else if (password.isEmpty()) {
                    edtTxtLoginPassword.setError("Please enter password");
                } else {
                    //TODO: Server code here...
                    pbLoginLoading.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                pbLoginLoading.setVisibility(View.INVISIBLE);

                                Toast.makeText(LoginActivity.this, "LoginSuccess", Toast.LENGTH_SHORT).show();
                                preferenceManager.setUserLog(email, password, true);
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            } else {
                                pbLoginLoading.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "Please check your email and password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pbLoginLoading.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Login failed please check network", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}
