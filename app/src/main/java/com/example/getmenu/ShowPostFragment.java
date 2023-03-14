package com.example.getmenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;

import java.util.List;

public class ShowPostFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_post, container, false);

        String postId = ShowPostFragmentArgs.fromBundle(getArguments()).getPostID();

        TextView showPostId = view.findViewById(R.id.ShowPost_author_name_tv);
        showPostId.setText(postId);
        return view;

    }
}