package com.example.getmenu.ui.post;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getmenu.MobileNavigationDirections;
import com.example.getmenu.Model.ExchangeRate;
import com.example.getmenu.Model.ExchangeRateModel;
import com.example.getmenu.Model.Model;
import com.example.getmenu.Model.Post;
import com.example.getmenu.Model.User;
import com.example.getmenu.MyApplication;
import com.example.getmenu.R;
import com.example.getmenu.Utils;
import com.example.getmenu.databinding.FragmentDisplayPostsBinding;

import com.squareup.picasso.Picasso;

import java.util.Date;

public class DisplayPostsFragment extends Fragment {
    String userId = null;
    FragmentDisplayPostsBinding binding;
    PostRecyclerAdapter adapter;
    DisplayPostViewModel displayPostViewModel;
    View view;

    MutableLiveData<Float> exchangeRate;

    MutableLiveData<Character> currencySymbol;

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

        exchangeRate = new MutableLiveData<>();
        exchangeRate.setValue(new Float(1));

        currencySymbol = new MutableLiveData<>();
        currencySymbol.setValue('$');
        binding.currencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.currencyBtn.getText().toString().contains("ILS")){
                    LiveData<Float> res = ExchangeRateModel.instance.getExchangeRate("USD","ILS");
                    binding.progressBar.setVisibility(View.VISIBLE);
                    res.observe(getViewLifecycleOwner(),(Float)->{
                        exchangeRate.setValue(Float);
                        currencySymbol.setValue('₪');
                        refresh();
                    });
                    binding.currencyBtn.setText("Switch prices to USD");
                }else{
                    binding.progressBar.setVisibility(View.VISIBLE);
                    exchangeRate.setValue(new Float(1));
                    currencySymbol.setValue('$');
                    refresh();
                    binding.currencyBtn.setText("Switch prices to ILS");
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });

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
        binding.progressBar.setVisibility(View.GONE);

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
            Float price = (Float.parseFloat(post.getAvgPrice())*exchangeRate.getValue());
            Character symbol = currencySymbol.getValue();
            avgPrice.setText(String.format("%.2f", price) + " " + symbol);
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