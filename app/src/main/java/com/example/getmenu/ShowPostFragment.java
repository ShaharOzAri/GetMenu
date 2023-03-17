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

import com.example.getmenu.Model.Post;
import com.example.getmenu.ShowPostFragmentDirections;
import com.example.getmenu.databinding.FragmentShowPostBinding;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class ShowPostFragment extends Fragment {
    FragmentShowPostBinding binding;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentShowPostBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        Post post = ShowPostFragmentArgs.fromBundle(getArguments()).getPost();

        binding.showpostEditBtn.setOnClickListener(view1 -> {
            com.example.getmenu.ShowPostFragmentDirections.ActionShowPostFragmentToEditPostFragment2 action = ShowPostFragmentDirections.actionShowPostFragmentToEditPostFragment2(post);
            Navigation.findNavController(view1).navigate(action);
        });


        TextView name = binding.ShowPostAuthorNameTv;
        name.setText(post.getUserName());
        TextView title = binding.ShowPostTitleTv;
        title.setText(post.getTitle());
        TextView description = binding.ShowPostDescriptionTv;
        description.setText(post.getDescription());
        TextView avgPrice = binding.ShowPostAvgpriceTv;
        avgPrice.setText(post.getAvgPrice());
        ImageView avatarImg = binding.ShowPostAuthorAvatarImg;
        Picasso.get().load(Uri.parse(post.getUserProfileUrl())).into(avatarImg);
        ImageView image = binding.ShowPostPostImg;
        Picasso.get().load(Uri.parse(post.getPostImageUrl())).into(image);

        return view;
    }
}