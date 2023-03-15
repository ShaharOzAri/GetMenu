package com.example.getmenu.ui.post;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getmenu.MobileNavigationDirections;
import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.ProfileFragment;
import com.example.getmenu.R;
import com.example.getmenu.databinding.FragmentDisplayPostsBinding;
import com.example.getmenu.databinding.FragmentHomeBinding;
import com.example.getmenu.ui.home.HomeFragmentDirections;
import com.example.getmenu.ui.post.DisplayPostsFragment;
import com.example.getmenu.ui.post.DisplayPostsFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.LinkedList;
import java.util.List;

public class DisplayPostsFragment extends Fragment {
    String userId = null;
    FragmentDisplayPostsBinding binding;
    List<Post> data = new LinkedList<>();
    PostRecyclerAdapter adapter;
    DrawerLayout drawerLayout;
    public DisplayPostsFragment(String userId){
        this.userId = userId;
    }

    public DisplayPostsFragment(){
        this.userId = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDisplayPostsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        adapter = new PostRecyclerAdapter();
        binding.postrecyclerList.setHasFixedSize(true);
        binding.postrecyclerList.setAdapter(adapter);

        binding.postrecyclerList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Post ps = data.get(pos);
                MobileNavigationDirections.ActionGlobalShowPostFragment action = DisplayPostsFragmentDirections.actionGlobalShowPostFragment(ps.getId(),ps.getTitle(),ps.getUserName(),ps.getUserId(),ps.getPostImageUrl(),ps.getUserProfileUrl());
                Navigation.findNavController(view).navigate(action);

            }
        });

        reloadData();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }

    void reloadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Model.instance().getAllPosts((postList)->{
            data = postList;
            adapter.setData(data);
            binding.progressBar.setVisibility(View.GONE);
        });
    }


    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView userName;
        ImageView postImageUrl;
        ImageView userProfileUrl;

        public PostViewHolder(@NonNull View itemView , OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.postlistrow_title_tv);
            userName = itemView.findViewById(R.id.postlistrow_name_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });
        }

        public void bind(Post post , int pos) {
            title.setText(post.getTitle());
            userName.setText(String.valueOf(post.getUserName()));
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{
        OnItemClickListener Listener;
        List<Post> posts;

        public void setData(List<Post> data){
            this.posts = data;
            notifyDataSetChanged();
        }
        void setOnItemClickListener(OnItemClickListener listener){
            this.Listener = listener;
        }
        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row,parent,false);
            return new PostViewHolder(view , Listener);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            Post post = data.get(position);
            holder.bind(post , position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}