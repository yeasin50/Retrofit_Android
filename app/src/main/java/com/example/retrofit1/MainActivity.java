package com.example.retrofit1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    String baseUrl = "https://jsonplaceholder.typicode.com/";

    private JsonPlaceholderApi jsonPlaceholderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        // to patch null
        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();


        jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

//        getPosts();
//        getComments();

//        creatEPost();

//        updatePost();
        deletePost();
    }

    private void deletePost() {

        Call<Void>  call = jsonPlaceholderApi.delete(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textView.setText(""+response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textView.setText(""+t.getMessage());
            }
        });
    }

    private void updatePost() {
        Post post = new Post(12,null, "new Txt");

        Call<Post> cal2l = jsonPlaceholderApi.putPost(5,post);
        Call<Post> call = jsonPlaceholderApi.patchPost(5,post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful())
                {
                    textView.setText(""+response.code());
                    return;
                }

                Post post = response.body();
                String content ="";
                content += "ID: " + post.getId() + "\n";
                content += "User ID: " + post.getUserId() + "\n";
                content += "Title: " + post.getTitle() + "\n";
                content += "Text: " + post.getText() + "\n\n";

                textView.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private void creatEPost() {

        Post post = new Post(33,"Title here", "Text here");
//        Call<Post> call = jsonPlaceholderApi.createPost(post);

        Call<Post> call = jsonPlaceholderApi.createPost(23,"tt","bodyss");

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    textView.setText("code"+response.code());
                return;
                }

                Post post1 = response.body();

                String content  ="";
                content += "Code: "+ response.code()+"\n";
                content += "ID: "+ post1.getId()+"\n";
                content += "UserId: "+ post1.getUserId()+"\n";
                content += "Title: "+ post1.getTitle()+"\n";
                content += "Text: "+ post1.getText()+"\n";

                textView.append(content);

            }



            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    private void getPosts(){
        //ignore null
        Map<String , String> map = new HashMap<>();
        map.put("userId","1");
        map.put("_sort","id");
        map.put("_order","desc");

//        Call<List<Post>> call = jsonPlaceholderApi.getPost(new Integer[]{3,4},"id","desc");
        Call<List<Post>> call = jsonPlaceholderApi.getPost( map);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code: "+ response.code());

                    return;
                }

                List<Post> postList = response.body();

                for (Post post: postList){
                    String content ="";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textView.append(content);

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

                textView.setText(t.getMessage());
            }
        });
    }

    private void getComments() {
        //full url also work same
        Call<List<Comment>> call = jsonPlaceholderApi.getComments("posts/3/comments");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code: "+ response.code());
                    return;
                }

                List<Comment> comments = response.body();
                for (Comment comment : comments) {
                    String content = "";
                    content += "ID: " + comment.getId() + "\n";
                    content += "Post ID: " + comment.getPostId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";

                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
}
