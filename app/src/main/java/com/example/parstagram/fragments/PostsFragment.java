package com.example.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    public static final String TAG= "PostsFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(),allPosts);
        //steps to create a recycler view
        //0. create layout for one row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        //4. set the layout manager on the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(layoutManager);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        //configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data set");
                adapter.clear();
                allPosts.clear();
                queryPosts();
            }
        });
        //after the data is added in
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG,"onLoadMore"+page);
                queryPosts();
            }
        };
        //Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);
        queryPosts();
    }
    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
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
                allPosts.clear();
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

}