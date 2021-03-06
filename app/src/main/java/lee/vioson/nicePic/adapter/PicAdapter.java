package lee.vioson.nicePic.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import lee.vioson.nicePic.models.ShowBean;
import lee.vioson.nicePic.utils.UrlUtil;
import lee.vioson.utils.PicassoUtil;
import lee.vioson.widget.ZoomImageView;

/**
 * Author:李烽
 * Date:2016-06-03
 * FIXME
 * Todo
 */
public class PicAdapter extends PagerAdapter {
    private ArrayList<ImageView> views;
    private List<ShowBean.ListEntity> paths;
    private Context mContext;

    public PicAdapter(List<ShowBean.ListEntity> paths, Context mContext) {
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
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View imageView = createImageView(paths.get(position).getSrc());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null)
                    onClickListener.onClick(imageView, position);
            }
        });

//                DialogShower.showCancelableChooseDialog(mContext, "保存图片到本地", "确定",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                PicassoUtil.saveBitmapSDCard(mContext,
//                                        UrlUtil.completeBigImgUrl(paths.get(position).getSrc())
//                                        , Environment.getExternalStorageDirectory() + "/vioson.png");
//                            }
//                        });
//                Log.d("path",Environment.getExternalStorageDirectory() + "/vioson.png");

        ((ViewPager) container).addView(imageView);
        return imageView;
    }

    private View createImageView(String path) {

        final ZoomImageView imageView = new ZoomImageView(mContext);
//        final ImageView imageView=new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        imageView.setLayoutParams(lp);
        PicassoUtil.setImage(mContext, UrlUtil.completeBigImgUrl(path), imageView);
//        Picasso.with(mContext).load(UrlUtil.completeBigImgUrl(path)).into(imageView);
        return imageView;
    }


    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }

    public interface OnLongClickListener {
        void onLongClick(View view, int position);
    }
}
