package com.example.testmvp.detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.testmvp.R;
import com.example.testmvp.model.Result;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

/**
 * Created by Praveen
 */

public class DetailsActivity extends Activity implements DetailsView {

    //private ProgressBar progressBar;
    private DetailsPresenter presenter;
    DetailsActivity mActivity;
    CollapsingToolbarLayout collapsingToolbarLayout ;
    AppBarLayout appBarLayout;
    CoordinatorLayout coordinatorLayout;
    ImageView articlePic;
    Result result;
    TextView title_txt, abstract_txt,keyword_txt,section_txt,date_txt;
    ProgressBar progressBar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            result = (Result) bundle.getSerializable("result");
        }

        title_txt = (TextView)findViewById(R.id.txt_title);
        abstract_txt = (TextView)findViewById(R.id.txt_abstractKey);
        keyword_txt = (TextView)findViewById(R.id.txt_keywords);
        section_txt = (TextView)findViewById(R.id.txt_section);
        date_txt = (TextView)findViewById(R.id.txt_date);
        articlePic = (ImageView) findViewById(R.id.article_pic);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = result.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(Intent.createChooser(intent, "Open Link"));
            }
        });

        initCollapsingToolbar(result);
        presenter = new DetailsPresenterImpl(this,new DetailsCommunicatorImpl());
        presenter.viewCreated();
    }

    private void initCollapsingToolbar(Result result) {
        final Display dWidth = getWindowManager().getDefaultDisplay();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        collapsingToolbarLayout.setTitle(result.getByline());
        title_txt.setText(result.getTitle());
        abstract_txt.setText(result.getAbstract());
        keyword_txt.setText(result.getAdxKeywords());
        section_txt.setText(result.getSection());
        date_txt.setText(result.getPublishedDate());


        Picasso.get().load( result.getMedia().get(0).getMediaMetadata().get(3).getUrl()).into(articlePic);
        Picasso/*.with(getApplicationContext())*/
                .get()
                .load(result.getMedia().get(0).getMediaMetadata().get(3).getUrl())
                .error(R.drawable.header)
                .into(articlePic, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        articlePic.getLayoutParams().height = dWidth.getWidth();

        try {
            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            Log.e(TAG, "onCreate: failed to create bitmap from background", e.fillInStackTrace());
        }


        appBarLayout.setExpanded(true);

        appBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = dWidth.getWidth() * 1 / 3;
                setAppBarOffset(heightPx);
            }
        });

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                } else if (isShow) {
                    isShow = false;
                }
            }
        });
    }

    private void setAppBarOffset(int offsetPx) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(coordinatorLayout, appBarLayout, null, 0, offsetPx, new int[]{0, 0});
    }


    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void navigateToHome() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

}
