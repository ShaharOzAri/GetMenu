package com.example.getmenu.Model;

import androidx.room.Room;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.getmenu.MyApplication;
import com.example.getmenu.Model.Dao.PostDao;


@Database(entities = {Post.class}, version = 10)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(
                            MyApplication.getMyContext(),
                            AppLocalDbRepository.class,
                            "getMenu.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
