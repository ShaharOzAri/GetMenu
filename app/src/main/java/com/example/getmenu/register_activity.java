package com.example.getmenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getmenu.Model.FireBaseModel;
import com.example.getmenu.Model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class register_activity extends AppCompatActivity {

    FireBaseModel modelFireBase;

    // Buttons
    Button registerBtn;
    Button alreadyRegisterBtn;

    // title
    TextView registerTitle;

    // inputs
    TextView emailInput;
    TextView passwordInput;
    TextView nameInput;

    // Profile image
    CircleImageView profileImage;
    Uri profileImageUri = null;

    // error texts
    TextView emailError;
    TextView passwordError;
    TextView userNameError;
    TextView imageError;

    //loader
    ProgressBar progressBar;

    // input validation
    boolean validEmail = false;
    boolean validPassword = false;
    boolean validUserName = false;

    // background image
    ImageView backgroundImageView;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            currentUser.reload();
            MyApplication.firebaseUserUid = currentUser;
            goToMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase
        modelFireBase = new FireBaseModel();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        alreadyRegisterBtn = findViewById(R.id.already_registered);
        registerBtn = findViewById(R.id.register_btn);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        emailError = findViewById(R.id.register_email_error);
        passwordError = findViewById(R.id.register_password_error);
        progressBar = findViewById(R.id.progressBar);
        registerTitle = findViewById(R.id.login_title);
        profileImage = findViewById(R.id.row_list_profile_image);
        nameInput = findViewById(R.id.register_name);
        userNameError = findViewById(R.id.register_name_error);
        imageError = findViewById(R.id.image_error);

        backgroundImageView = findViewById(R.id.register_activity_background_image_view);

        registerBtn.setOnClickListener((View view) -> {
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

            if (!(nameInput.getText().length() > 0)) {
                showUserNameError();
            } else {
                validUserName = true;
            }

            if (profileImageUri == null) {
                showImageError();
            }

            if (validEmail && validPassword && validUserName && profileImageUri != null) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String name = nameInput.getText().toString();

                hideEmailError();
                hidePasswordError();
                hideUserNameError();
                hideImageError();

                registerTitle.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                FireBaseModel.register(email, password, new FireBaseModel.Listener<Boolean>() {
                    @Override
                    public void onComplete() {
                        Utils.print(email + " registered successfully");

                        MyApplication.user.setEmail(email);
                        MyApplication.user.setName(name);


                        Model.instance().addUser(MyApplication.user, profileImageUri, () -> {
                            goToMainActivity();
                            register_activity.this.finish();
                        });
                    }

                    @Override
                    public void onFail() {
                        Utils.print(email + " registered failed");

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MyApplication.context, "registered failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // already registered
        alreadyRegisterBtn.setOnClickListener((View view) -> {
            Utils.print("User not registered, moving to register activity");
            Intent intent = new Intent(this, login_activity.class);
            startActivity(intent);
        });

        profileImage.setOnClickListener((View view) -> {
            Utils.pickImageFromGallery(register_activity.this);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            profileImageUri = data.getData();
            profileImage.setImageURI(profileImageUri);
        }
    }

    public void goToMainActivity() {
        Utils.print("User logged in, moving to main activity");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Error messages
    public void showPasswordError() {
        this.passwordError.setVisibility(View.VISIBLE);
        this.passwordError.setText("Password must contains numbers and letter");
    }

    public void showEmailError() {
        this.emailError.setVisibility(View.VISIBLE);
        this.emailError.setText("Please enter a valid email");
    }

    public void showUserNameError() {
        this.userNameError.setVisibility(View.VISIBLE);
        this.userNameError.setText("Please enter a name");
    }

    public void showImageError() {
        this.imageError.setVisibility(View.VISIBLE);
        this.imageError.setText("Please pick an image");
    }

    public void hideImageError() {
        this.imageError.setVisibility(View.INVISIBLE);
    }

    public void hidePasswordError() {
        this.passwordError.setVisibility(View.INVISIBLE);
    }

    public void hideEmailError() {
        this.emailError.setVisibility(View.INVISIBLE);
    }

    public void hideUserNameError() {
        this.userNameError.setVisibility(View.INVISIBLE);
    }
}