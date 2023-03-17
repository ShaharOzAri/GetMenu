package com.example.getmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.getmenu.Model.FireBaseModel;

import com.example.getmenu.databinding.ActivityStartBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class start_activity extends AppCompatActivity {

    ImageView backgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FireBaseModel.signOut();
        ActivityStartBinding binding = ActivityStartBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        backgroundImageView = findViewById(R.id.main_background);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        new Thread() {
            public void run() {
                try {
                    //Display for 3 seconds
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if (currentUser != null) {
                        currentUser.reload();

                        MyApplication.firebaseUserUid = currentUser;
                        MyApplication.setUserData(currentUser.getUid(), new FireBaseModel.Listener < Boolean > () {
                            @Override
                            public void onComplete() {
                                goToMainActivity();
                                start_activity.this.finish();
                            }

                            @Override
                            public void onFail() {
                            }
                        });
                    } else {
                        toLoginPage();
                        start_activity.this.finish();
                    }
                }
            }
        }.start();
    }

    private void toLoginPage() {
        Intent intent = new Intent(this, login_activity.class);
        startActivity(intent);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}