package lee.vioson.nicePic.utils;

/**
 * Author:李烽
 * Date:2016-05-03
 * FIXME
 * Todo
 */
public class UrlUtil {

    private static final String HOST = "http://tnfs.tngou.net/image";
    private static final String HOST_1 = "http://tnfs.tngou.net/img";
    private static final String HOST_2 = "http://www.tngou.net/cook/show/";

    public static String completeImgUrlWithSize(String url, int width, int height) {
        return HOST + url + "_" + width + "x" + height;
    }

    public static String completeBigImgUrl(String url) {
        return HOST_1 + url;
    }

    public static String completeDetailUrl(String ID) {
        return HOST_2 + ID;
    }
}
