package lee.vioson.nicePic.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lee.vioson.nicePic.utils.DataServie;
import lee.vioson.utils.PicassoUtil;
import lee.vioson.widget.ZoomImageView;
import lee.vioson.xiumm.models.PicDetail;

/**
 * Author:李烽
 * Date:2016-06-03
 * FIXME
 * Todo
 */
public class PicAdapter_1 extends PagerAdapter {
    private ArrayList<ImageView> views;
    private List<PicDetail> paths;
    private Context mContext;

    public PicAdapter_1(List<PicDetail> paths, Context mContext) {
        this.paths = paths;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return paths == null ? 0 : paths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View imageView = createImageView(paths.get(position).src);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null)
                    onClickListener.onClick(imageView, position);
            }
        });

        container.addView(imageView);
        return imageView;
    }

    private View createImageView(String path) {
        final ZoomImageView imageView = new ZoomImageView(mContext);
//        final ImageView imageView=new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        imageView.setLayoutParams(lp);
        PicassoUtil.setImage(mContext, DataServie.getPicUrl(path), imageView);
        Log.e("picadapter",DataServie.getPicUrl(path));
        return imageView;
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }



    public interface OnClickListener {
        void onClick(View view, int position);
    }


}
