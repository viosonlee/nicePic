package lee.vioson.xiumm.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * author:合肥工业大学 管院学院 钱洋
 *1563178220@qq.com
 *博客地址:http://blog.csdn.net/qy20115549/
*/
public class UrlUtil {
    private final static String ENCODE = "GBK";

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String str) throws UnsupportedEncodingException {
        //中文正则匹配
        Pattern p = Pattern.compile("([\u4e00-\u9fa5]+)");
        Matcher m = p.matcher(str);
        String mv = null;
        List<String> list = new ArrayList<>();
        while (m.find()) {
            mv = m.group(0);
            list.add(getURLEncoderString(mv));
        }
    }
}
