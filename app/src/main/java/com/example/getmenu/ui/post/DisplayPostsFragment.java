package com.example.getmenu.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getmenu.MobileNavigationDirections;
import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.Model.User;
import com.example.getmenu.R;
import com.example.getmenu.databinding.FragmentDisplayPostsBinding;

import com.squareup.picasso.Picasso;

public class DisplayPostsFragment extends Fragment {
    String userId = null;
    FragmentDisplayPostsBinding binding;
    PostRecyclerAdapter adapter;
    DisplayPostViewModel displayPostViewModel;
    View view;

    public DisplayPostsFragment(String userId){
        this.userId = userId;
    }

    public DisplayPostsFragment(){
        this.userId = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        displayPostViewModel = new ViewModelProvider(this).get(DisplayPostViewModel.class);
        binding = FragmentDisplayPostsBinding.inflate(inflater,container,false);
        view = binding.getRoot();
        adapter = new PostRecyclerAdapter();
        binding.postrecyclerList.setHasFixedSize(true);
        binding.postrecyclerList.setAdapter(adapter);

        binding.postrecyclerList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        displayPostViewModel.getData(userId).observe(getViewLifecycleOwner(), list -> {
            refresh();
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Post ps = displayPostViewModel.getData(userId).getValue().get(pos);
                MobileNavigationDirections.ActionGlobalShowPostFragment action =
                        DisplayPostsFragmentDirections.actionGlobalShowPostFragment(ps);
                Navigation.findNavController(view).navigate(action);

            }
        });

        Model.instance().postListLoadingState.observe(getViewLifecycleOwner(),status->{
            binding.swipeRefresh.setRefreshing(status == Model.PostListLoadingState.loading);
        });

        binding.swipeRefresh.setOnRefreshListener(()->{
            reloadData();
        });
        return view;
    }


    public void refresh() {
        adapter.notifyDataSetChanged();
        binding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }

    void reloadData(){
        Model.instance().getAllPosts();
    }


    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView userName;
        ImageView postImageUrl;
        ImageView userProfileUrl;
        TextView description;
        TextView avgPrice;

        public PostViewHolder(@NonNull View itemView , OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.postlistrow_title_tv);
            userName = itemView.findViewById(R.id.postlistrow_name_tv);
            postImageUrl = itemView.findViewById(R.id.postlistrow_post_img);
            userProfileUrl = itemView.findViewById(R.id.postlistrow_avatar_img);
            description = itemView.findViewById(R.id.postlistrow_description_tv);
            avgPrice = itemView.findViewById(R.id.postlisrow_avgPrice_tv);

            userProfileUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    String id = displayPostViewModel.getData(userId).getValue().get(getAdapterPosition()).getUserId();
                    com.example.getmenu.MobileNavigationDirections.ActionGlobalUserProfileFragment action = DisplayPostsFragmentDirections.actionGlobalUserProfileFragment(id);
                    Navigation.findNavController(view1).navigate(action);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });
        }

        public void bind(Post post , int pos) {
            Model.instance().getUserById(post.getUserId(), new Model.GetUserListener() {
                @Override
                public void onComplete(User user) {
                    userName.setText(user.getName());
                    if(post != null && !user.getProfileImageUrl().isEmpty()){
                        Picasso.get().load(user.getProfileImageUrl()).noPlaceholder().into(userProfileUrl);
                    }
                }
            });
            title.setText(post.getTitle());
            description.setText(post.getDescription());
            avgPrice.setText(post.getAvgPrice());
            if(post != null && !post.getPostImageUrl().isEmpty()){
                Picasso.get().load(post.getPostImageUrl()).noPlaceholder().into(this.postImageUrl);
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{
        OnItemClickListener Listener;

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
            Post ps = displayPostViewModel.getData(userId).getValue().get(position);
            holder.bind(ps , position);
        }

        @Override
        public int getItemCount() {
            if (displayPostViewModel.getData(userId).getValue() == null) {
                return 0;
            }

            return displayPostViewModel.getData(userId).getValue().size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}