package lee.vioson.nicePic.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Author:李烽
 * Date:2016-06-02
 * FIXME
 * Todo
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    private static final long DELAYED_TIME = 300;

    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void startRefresh() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
            }
        }, DELAYED_TIME);
    }

    public void endRefresh() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
            }
        }, DELAYED_TIME);
    }

}
