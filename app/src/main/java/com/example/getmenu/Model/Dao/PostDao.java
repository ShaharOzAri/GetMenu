package com.example.getmenu.Model.Dao;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import com.example.getmenu.Model.Post;


@Dao
public interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... post);

    @Query("SELECT * FROM Post ORDER BY timestamp DESC")
    List<Post> getAllPosts();

    @Query("select * from Post where id == :postId LIMIT 1")
    List<Post> getPostById(String postId);

    @Query("select * from Post where userId == :userId ORDER BY timestamp DESC")
    List<Post> getPostsByUserId(String userId);

    @Query("select exists(select * from Post where id = :postId)")
    boolean isPostExists(String postId);

    @Query("delete from Post where id == :postId")
    void deleteById(String postId);

    @Query("delete from Post")
    void deleteAll();
}
