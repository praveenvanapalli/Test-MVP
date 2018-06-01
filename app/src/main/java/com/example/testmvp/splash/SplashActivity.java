package com.example.testmvp.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.testmvp.R;
import com.example.testmvp.main.MainActivity;


public class SplashActivity extends Activity implements SplashView {

   // private ProgressBar progressBar;
    private SplashPresenter presenter;
    private TextView title;
    Animation animFadeIn,animZoomIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh_act);

       // progressBar = (ProgressBar) findViewById(R.id.progress);
        title = (TextView) findViewById(R.id.logo_txt);

        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
/*        animFadeIn.setAnimationListener(this);
        animZoomIn.setAnimationListener(this);*/

        presenter = new SplashPresenterImpl(this,new SplashCommunicatorImpl());
        presenter.viewCreated();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

   /* @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }*/



    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showAnimation() {
        title.startAnimation(animFadeIn);
    }

    @Override
    public void showZoomAnim() {
        title.startAnimation(animZoomIn);
    }


}
