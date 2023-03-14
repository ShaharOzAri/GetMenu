package com.example.getmenu.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.getmenu.R;


public class SlideshowFragment extends Fragment {

    SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button editBtn = view.findViewById(R.id.profile_edit_btn);
        editBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_nav_slideshow_to_editProfileFragment);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}