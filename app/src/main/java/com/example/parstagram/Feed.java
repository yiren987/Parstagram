package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class Feed extends AppCompatActivity {

    public static final String TAG = "FeedActivity";
    private TextView tvDescription;
    private Button btnOtherlayout;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        tvDescription= findViewById(R.id.tvDescription);
        ivImage= findViewById(R.id.ivImage);
        btnOtherlayout= findViewById(R.id.btnOtherlayout);
        btnOtherlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { goMainActivity();}
        });
        queryPosts(this);
    }

    private void goMainActivity() {
        Intent i= new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void queryPosts(Context context) {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null){
                    Log.e(TAG,"Issue with getting posts",e);
                    return;
                }
                for(Post post : posts) {
                    Log.i(TAG,"Post: "+ post.getDescription()+ ", username: "+ post.getUser().getUsername());
                    Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
                }
            }
        });
    }
}