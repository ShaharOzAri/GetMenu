package com.example.getmenu.Model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    private static final Model _instance = new Model();
    List<com.example.getmenu.Model.Post> data = new LinkedList<>();
     public static Model instance(){
        return _instance;
    }

    private Model(){
         for(int i=0;i<20;i++){
             addPost(new Post(i));
         }
    }
    public List<com.example.getmenu.Model.Post> getAllPosts(){
        return data;
    }

    public com.example.getmenu.Model.Post getPost(int i){
        return data.get(i);
    }

    public void addPost(com.example.getmenu.Model.Post post){
        data.add(post);
    }

    public void removeStudent(com.example.getmenu.Model.Post post){
        data.remove(post);
    }




}
