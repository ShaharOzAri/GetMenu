package com.example.getmenu.ui.post;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.Model.User;
import com.example.getmenu.MyApplication;
import com.example.getmenu.databinding.FragmentShowPostBinding;
import com.squareup.picasso.Picasso;

public class ShowPostFragment extends Fragment {
    FragmentShowPostBinding binding;
    View view;
    User postUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentShowPostBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        Post post = ShowPostFragmentArgs.fromBundle(getArguments()).getPost();
        Model.instance().getUserById(post.getUserId(), new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                TextView name = binding.ShowPostAuthorNameTv;
                name.setText(user.getName());
                ImageView avatarImg = binding.ShowPostAuthorAvatarImg;
                Picasso.get().load(Uri.parse(user.getProfileImageUrl())).into(avatarImg);
            }
        });

        if(!post.getUserId().equals(MyApplication.user.getId())){
            binding.showpostEditBtn.setVisibility(view.GONE);
        }

        binding.showpostEditBtn.setOnClickListener(view1 -> {
            ShowPostFragmentDirections.ActionShowPostFragmentToEditPostFragment2 action = ShowPostFragmentDirections.actionShowPostFragmentToEditPostFragment2(post);
            Navigation.findNavController(view1).navigate(action);
        });


        TextView title = binding.ShowPostTitleTv;
        title.setText(post.getTitle());
        TextView description = binding.ShowPostDescriptionTv;
        description.setText(post.getDescription());
        TextView avgPrice = binding.ShowPostAvgpriceTv;
        avgPrice.setText(post.getAvgPrice());
        ImageView image = binding.ShowPostPostImg;
        Picasso.get().load(Uri.parse(post.getPostImageUrl())).into(image);

        return view;
    }
}