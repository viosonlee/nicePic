package lee.vioson.nicePic.controls;

import android.util.Log;

import com.squareup.okhttp.Request;

import lee.vioson.nicePic.api.ApiClient;
import lee.vioson.nicePic.models.ClassifyBean;
import lee.vioson.nicePic.models.ListBean;
import lee.vioson.nicePic.models.ShowBean;
import lee.vioson.utils.HttpTool;

/**
 * Author:李烽
 * Date:2016-06-01
 * FIXME
 * Todo
 */
public class DataCotroller {

    private static DataCotroller instance;

    private static ClassifyBean mClassifyBean;
    private static ListBean mListBean;
    private static ListBean mNewsBean;
    private static ShowBean mShowBean;

    private static ApiClient client = new ApiClient();

    public synchronized static DataCotroller getInstance() {
        if (instance == null)
            synchronized (DataCotroller.class) {
                if (instance == null)
                    instance = new DataCotroller();
            }
        return instance;
    }

    public void loadClassify(final DataHandler<ClassifyBean> handler) {
        client.getClassify(new HttpTool.ResultCallBack<ClassifyBean>() {
            @Override
            protected void onError(Request request, Exception e) {
                Log.d(TAG, request.toString());
                handler.onDataBack(true, null, request.toString());
            }

            @Override
            protected void onResponse(ClassifyBean classify) {
                mClassifyBean = classify;
                handler.onDataBack(false, classify, "");
            }
        });
    }

    public void getClassify(final DataHandler<ClassifyBean> handler) {
        if (mClassifyBean != null)
            handler.onDataBack(false, mClassifyBean, "");
        else loadClassify(handler);
    }

    @Deprecated
    public void loadList(boolean isNews, int id, int page, int row, final DataHandler<ListBean> handler) {
        if (isNews) client.getNewGalleries(id, row, 0, new HttpTool.ResultCallBack<ListBean>() {
            @Override
            protected void onError(Request request, Exception e) {
                Log.d(TAG, request.toString());
                handler.onDataBack(true, null, request.toString());
            }

            @Override
            protected void onResponse(ListBean list) {
                mListBean = list;
                handler.onDataBack(false, list, "");
            }
        });
        else client.getListGalleries(id, page, row, new HttpTool.ResultCallBack<ListBean>() {
            @Override
            protected void onError(Request request, Exception e) {
                Log.d(TAG, request.toString());
                handler.onDataBack(true, null, request.toString());
            }

            @Override
            protected void onResponse(ListBean list) {
                mListBean = list;
                handler.onDataBack(false, list, "");
            }
        });
    }

    public void loadDetail(String id, final DataHandler<ShowBean> handler) {
        client.getPicDetail(id, new HttpTool.ResultCallBack<ShowBean>() {
            @Override
            protected void onError(Request request, Exception e) {
                Log.d(TAG, request.toString());
                handler.onDataBack(true, null, request.toString());
            }

            @Override
            protected void onResponse(ShowBean showBean) {
                mShowBean = showBean;
                handler.onDataBack(false, showBean, "");
            }
        });
    }

    public void loadNews(int id, int row, int classify, final DataHandler<ListBean> handler) {
        client.getNewGalleries(id, row, classify, new HttpTool.ResultCallBack<ListBean>() {
            @Override
            protected void onError(Request request, Exception e) {
                Log.d(TAG, request.toString());
                handler.onDataBack(true, null, request.toString());
            }

            @Override
            protected void onResponse(ListBean listBean) {
                mNewsBean = listBean;
                handler.onDataBack(false, listBean, "");
            }
        });
    }


    public interface DataHandler<T> {
        String TAG = "DataHandler";

        void onDataBack(boolean isEmpty, T t, String msg);
    }

}
