package com.example.getmenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.getmenu.Model.Post;
import com.example.getmenu.Model.User;
import com.example.getmenu.databinding.FragmentHomeBinding;
import com.example.getmenu.databinding.FragmentProfileBinding;
import com.example.getmenu.ui.post.DisplayPostsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    User user = new User();
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.inflate(inflater,container,false);
//        view = binding.getRoot();
        Post post = ProfileFragmentArgs.fromBundle(getArguments()).getPost();

//        user.setId(ProfileFragmentArgs.fromBundle(getArguments()).getUserId());
//        user.setEmail(ProfileFragmentArgs.fromBundle(getArguments()).getUserEmail());
//        user.setName(ProfileFragmentArgs.fromBundle(getArguments()).getUserName());
//        user.setProfileImageUrl(ProfileFragmentArgs.fromBundle(getArguments()).getUserProfileImage());


        Fragment displayPostsFragment = new DisplayPostsFragment(MyApplication.user.getId());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.display_user_posts_container, displayPostsFragment).commit();

        binding.profileEmailTv.setText(user.getEmail());
        binding.profileNameTv.setText(user.getName());
        if(user != null && !user.getProfileImageUrl().isEmpty()){
            Picasso.get().load(user.getProfileImageUrl()).noPlaceholder().into(binding.profileImg);
        }
        if(!user.getId().equals(MyApplication.user.getId())){
            binding.profileEditBtn.setVisibility(View.GONE);
        }


        return view;
    }
}