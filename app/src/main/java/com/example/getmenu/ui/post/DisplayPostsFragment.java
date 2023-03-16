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
        displayPostViewModel.getData().observe(getViewLifecycleOwner(), list -> {
            refresh();
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Post ps = displayPostViewModel.getData().getValue().get(pos);
                MobileNavigationDirections.ActionGlobalShowPostFragment action =
                        DisplayPostsFragmentDirections.actionGlobalShowPostFragment(
                                ps.getId(),ps.getTitle(),ps.getUserName(),ps.getUserId(),ps.getPostImageUrl(),ps.getUserProfileUrl(),ps.getDescription(),ps.getAvgPrice());
                Navigation.findNavController(view).navigate(action);

            }
        });

        reloadData();
        return view;
    }


    public void refresh() {
        binding.progressBar.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        binding.progressBar.setVisibility(View.GONE);

//        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }

    void reloadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
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
                    Post post = new Post();
                    post = displayPostViewModel.getData().getValue().get(getAdapterPosition());
//                    Navigation.findNavController(view).navigate(DisplayPostsFragmentDirections.actionDisplayPostsFragmentToNavProfile(post));
                    com.example.getmenu.MobileNavigationDirections.ActionGlobalNavProfile action = DisplayPostsFragmentDirections.actionGlobalNavProfile(post);

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
            title.setText(post.getTitle());
            description.setText(post.getDescription());
            avgPrice.setText(post.getAvgPrice());
            if(post != null && !post.getUserProfileUrl().isEmpty()){
                Picasso.get().load(post.getUserProfileUrl()).noPlaceholder().into(this.userProfileUrl);
            }
            if(post != null && !post.getPostImageUrl().isEmpty()){
                Picasso.get().load(post.getPostImageUrl()).noPlaceholder().into(this.postImageUrl);
            }
            userName.setText(post.getUserName());
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
            Post ps = displayPostViewModel.getData().getValue().get(position);
            holder.bind(ps , position);
        }

        @Override
        public int getItemCount() {
            if (displayPostViewModel.getData().getValue() == null) {
                return 0;
            }

            return displayPostViewModel.getData().getValue().size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}