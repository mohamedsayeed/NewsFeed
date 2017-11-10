package com.link.newsfeed;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.link.newsfeed.connection.ConnectionManager;
import com.link.newsfeed.model.Article;
import com.link.newsfeed.model.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavView;
    private RecyclerView mRecyclerArticles;
    private ArticleAdapter mArticleAdapter;
    private ProgressDialog mProgressDialog;
    private ArrayList<Article> mArticlesArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDrawer();
        mRecyclerArticles = findViewById(R.id.articles_list);

        //Showing progress dialog
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        ConnectionManager.getInstance().getArticles().enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                // Dismiss the progress dialog
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Result result = response.body();
                mArticlesArray = result.getArticles();


                /*
                 * Updating parsed JSON data into Adapter
                 */
                mRecyclerArticles.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mArticleAdapter = new ArticleAdapter(getApplicationContext(), mArticlesArray);
                mRecyclerArticles.setAdapter(mArticleAdapter);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });


    }

    private void initializeDrawer() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mNavView = findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return true;
        if (item.getItemId() == R.id.action_search)
            return true;

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                Toast.makeText(getApplicationContext(), "Explore", Toast.LENGTH_SHORT).show();
                break;
            case R.id.live_chat:
                Toast.makeText(getApplicationContext(), "Live Chat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gallery:
                Toast.makeText(getApplicationContext(), "Gallery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wish_list:
                Toast.makeText(getApplicationContext(), "Wish List", Toast.LENGTH_SHORT).show();
                break;
            case R.id.e_magazine:
                Toast.makeText(getApplicationContext(), "E-Magazine", Toast.LENGTH_SHORT).show();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
