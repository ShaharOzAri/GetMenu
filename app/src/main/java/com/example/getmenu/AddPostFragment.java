package com.example.getmenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.ui.home.HomeFragment;

public class AddPostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_post, container, false);
        EditText nameEt = view.findViewById(R.id.addPost_title_pt);
        Button saveBtn = view.findViewById(R.id.addPost_save_btn);
        Button cancelBtn = view.findViewById(R.id.addPost_cancel_btn);

        saveBtn.setOnClickListener(view1 -> {
            String name = nameEt.getText().toString();

        });

//        cancelBtn.setOnClickListener(view1 -> finish());

        return view;
    }
}