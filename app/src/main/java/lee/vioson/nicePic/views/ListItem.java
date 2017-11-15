package lee.vioson.nicePic.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import lee.vioson.nicePic.R;
import lee.vioson.nicePic.models.ListBean;
import lee.vioson.nicePic.utils.ActivitiSwtcher;
import lee.vioson.nicePic.utils.UrlUtil;
import lee.vioson.utils.DateUtil;
import lee.vioson.utils.DebugLog;
import lee.vioson.utils.NetWorkUtils;
import lee.vioson.utils.PicassoUtil;
import lee.vioson.xiumm.models.ListData;

/**
 * Author:李烽
 * Date:2016-06-02
 * FIXME
 * Todo
 */
@Deprecated
public class ListItem extends LinearLayout {


    private static int picSize = 100;
    //    @BindView(R.id.pic)
    ImageView pic;
    //    @BindView(R.id.title)
    TextView title;
    //    @BindView(R.id.time)
    TextView time;

    public ListItem(Context context) {
        this(context, null);
    }

    public ListItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_list_item, this);
//        ButterKnife.bind(view, this);
        pic = (ImageView) view.findViewById(R.id.pic);
        title = (TextView) view.findViewById(R.id.title);
        time = (TextView) view.findViewById(R.id.time);
        if (NetWorkUtils.isFastMobileNetwork(context))
            picSize = context.getResources().getDimensionPixelOffset(R.dimen.pic_size_width);
    }

    @SuppressLint("DefaultLocale")
    public void addData(final ListBean.TngouEntity data) {
        if (data == null)
            return;
        String url = UrlUtil.completeImgUrlWithSize(data.getImg(), picSize, picSize);
//        String url = UrlUtil.completeBigImgUrl(data.getImg());
        DebugLog.d("img", url);
        PicassoUtil.setImage(getContext(), url, pic);
        title.setText(data.getTitle());
        time.setText(String.format("%d图\t%s", data.getSize(), DateUtil.getDay(data.getTime())));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitiSwtcher.toGallery(getContext(), data.getId() + "");
            }
        });
    }

    public void addData(final ListData listData) {
        if (listData == null)
            return;
        String url = listData.src;
        if (!TextUtils.isEmpty(url))
            PicassoUtil.setImage(getContext(), url, pic);
        title.setText(listData.alt);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitiSwtcher.toGallery(getContext(), listData.href, listData.alt);
            }
        });
    }
}
