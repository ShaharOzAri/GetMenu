package com.example.getmenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.Model.User;
import com.example.getmenu.databinding.FragmentHomeBinding;
import com.example.getmenu.databinding.FragmentProfileBinding;
import com.example.getmenu.databinding.FragmentUserProfileBinding;
import com.example.getmenu.ui.post.DisplayPostsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {
    FragmentUserProfileBinding binding;
    User user = new User();
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        binding = FragmentUserProfileBinding.inflate(inflater,container,false);
//        view = binding.getRoot();

        String userId = UserProfileFragmentArgs.fromBundle(getArguments()).getUserId();

        Model.instance().getUserById(userId, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {

                TextView nameTv = view.findViewById(R.id.profile_name_tv);
                nameTv.setText(user.getName());

                TextView emailTv = view.findViewById(R.id.profile_email_tv);
                emailTv.setText(user.getEmail());

                ImageView profileImage = view.findViewById(R.id.profile_img);

                if(user != null && !user.getProfileImageUrl().isEmpty()){
                    Picasso.get().load(user.getProfileImageUrl()).noPlaceholder().into(profileImage);
                }
            }
        });


        Fragment displayPostsFragment = new DisplayPostsFragment(userId);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.display_user_posts_container, displayPostsFragment).commit();


        return view;
    }
}