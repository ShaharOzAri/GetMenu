package com.example.getmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.getmenu.Model.FireBaseModel;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {


    Button loginBtn;
    Button notRegisteredBtn;

    TextView loginTitle;

    ImageView backgroundImageView;

    // inputs
    TextView emailInput;
    TextView passwordInput;

    // error texts
    TextView emailError;
    TextView passwordError;
    TextView authenticationError;

    //loader
    ProgressBar progressBar;

    //
    boolean validEmail = false;
    boolean validPassword = false;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        notRegisteredBtn = findViewById(R.id.not_registered);
        loginBtn = findViewById(R.id.login_btn);
        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);
        emailError = findViewById(R.id.login_email_error);
        passwordError = findViewById(R.id.login_password_error);
        progressBar = findViewById(R.id.progressBar);
        loginTitle = findViewById(R.id.register_title);
        authenticationError = findViewById(R.id.authentication_error);

        backgroundImageView = findViewById(R.id.login_activity_background_image_view);

        loginBtn.setOnClickListener((View view) -> {
            if (!Utils.isValidEmail(emailInput.getText())) {
                showEmailError();
            } else {
                validEmail = true;
            }

            if (!Utils.isValidPassword(passwordInput.getText())) {
                showPasswordError();
            } else {
                validPassword = true;
            }

            if (validEmail && validPassword) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                hideEmailError();
                hidePasswordError();

                loginTitle.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                FireBaseModel.loginUser(email, password, new FireBaseModel.Listener() {
                    @Override
                    public void onComplete() {
                        MyApplication.setUserData(mAuth.getUid(), new FireBaseModel.Listener<Boolean>() {
                            @Override
                            public void onComplete() {
                                goToMainActivity();
                                login_activity.this.finish();
                            }

                            @Override
                            public void onFail() {
                            }
                        });
                    }

                    @Override
                    public void onFail() {
                        progressBar.setVisibility(View.INVISIBLE);
                        showInputsError();
                    }
                });
            }
        });

        // Not registered
        notRegisteredBtn.setOnClickListener((View view) -> {
            Utils.print("User not registered, moving to register activity");

            Intent intent = new Intent(this, register_activity.class);
            startActivity(intent);
        });
    }

    public void goToMainActivity() {
        Utils.print("User logged in, moving to main activity");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Error messages
    public void showPasswordError() {
        this.passwordError.setText("Password must contains numbers and letter");
    }

    public void showEmailError() {
        this.emailError.setText("Please enter a valid email");
    }

    public void hidePasswordError() {
        this.passwordError.setVisibility(View.INVISIBLE);
    }

    public void hideEmailError() {
        this.emailError.setVisibility(View.INVISIBLE);
    }

    public void showInputsError() {
        this.authenticationError.setVisibility(View.VISIBLE);
    }
}