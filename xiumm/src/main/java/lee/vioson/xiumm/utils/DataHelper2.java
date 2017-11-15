package lee.vioson.xiumm.utils;

import android.os.Bundle;
import android.os.Message;
import android.util.SparseArray;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import lee.vioson.utils.DebugLog;
import lee.vioson.xiumm.models.ListData;
import lee.vioson.xiumm.models.PicDetail;
import lee.vioson.xiumm.models.Type;

/**
 * Created by viosonlee
 * on 2017/10/14.
 * todo 另一个源
 */

public class DataHelper2 {
    private static final String URL = "http://zhaifuli.info";
    private static final String TAG = DataHelper2.class.getSimpleName();
    private static final String TYPE_CLASS = "menu-item menu-item-type-taxonomy menu-item-object-category menu-item";
    private static SparseArray<Type> typeSparseArray = new SparseArray<>();
    private static DataHelper2.Handler handler = new DataHelper2.Handler();

    public static SparseArray<Type> getTypes() {
        typeSparseArray.append(CommonKey.typeId[0], new Type(URL + CommonKey.type_herfs[0], CommonKey.type_title[0]));
        typeSparseArray.append(CommonKey.typeId[1], new Type(URL + CommonKey.type_herfs[1], CommonKey.type_title[1]));
        typeSparseArray.append(CommonKey.typeId[2], new Type(URL + CommonKey.type_herfs[2], CommonKey.type_title[2]));
        typeSparseArray.append(CommonKey.typeId[3], new Type(URL + CommonKey.type_herfs[3], CommonKey.type_title[3]));
        typeSparseArray.append(CommonKey.typeId[4], new Type(URL + CommonKey.type_herfs[4], CommonKey.type_title[4]));
        typeSparseArray.append(CommonKey.typeId[5], new Type(URL + CommonKey.type_herfs[5], CommonKey.type_title[5]));
        return typeSparseArray;
//        try {
//            Document document = Jsoup.connect(URL).get();
//            DebugLog.d(TAG, document.toString());
//            Elements elements = document.getElementsByClass(TYPE_CLASS);
//            List<Type> types = new ArrayList<>();
//            for (Element element : elements) {
//                Elements aElements = element.getElementsByTag(CommonKey.TAG_A);
//                if (aElements != null && aElements.size() > 0) {
//                    Element a = aElements.get(0);
//                    String herf = URL + "/" + a.attr(CommonKey.TAG_ATTR_HERF);
//                    String title = a.text();
//                    if (!title.contains("番号集"))
//                        types.add(new Type(herf, title));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static ArrayList<Type> loadTypes() {
        ArrayList<Type> types = new ArrayList<>();
        types.add(new Type(URL + CommonKey.type_herfs[0], CommonKey.type_title[0]));
        types.add(new Type(URL + CommonKey.type_herfs[1], CommonKey.type_title[1]));
        types.add(new Type(URL + CommonKey.type_herfs[2], CommonKey.type_title[2]));
        types.add(new Type(URL + CommonKey.type_herfs[3], CommonKey.type_title[3]));
        types.add(new Type(URL + CommonKey.type_herfs[4], CommonKey.type_title[4]));
        types.add(new Type(URL + CommonKey.type_herfs[5], CommonKey.type_title[5]));
        return types;
    }

    private static String getPageUrl(int page, String href, int typeId) {
        if (page == 1) {
            return href;
        } else {
            return href + "list_" + typeId + "_" + page + ".html";
        }
    }

    private static String getPicListUrl(int page, String href) {
        if (page == 1)
            return href;
        else return href.replace(".html", "_" + page + ".html");
    }

    private static void getListData(int page, String herf, int typeId) {
        String url = getPageUrl(page, herf, typeId);
        DebugLog.i("vioson", url);
        try {
            Document document = Jsoup.connect(url).get();
            if (document != null) {
                Elements contents = document.getElementsByClass("content");
                DebugLog.d(TAG, contents.toString());
                Element content = contents.get(0);
                ArrayList<ListData> listDataList = new ArrayList<>();
                Elements elementsByClass = content.getElementsByTag("article");
                for (Element byClass : elementsByClass) {
                    Elements img = byClass.getElementsByTag("img");
                    String src = img.get(0).attr("src");
                    Elements h2 = byClass.getElementsByTag(CommonKey.TAG_H2);
                    Element element = h2.get(0);
                    Elements select = element.select("a[href]");
                    if (select != null && select.size() > 0) {
                        DebugLog.i(TAG, select.toString());
                        String link = select.get(0).attr(CommonKey.TAG_ATTR_HERF);
                        String title = select.get(0).attr(CommonKey.TAG_ATTR_TITLE);
                        DebugLog.e(TAG, link + "\n" + title);
                        ListData listData = new ListData();
                        listData.href = URL + link;
                        listData.alt = title;
                        listData.src = URL + src;
                        listDataList.add(listData);
                        DebugLog.i(TAG, listData.toString());
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(DATA, listDataList);
                DebugLog.i(TAG, listDataList.toString());
                DebugLog.i(TAG, listDataList.size() + "个");
                Message message = new Message();
                message.setData(bundle);
                message.what = LIST_BACK;
                handler.sendMessage(message);
            } else handler.sendEmptyMessage(NULL);
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(ERROR);
        }
    }

    public static void loadList(final int page, final String typeHref, final int typeID, DataHandler<ArrayList<ListData>> handler) {
        listDataHandler = handler;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getListData(page, typeHref, typeID);
            }
        }).start();
    }

    public static String getPicUrl(String src) {
        if (src.contains("http://"))
            return src;
        else return URL + src;
    }

    private static void getPicList(String href) {
        int page = 1;
        boolean hasMore = true;
        ArrayList<PicDetail> picDetails = new ArrayList<>();
        while (hasMore) {
            String url = getPicListUrl(page, href);
            DebugLog.i(TAG, url);
            try {
                Document document = Jsoup.connect(url).get();
                if (document != null) {
//                    Elements elementsByClass = document.getElementsByClass("article-content");
//                    Element element = elementsByClass.get(0);
                    Elements select = document.select("img[alt]");
                    if (select != null && select.size() > 0) {
                        DebugLog.e(TAG, select.toString());
                        for (Element pic : select) {
                            String alt = pic.attr("alt");
                            String src = pic.attr("src");
                            PicDetail picDetail = new PicDetail();
                            picDetail.alt = alt;
                            picDetail.src = src;
                            picDetails.add(picDetail);
                        }
                        page++;
                    } else {
                        hasMore = false;
                        DebugLog.i(TAG, "解析完毕");
                        DebugLog.e(TAG, picDetails.toString());
                        if (page == 1)
                            handler.sendEmptyMessage(ERROR);
                    }
                } else {
                    hasMore = false;
                    if (page == 1)
                        handler.sendEmptyMessage(ERROR);
                }
            } catch (IOException e) {
                hasMore = false;
                e.printStackTrace();
                if (page == 1)
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
                getPicList(href);
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

}
