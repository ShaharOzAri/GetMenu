package com.example.getmenu.ui.home;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
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

import com.example.getmenu.AddPostFragment;
import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.R;
import com.example.getmenu.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    HomeViewModel homeViewModel;
    List<Post> data;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton plusFab = view.findViewById(R.id.main_add_post_btn);
        RecyclerView list = view.findViewById(R.id.postrecycler_list);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(view.getContext()));
        PostRecyclerAdapter adapter = new PostRecyclerAdapter();
        data = Model.instance().getAllPosts();
        plusFab.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_addPostFragment);
        });

        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Post ps = data.get(pos);
                HomeFragmentDirections.actionNavHomeToAddPostFragment();
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_showPostFragment);
            }
        });

        return view;

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