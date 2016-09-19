package lee.vioson.nicePic.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.RowIdLifetime;

import lee.vioson.nicePic.R;
import lee.vioson.utils.ScreenUtil;

/**
 * Author:李烽
 * Date:2016-06-02
 * FIXME
 * Todo 半透明的actionBar
 */
public class TranActionBar extends LinearLayout implements View.OnClickListener {


    private final ImageView mIcon;
    private final TextView mTitle;
    private int mBackgroundColor;

    private int mIconId;

    private int mTextColor;

    public void setTitle(String title) {
        this.title = title;
        mTitle.setText(title);
    }

    private String title;

    public TranActionBar(Context context) {
        this(context, null);
    }

    public TranActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranActionBar(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mBackgroundColor = Color.argb(100, 0, 0, 0);
        mIconId = R.drawable.ic_back;
        mTextColor = Color.parseColor("#ffffff");

        setGravity(Gravity.CENTER_VERTICAL);

//        setOrientation(HORIZONTAL);
        setPadding(ScreenUtil.dip2px(context, 6), 0, ScreenUtil.dip2px(context, 6), 0);
        setBackgroundColor(mBackgroundColor);

        mIcon = new ImageView(context);
        mIcon.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mIcon.setImageResource(mIconId);

        mTitle = new TextView(context);
        mTitle.setTextColor(mTextColor);
        mTitle.setTextSize(16);
        mTitle.setEms(12);
        mTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTitle.setSingleLine();
        mTitle.setFocusable(true);
        mTitle.setFocusableInTouchMode(true);
        if (TextUtils.isEmpty(title))
            mTitle.setText(context.getApplicationInfo().loadLabel(context.getPackageManager()).toString());

//*********************占位***************************8*/
        View view = new View(context);
        view.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        addView(mIcon);
        addView(mTitle);
        addView(view);

        mIcon.setOnClickListener(this);

        mTitle.setOnClickListener(this);
    }


    public void setIcon(int iconID) {
        mIcon.setImageResource(iconID);
    }

    public void hide() {
        setVisibility(GONE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bar_hide);
        this.setAnimation(animation);
        animation.start();
    }


    public void show() {
        setVisibility(VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bar_show);
        this.setAnimation(animation);
        animation.start();

    }


    @Override
    public void onClick(View v) {
        if (getContext() instanceof Activity)
            ((Activity) getContext()).finish();
    }
}
