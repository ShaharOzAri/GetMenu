package com.example.getmenu.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.getmenu.R;
import com.example.getmenu.databinding.FragmentHomeBinding;
import com.example.getmenu.ui.post.DisplayPostsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    View view;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        Fragment displayPostsFragment = new DisplayPostsFragment(null);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.display_user_posts_container, displayPostsFragment).commit();

        FloatingActionButton plusFab = binding.mainAddPostBtn;

        plusFab.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_nav_home_to_addPostFragment);
        });

        return view;

    }
}