package lee.vioson.nicePic.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import lee.vioson.nicePic.R;
import lee.vioson.nicePic.utils.ActivitiSwtcher;
import lee.vioson.nicePic.utils.DataServie;
import lee.vioson.xiumm.models.Type;
import lee.vioson.xiumm.utils.DataHandler;

/**
 * Author:李烽
 * Date:2016-06-20
 * FIXME
 * Todo 第一屏（可以插广告的）
 */
public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    RelativeLayout rootView;
    private static final int FLAG_LOADING = 3;
    private static final int FLAG_LOADFAUER = 0;
    private static final int FLAG_LOADOK = 1;

    int flag = FLAG_LOADING;

    boolean animIsOver;
    ArrayList<Type> types = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        loadType();
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1000);
        rootView.setAnimation(animation);
        animation.start();
        animation.setAnimationListener(this);
    }

    private void loadType() {
        DataServie.loadTypes(new DataHandler<ArrayList<Type>>() {
            @Override
            public void onDataBack(boolean isEmpty, ArrayList<Type> types) {
                flag = FLAG_LOADOK;
                if (animIsOver) {
                    ActivitiSwtcher.toMain(SplashActivity.this, types);
                } else {
                    SplashActivity.this.types.clear();
                    SplashActivity.this.types.addAll(types);
                }
            }

            @Override
            public void onDocumentNull() {
                flag = FLAG_LOADFAUER;
                if (animIsOver)
                    ActivitiSwtcher.toMain(SplashActivity.this);
            }
        });

//        types = DataHelper2.loadTypes();
//        flag = FLAG_LOADOK;
//        DataHelper.loadTypes(new DataHelper.DataHandler<ArrayList<Type>>() {
//            @Override
//            public void onDataBack(boolean isEmpty, ArrayList<Type> types) {
//                flag = FLAG_LOADOK;
//                if (animIsOver) {
//                    ActivitiSwtcher.toMain(SplashActivity.this, types);
//                } else {
//                    SplashActivity.this.types.clear();
//                    SplashActivity.this.types.addAll(types);
//                }
//
//            }
//
//            @Override
//            public void onDocumentNull() {
//                flag = FLAG_LOADFAUER;
//                if (animIsOver)
//                    ActivitiSwtcher.toMain(SplashActivity.this);
//            }
//        });
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animIsOver = true;
        if (flag == FLAG_LOADOK)
            ActivitiSwtcher.toMain(this, types);
        else if (flag == FLAG_LOADFAUER)
            ActivitiSwtcher.toMain(this);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
