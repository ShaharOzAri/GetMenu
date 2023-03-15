package com.example.getmenu;

import android.app.Application;
import android.content.Context;

import com.example.getmenu.Model.User;

public class MyApplication extends Application {
    public static User user = new User();

    static private Context context;
    public static Context getMyContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
