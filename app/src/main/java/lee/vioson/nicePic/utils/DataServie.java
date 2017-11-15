package lee.vioson.nicePic.utils;

import java.util.ArrayList;

import lee.vioson.xiumm.models.ListData;
import lee.vioson.xiumm.models.PicDetail;
import lee.vioson.xiumm.models.Type;
import lee.vioson.xiumm.utils.CommonKey;
import lee.vioson.xiumm.utils.DataHandler;
import lee.vioson.xiumm.utils.DataHelper;
import lee.vioson.xiumm.utils.DataHelper2;

/**
 * Created by viosonlee
 * on 2017/10/14.
 * todo
 */

public class DataServie {
    public static boolean isLimit = true;//限制

    public static void loadTypes(DataHandler<ArrayList<Type>> dataHandler) {
        if (isLimit)
            DataHelper.loadTypes(dataHandler);
        else dataHandler.onDataBack(false, DataHelper2.loadTypes());
    }

    private static int getTypeIDByType(String href) {
        for (int i = 0; i < CommonKey.type_herfs.length; i++) {
            if (href.contains(CommonKey.type_herfs[i]))
                return CommonKey.typeId[i];
        }
        return CommonKey.typeId[0];
    }

    public static void loadListData(int page, String href, DataHandler<ArrayList<ListData>> dataHandler) {
        if (isLimit)
            DataHelper.loadList(page, href, dataHandler);
        else DataHelper2.loadList(page, href, getTypeIDByType(href), dataHandler);
    }

    public static void loadPicList(String href, DataHandler<ArrayList<PicDetail>> dataHandler) {
        if (isLimit)
            DataHelper.loadDetail(href, dataHandler);
        else DataHelper2.loadDetail(href, dataHandler);
    }

    public static String getPicUrl(String href) {
        if (isLimit)
            return DataHelper.getPicUrl(href);
        else return DataHelper2.getPicUrl(href);
    }
}
