package com.example.getmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.getmenu.Model.FireBaseModel;
import com.example.getmenu.Model.Model;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getmenu.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    View menuHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        menuHeader = navigationView.getHeaderView(0);


        if(MyApplication.user!=null && !MyApplication.user.getProfileImageUrl().isEmpty()){
            TextView nameTv = menuHeader.findViewById(R.id.profile_name_tv);
            nameTv.setText(MyApplication.user.getName());

            CircleImageView profileImage = menuHeader.findViewById(R.id.profile_image_view);
            Picasso.get().load(MyApplication.user.getProfileImageUrl()).noPlaceholder().into(profileImage);
        }

        Button logoutBtn = menuHeader.findViewById(R.id.logout_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance().signOut(new Model.OnSignOutListener() {
                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(view.getContext(), login_activity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                });

            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}