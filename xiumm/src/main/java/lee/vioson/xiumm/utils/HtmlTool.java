package lee.vioson.xiumm.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lifeng on 16-6-10.
 * TODO:
 */
public class HtmlTool {

    private static final String TAG = HtmlTool.class.getSimpleName();

    public static String getHtml(String url) throws IOException {
        URL url1 = new URL(url);
        URLConnection connection = url1.openConnection();
        InputStream inputStream = connection.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String str;
        String result = "";
        StringBuilder sb = new StringBuilder();
        while (null != (str = br.readLine())) {
            Log.d(TAG, str);
            result = result + str+"\n";
        }
        br.close();
        return result;
    }


//    public static String getHtml2(String url) {
//        HttpClient client = new DefaultHttpClient();
//        HttpGet httpget = new HttpGet(url.trim());
//        HttpResponse response = client.execute(httpget);
//        String charset = "";
//        String contentType = response.getEntity().getContentType().getValue();
//        if (StringUtils.isNotEmpty(contentType) && contentType.indexOf("charset") > 0) {
//            charset = contentType.substring(contentType.indexOf("charset=") + "charset=".length());
//        }
//        if (StringUtils.isEmpty(charset)) {
//            charset = "UTF-8";
//        }
//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charset));
//        String line;
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        String content = sb.toString();
//        return content;
//    }
}
