package com.example.getmenu;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NamedNavArgument;
import androidx.navigation.NavArgument;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class ShowPostFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_show_post, container, false);

        String postUserName = ShowPostFragmentArgs.fromBundle(getArguments()).getPostUserName();
        String postTitle = ShowPostFragmentArgs.fromBundle(getArguments()).getPostTitle();
        String postAvatarImage = ShowPostFragmentArgs.fromBundle(getArguments()).getPostUserProfileUrl();
        String postImage = ShowPostFragmentArgs.fromBundle(getArguments()).getPostImageUrl();

        Button editBtn = view.findViewById(R.id.showpost_edit_btn);
        editBtn.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_showPostFragment_to_editPostFragment2);
        });

//        ImageView userImage = view.findViewById(R.id.ShowPost_author_avatar_img);
//        userImage.setOnClickListener(view1 -> {
//            MobileNavigationDirections.ActionGlobalNavProfile action = ShowPostFragmentDirections.actionGlobalNavProfile("dvb");
//            Navigation.findNavController(view).navigate(action);
//        });

        TextView name = view.findViewById(R.id.ShowPost_author_name_tv);
        name.setText(postUserName);
        TextView title = view.findViewById(R.id.ShowPost_title_tv);
        title.setText(postTitle);
        ImageView avatarImg = view.findViewById(R.id.ShowPost_author_avatar_img);
        Picasso.get().load(Uri.parse(postAvatarImage)).into(avatarImg);
        ImageView image = view.findViewById(R.id.ShowPost_post_img);
        Picasso.get().load(Uri.parse(postImage)).into(image);

        return view;
    }
}