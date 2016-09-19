package lee.vioson.nicePic.api;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

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
public class ApiClient {
    private static final String IP = "http://apis.baidu.com/tngou/gallery/";

    private static final String NEWS = "news";
    private static final String CLASSIFY = "classify";
    private static final String LIST = "list";
    private static final String SHOW = "show";
    private static final String TAG = "ApiClient";

    private String completeUrl(String dns) {
        return IP + dns;
    }

    private static final String APIKEY = "apikey";

    private static final String APIKEY_VALUE = "3b0f889244f63645afeb76c3af21630f";

    private Map<String, String> getGetApiKey() {
        Map<String, String> map = new HashMap<>();
        map.put(APIKEY, APIKEY_VALUE);
        return map;
    }

    /**
     * 最新列表
     *
     * @param id
     * @param row
     * @param classify
     * @param callBack
     */
    public void getNewGalleries(int id, int row, int classify, HttpTool.ResultCallBack<ListBean> callBack) {
        String url = completeUrl(NEWS) + "?id=" + id + "&rows=" + row + "&classify=" + classify;
        Log.d(TAG, url);
        HttpTool.getAsynWithHeader(url, getGetApiKey(), callBack);
    }

    /**
     * 获取当前分类下的列表
     *
     * @param id
     * @param page
     * @param row
     * @param callBack
     */
    public void getListGalleries(int id, int page, int row, HttpTool.ResultCallBack<ListBean> callBack) {
        String url = completeUrl(LIST) + "?id=" + id + "&rows=" + row + "&page=" + page;
        Log.d(TAG, url);
        HttpTool.getAsynWithHeader(url, getGetApiKey(), callBack);
    }

    /**
     * 获取分类
     *
     * @param callBack
     */
    public void getClassify(HttpTool.ResultCallBack<ClassifyBean> callBack) {
        String url = completeUrl(CLASSIFY);
        Log.d(TAG, url);
        HttpTool.getAsynWithHeader(url, getGetApiKey(), callBack);
    }

    /**
     * 查看
     *
     * @param id
     * @param callBack
     */
    public void getPicDetail(String id, HttpTool.ResultCallBack<ShowBean> callBack) {
        String url = completeUrl(SHOW)+"?id="+id;
        Log.d(TAG, url);
        HttpTool.getAsynWithHeader(url, getGetApiKey(), callBack);
    }
}
