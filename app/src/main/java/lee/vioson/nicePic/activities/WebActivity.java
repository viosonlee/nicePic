package lee.vioson.nicePic.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import lee.vioson.nicePic.BuildConfig;
import lee.vioson.nicePic.R;
import lee.vioson.widget.ProgressWebView;

/**
 * Author:李烽
 * Date:2016-06-20
 * FIXME
 * Todo
 */
public class WebActivity extends AppCompatActivity {

    public static final String TITLE = "title";
    public static final String URL = "url";
    //    @BindView(R.id.ll_parent)
    LinearLayout parent;
    //    @BindView(R.id.web_view)
    ProgressWebView mWebView;
    private WebSettings settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
//        ButterKnife.bind(this);
        parent = (LinearLayout) findViewById(R.id.ll_parent);
        mWebView = (ProgressWebView) findViewById(R.id.web_view);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.getSettings().setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setBuiltInZoomControls(true);                      //support zoom
        settings.setLoadWithOverviewMode(true);
        settings.setUserAgentString(settings.getUserAgentString() + " NICEPIC/" + BuildConfig.VERSION_NAME);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("tel:")) {
                    Intent dialIntent = new Intent();
                    dialIntent.setAction(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse(url));
                    startActivity(dialIntent);
                } else {
                    mWebView.loadUrl(url);
                }
                return true;
            }
        });
        if (getIntent().getExtras() != null) {
            String title = getIntent().getExtras().getString(WebActivity.TITLE);
            if (!TextUtils.isEmpty(title)) {
                Log.e(getClass().getName(), title);
                this.setTitle(title);
            }
            String url = getIntent().getExtras().getString(WebActivity.URL);
            if (!TextUtils.isEmpty(url)) {
                Log.e(getClass().getName(), url);
                mWebView.loadUrl(url);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
        }
    }


    //控制返回键
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
