package com.example.getmenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.getmenu.Model.User;
import com.example.getmenu.databinding.FragmentHomeBinding;
import com.example.getmenu.ui.post.DisplayPostsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment {
    FragmentHomeBinding binding;
    User user;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentHomeBinding.inflate(inflater,container,false);
//        view = binding.getRoot();

        Fragment displayPostsFragment = new DisplayPostsFragment(MyApplication.user.getId());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.display_user_posts_container, displayPostsFragment).commit();

        FloatingActionButton plusFab = binding.mainAddPostBtn;


        plusFab.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_nav_home_to_addPostFragment);
        });

        return view;
    }
}