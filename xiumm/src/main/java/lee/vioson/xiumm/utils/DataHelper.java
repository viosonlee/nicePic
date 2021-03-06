package lee.vioson.xiumm.utils;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import lee.vioson.utils.DebugLog;
import lee.vioson.xiumm.models.ListData;
import lee.vioson.xiumm.models.PicDetail;
import lee.vioson.xiumm.models.Type;

/**
 * Created by lifeng on 16-6-9.
 * TODO: 获取数据
 */
public class DataHelper {

    private static final String TAG = DataHelper.class.getSimpleName();
    private static Handler handler = new Handler();

    public static String getUrl() {
        return url;
    }

    public static String getPicUrl(String picUrl) {
        return picUrl.contains("http://") ? picUrl : url + picUrl;
    }

    //    private static final String url = "http://xiumm.cc";
//    private static final String url = "http://www.xmeim.com";
    private static final String url = "http://www.xiumeim.com";

    /**
     * 获取列表数据
     *
     * @param page
     */
    private static void getList(int page, String typeHref) {
        String url = completeUrl(page, typeHref);
        Log.d(TAG, url);
        try {
            Document document = Jsoup.connect(encodeUrl(url)).get();
//            Document document = Jsoup.parse(reaDHTML(url));
            if (document != null) {
//                Log.d(TAG, document.toString());
                Elements gallaryWrap = document.getElementsByClass("gallary_wrap");
                Log.d(TAG, gallaryWrap.toString());
                Element bodywrap = gallaryWrap.get(0);
                Elements elements = bodywrap.select("a[target]");
                Log.d(TAG, elements.toString());
                ArrayList<ListData> listDatas = new ArrayList<>();
                for (Element element : elements) {
                    String href = element.attr("href");
                    Elements img = element.select("img");
                    if (img != null && !img.isEmpty()) {
                        String alt = img.attr("alt");
                        String src = img.attr("data-original");
                        ListData listData = new ListData();
                        listData.alt = alt;
                        listData.src = src;
                        listData.href = href;
                        listDatas.add(listData);
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(DATA, listDatas);
                DebugLog.i(TAG, listDatas.toString());
                DebugLog.i(TAG, listDatas.size() + "个");
                Message message = new Message();
                message.setData(bundle);
                message.what = LIST_BACK;
                handler.sendMessage(message);
            } else handler.sendEmptyMessage(NULL);

        } catch (IOException e) {
            e.printStackTrace();
//            Log.d(TAG, e.getMessage());
            handler.sendEmptyMessage(ERROR);
        }


    }

    /**
     * 首页分类下面的数据
     *
     * @param page
     * @return
     */
    private static String getAlbumsUrl(int page) {
        if (page == 1)
            return url + "/";
        else return url + "/albums/page-" + page + ".html";
    }

    /**
     * 获取列表数据用到分页，
     *
     * @param page
     * @param typeHref
     * @return 返回当前分页的url
     */
    private static String completeUrl(int page, String typeHref) {
        if (!TextUtils.isEmpty(typeHref) && typeHref.contains("http://")) {
            if (typeHref.equals(url + "/")) {
                return getAlbumsUrl(page);
            } else {
                if (page == 1)
                    return typeHref;
                else {
                    typeHref.replace(".html", "-" + page + ".html");
                    return typeHref;
                }
            }
        } else {
            return completeDetailUrl(typeHref, page);
//            if (page == 1) {
//                return url + typeHref;
//            } else {
//                return url + (typeHref.replace(".html", "-" + page + ".html"));
//            }
        }
    }


    public static void loadList(final int page, final String typeHref, DataHandler<ArrayList<ListData>> handler) {
        listDataHandler = handler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getList(page, typeHref);
            }
        }).start();
    }

    /**
     * 获取类型
     */
    private static void getTypes() {
        String url = getUrl();
        Log.d(TAG, url);
        try {
            Document document = Jsoup.connect(encodeUrl(url)).get();
//            Document document = Jsoup.parse(reaDHTML(url));// TODO: 2017/11/14 修复url包含中文
            if (document != null) {
//                Log.d(TAG, document.toString());
                Element bodywrap = document.getElementById("menu");
//                Log.d(TAG, bodywrap.toString());
                Elements elements = bodywrap.select("a[href]");
//                Log.d(TAG, elements.toString());
                ArrayList<Type> types = new ArrayList<>();
                for (Element element : elements) {
                    String href = element.attr(CommonKey.TAG_ATTR_HERF);
                    String title = element.attr("title");
                    Type type = new Type();
                    type.href = href;
                    type.title = title;
//                    Log.e(TAG, type.toString());
                    if (!title.equals("赞助本站"))
                        types.add(type);
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(DATA, types);
                Message message = new Message();
                message.setData(bundle);
                message.what = TYPE_BACK;
                handler.sendMessage(message);
            } else handler.sendEmptyMessage(NULL);
        } catch (IOException e) {
            e.printStackTrace();
//            Log.d(TAG, e.getMessage());
            handler.sendEmptyMessage(ERROR);
        }


    }

    public static void loadTypes(DataHandler<ArrayList<Type>> handler) {
        typeDataHandler = handler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTypes();
            }
        }).start();
    }

    /**
     * 详情里面的url
     *
     * @param href
     * @param page
     * @return
     */
    private static String completeDetailUrl(String href, int page) {
        if (!TextUtils.isEmpty(href) && (href.contains("http://") || href.contains("https://")))
            return page == 1 ? href : (href.replace(".html", "-" + page + ".html"));
        else
            return page == 1 ? url + href : url + (href.replace(".html", "-" + page + ".html"));
    }

    /**
     * 获取详情
     *
     * @param href
     */
    private static void getDetail(String href) {
        int page1 = 1;
        boolean hasMore = true;
        ArrayList<PicDetail> picDetails = new ArrayList<>();
        while (hasMore) {
            String url = completeDetailUrl(href, page1);
            Log.e(TAG, url);
            try {
//                String html = HtmlTool.getHtml(url);
//                Document document = Jsoup.parse(html);
                Document document = Jsoup.connect(encodeUrl(url)).get();
//                Document document = Jsoup.parse(reaDHTML(url));// TODO: 2017/11/14 修复url包含中文
                if (document != null) {
//                    Log.e(TAG, html);
//                    Log.d(TAG, document.toString());
                    Elements bodywrap = document.getElementsByClass("mainbody");
//                    Log.d(TAG, bodywrap.toString());
                    Elements elements = bodywrap.select("img[src]");
//                    Log.d(TAG, elements.toString());
                    for (Element element : elements) {
                        String style = element.attr("style");
                        if (!TextUtils.isEmpty(style))
                            continue;
                        PicDetail picDetail = new PicDetail();
                        picDetail.src = element.attr("src");
                        picDetail.alt = element.attr("alt");
                        picDetails.add(picDetail);
//                        Log.e(TAG, picDetail.toString());
                    }
                    page1++;
                } else {
                    hasMore = false;
                    if (page1 == 1) {
                        handler.sendEmptyMessage(NULL);
                    }
                }
            } catch (IOException e) {
                hasMore = false;
                e.printStackTrace();
                if (page1 == 1)
                    handler.sendEmptyMessage(ERROR);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(DATA, picDetails);
        Message message = new Message();
        message.setData(bundle);
        message.what = DETAIL_BACK;
        handler.sendMessage(message);
    }

    public static void loadDetail(final String href, DataHandler<ArrayList<PicDetail>> handler) {
        dataHandler = handler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDetail(href);
            }
        }).start();
    }


    private static final int ERROR = 0x002;
    private static final int DETAIL_BACK = 0x001;
    private static final int TYPE_BACK = 0x003;
    private static final int LIST_BACK = 0x004;
    private static final int NULL = 0x005;


    private static final String DATA = "data-key";

    static class Handler extends android.os.Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DETAIL_BACK:
                    ArrayList<PicDetail> pics =
                            msg.getData().getParcelableArrayList(DATA);
                    if (dataHandler != null)
                        dataHandler.onDataBack(false, pics);
                    break;
                case TYPE_BACK:
                    ArrayList<Type> types =
                            msg.getData().getParcelableArrayList(DATA);
                    if (typeDataHandler != null)
                        typeDataHandler.onDataBack(false, types);
                    break;
                case LIST_BACK:
                    ArrayList<ListData> listDatas =
                            msg.getData().getParcelableArrayList(DATA);
                    if (listDataHandler != null)
                        listDataHandler.onDataBack(false, listDatas);
                    break;
                case ERROR:
                    if (dataHandler != null)
                        dataHandler.onDataBack(true, null);
                    if (typeDataHandler != null)
                        typeDataHandler.onDataBack(true, null);
                    if (listDataHandler != null)
                        listDataHandler.onDataBack(true, null);
                    break;
                case NULL:
                    if (dataHandler != null)
                        dataHandler.onDocumentNull();
                    if (typeDataHandler != null)
                        typeDataHandler.onDocumentNull();
                    if (listDataHandler != null)
                        listDataHandler.onDocumentNull();
                    break;
            }
        }
    }

    private static DataHandler dataHandler;
    private static DataHandler listDataHandler;
    private static DataHandler typeDataHandler;

    private static String reaDHTML(String myurl) throws IOException {
        StringBuffer sb = new StringBuffer("");
        URL url;
        url = new URL(myurl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),
                "utf8"));
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s + "\r\n");
        }
        return sb.toString();

    }

    private static String encodeUrl(String url) throws UnsupportedEncodingException {
        //对路径进行编码 然后替换路径中所有空格 编码之后空格变成“+”而空格的编码表示是“%20” 所以将所有的“+”替换成“%20”就可以了
        String newUrl = URLEncoder.encode(url, "utf8").replaceAll("\\+", "%20");
        //编码之后的路径中的“/”也变成编码的东西了 所有还有将其替换回来 这样才是完整的路径
        newUrl = newUrl.replaceAll("%3A", ":")
                .replaceAll("%2F", "/");
        Log.d(TAG, "newUrl:" + newUrl);
        return newUrl;

    }
}
