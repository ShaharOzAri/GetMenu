package com.example.getmenu;

import android.app.Application;
import android.content.Context;
import android.text.format.Time;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.getmenu.Model.ExchangeRateModel;
import com.example.getmenu.Model.FireBaseModel;
import com.example.getmenu.Model.Post;
import com.example.getmenu.Model.User;
import com.example.getmenu.ui.post.DisplayPostsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class MyApplication extends Application {
    public static User user = new User();
    public static FirebaseUser firebaseUserUid = null;
    public static Context context = null;

    public static Context getMyContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static void setUserData(String UUId, FireBaseModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(User.COLLECTION_NAME).document(UUId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (user == null) {
                        user = new User();
                    }

                    user.setName((String) task.getResult().get("name"));
                    user.setEmail((String) task.getResult().get("email"));
                    user.setId(UUId);
                    user.setProfileImageUrl((String) task.getResult().get("profileImageUrl"));
                }

                listener.onComplete();
            }
        });
    }

    public static User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static void signOut() {
        user = null;
        firebaseUserUid = null;
    }
}
