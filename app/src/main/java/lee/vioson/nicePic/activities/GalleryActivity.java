package lee.vioson.nicePic.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lee.vioson.nicePic.R;
import lee.vioson.nicePic.adapter.PicAdapter;
import lee.vioson.nicePic.adapter.PicAdapter_1;
import lee.vioson.nicePic.api.ApiClient;
import lee.vioson.nicePic.models.ShowBean;
import lee.vioson.nicePic.utils.DialogShower;
import lee.vioson.nicePic.utils.UrlUtil;
import lee.vioson.nicePic.views.TranActionBar;
import lee.vioson.utils.HttpTool;
import lee.vioson.utils.PermissionUtil;
import lee.vioson.utils.PicassoUtil;
import lee.vioson.xiumm.models.PicDetail;
import lee.vioson.xiumm.utils.DataHelper;

/**
 * Author:李烽
 * Date:2016-06-02
 * FIXME
 * Todo
 */
public class GalleryActivity extends AppCompatActivity implements PicAdapter.OnClickListener, ViewPager.OnPageChangeListener, PicAdapter_1.OnClickListener {
    public static final String ID = "gallery_id";
    private static final int REQUEST_CODE = 1;
    private static final String DIR_NAME = "/nicePic/";
    public static final String TITLE = "title";

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.root_view)
    RelativeLayout rootView;
    @Bind(R.id.action_bar)
    TranActionBar actionBar;
    @Bind(R.id.save_pic)
    ImageView savePic;
    @Bind(R.id.count_txt)
    TextView countTxt;


    private ApiClient client;

    private ArrayList<ShowBean.ListEntity> mData = new ArrayList<>();
    private ArrayList<PicDetail> mData1 = new ArrayList<>();

    private String title;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        actionBar.setIcon(R.drawable.ic_back);
        title = getTitleStr();
        client = new ApiClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在获取");
        viewPager.addOnPageChangeListener(this);
        loadData();
        actionBar.setTitle(title);
    }

    private String id = null;

    private String getID() {
        if (id == null)
            id = getIntent().getStringExtra(ID);
        return id;
    }

    private String getTitleStr() {
        if (TextUtils.isEmpty(getIntent().getStringExtra(TITLE)))
            title = getString(R.string.app_name);
        else title = getIntent().getStringExtra(TITLE);
        return title;
    }

    private void loadData() {
        dialog.show();
        DataHelper.loadDetail(getID(), new DataHelper.DataHandler<ArrayList<PicDetail>>() {
            @Override
            public void onDataBack(boolean isEmpty, ArrayList<PicDetail> picDetails) {
                dialog.dismiss();
                if (!isEmpty) {
                    mData1.clear();
                    mData1.addAll(picDetails);
                    PicAdapter_1 picAdapter1 = new PicAdapter_1(picDetails, GalleryActivity.this);
                    viewPager.setAdapter(picAdapter1);
                    picAdapter1.setOnClickListener(GalleryActivity.this);
//                title = picDetails.;
                    setCountIndex(0);
                }
            }

            @Override
            public void onDocumentNull() {
                dialog.dismiss();
            }
        });


//        client.getPicDetail(getID(), new HttpTool.ResultCallBack<ShowBean>() {
//            @Override
//            protected void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            protected void onResponse(ShowBean showBean) {
//                if (showBean != null) {
//                    mData.clear();
//                    mData.addAll(showBean.getList());
//                    PicAdapter adapter = new PicAdapter(showBean.getList(), GalleryActivity.this);
//                    viewPager.setAdapter(adapter);
//                    adapter.setOnClickListener(GalleryActivity.this);
//                    title = showBean.getTitle();
//                    setCountIndex(0);
//                }
//            }
//        });
    }

    private void setCountIndex(int i) {
        countTxt.setText(String.format("%d/%d", i + 1, mData1.size()));
    }

    boolean isShow = true;

    public void hide() {
        if (isShow) {
            actionBar.hide();
            countTxt.setVisibility(View.GONE);
            isShow = false;
        } else {
            actionBar.show();
            countTxt.setVisibility(View.VISIBLE);
            isShow = true;
        }
    }


    private void savePic() {
        boolean b = PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!b) {
            PermissionUtil.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (mData1.size() > 0) {
            final int index = viewPager.getCurrentItem();
            DialogShower.showCancelableChooseDialog(this, "保存图片到本地", "确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GalleryActivity.this.save(index);
                        }
                    });
            Log.d("path", Environment.getExternalStorageDirectory() + DIR_NAME +
                    title + "_" + index + ".png");
        }
    }

    private void save(int index) {
        PicassoUtil.saveBitmapSDCard(GalleryActivity.this,
                DataHelper.getPicUrl(mData1.get(index).src)
                , Environment.getExternalStorageDirectory() + DIR_NAME +
                        mData1.get(index).alt+/* "_" + index + */".png",
                new PicassoUtil.SaveBitmapListener() {
                    @Override
                    public void onSaved(String path) {
                        Toast.makeText(GalleryActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(GalleryActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view, int position) {
        hide();
    }


    @OnClick(R.id.save_pic)
    public void onClick() {
        savePic();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCountIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
