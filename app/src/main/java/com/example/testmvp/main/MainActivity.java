package com.example.testmvp.main;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.testmvp.R;
import com.example.testmvp.TestMvp;
import com.example.testmvp.adapter.NewsRecylervViewAdapter;
import com.example.testmvp.data.ApiClient;
import com.example.testmvp.data.ApiInterface;
import com.example.testmvp.detail.DetailsActivity;
import com.example.testmvp.generic.ConnectivityReceiver;
import com.example.testmvp.model.GetDataReponse;
import com.example.testmvp.model.Result;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener,
        ConnectivityReceiver.ConnectivityReceiverListener{
    MainPresenter mainPresenter;
    Toolbar toolbar;
    RecyclerView recylerView;
    private ProgressBar progressBar;
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "72a1f5f1b2bc43dcab344a4f9b4f3f13";
    NewsRecylervViewAdapter newsRecylervViewAdapter;
    DrawerLayout drawer;
    List<Result> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recylerView=(RecyclerView) findViewById(R.id.recylerView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        setSupportActionBar(toolbar);

        setUpNavigationMenu();

        if(!ConnectivityReceiver.isConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No internet connection");
            builder.setMessage("Please check your internet connection and try again.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    MainActivity.this.finish();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }else{
            mainPresenter = new MainPresenterImpl(this, new MainCommunicatorImpl());
            setUpRecylerView();
            mainPresenter.onViewCreated();
        }
    }

    private void getdata() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<GetDataReponse> call = apiService.getNews();
        call.enqueue(new Callback<GetDataReponse>() {
            @Override
            public void onResponse(Call<GetDataReponse>call, Response<GetDataReponse> response) {
                try {
                    newsList = response.body().getResults();
                    newsRecylervViewAdapter.addData(newsList);
                    newsRecylervViewAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    Log.d("SSS", "Number of News received: " + response.body().getResults().toString());
                }catch (IllegalStateException e){
                    Log.d("SSS", "Number of News received: " + e);
                }
            }

            @Override
            public void onFailure(Call<GetDataReponse> call, Throwable t) {
                Log.d("@@@", "Number of News received: " + t.getMessage());
            }
        });
    }

    private void setUpRecylerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recylerView.setLayoutManager(layoutManager);
        newsRecylervViewAdapter = new NewsRecylervViewAdapter(getApplicationContext());
        recylerView.setItemAnimator(new DefaultItemAnimator());
        recylerView.setAdapter(newsRecylervViewAdapter);
        newsRecylervViewAdapter.notifyDataSetChanged();
        newsRecylervViewAdapter.onSetonItemClickListner(new NewsRecylervViewAdapter.onItemClickListner() {
            @Override
            public void onItemClicked(int position, List<Result> getDataReponseList) {
                Result _Result=getDataReponseList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("result", _Result);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });
    }

    private void setUpNavigationMenu() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        TextView welcome_name = (TextView)headerView.findViewById(R.id.txt_name);
        TextView email = (TextView)headerView.findViewById(R.id.txt_email); //adress.setVisibility(View.GONE);
        TextView first_char = (TextView)headerView.findViewById(R.id.first_char);

        welcome_name.setText("Praveen Vanapalli");
        email.setText("vanapallipraveenk@gmail.com");
        StringTokenizer strToken = new StringTokenizer(welcome_name.getText().toString()," ");
        char[] fArray = strToken.nextToken().toCharArray();
        char[] lArray = strToken.nextToken().toCharArray();
        first_char.setText(fArray[0]+" "+lArray[0]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                Toast.makeText(MainActivity.this,"Search Clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Toast.makeText(MainActivity.this,"Settings Clicked",Toast.LENGTH_SHORT).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        /*progressBar.setVisibility(View.GONE);*/
    }

    @Override
    public void mainPage() {
        getdata();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.home) {
            Toast.makeText(MainActivity.this,"Home Clicked",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.profile) {
            Toast.makeText(MainActivity.this,"Profile Clicked",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.settings) {
            Toast.makeText(MainActivity.this,"Settings Clicked",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        }
    }

    protected void onResume(){
        super.onResume();
        if(ConnectivityReceiver.isConnected()){
            TestMvp.getInstance().setConnectivityListener(this);
        }
    }
}
