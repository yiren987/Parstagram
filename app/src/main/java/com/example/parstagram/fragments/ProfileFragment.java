package com.example.parstagram.fragments;

import android.util.Log;

import com.example.parstagram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment{
    @Override
    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null){
                    Log.e(TAG,"Issue with getting posts",e);
                    return;
                }
                for(Post post : posts) {
                    Log.i(TAG,"Post: "+ post.getDescription()+ ", username: "+ post.getUser().getUsername());
                    Log.i(TAG,"Post: "+ post.getDescription()+ ", username: "+ post.getImage().getUrl());
                }
                swipeContainer.setRefreshing(false);
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
