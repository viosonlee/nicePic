package lee.vioson.nicePic.utils;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import lee.vioson.nicePic.activities.GalleryActivity;
import lee.vioson.nicePic.activities.MainActivity;
import lee.vioson.nicePic.activities.WebActivity;
import lee.vioson.xiumm.models.Type;

/**
 * Author:李烽
 * Date:2016-06-02
 * FIXME
 * Todo
 */
public class ActivitiSwtcher {

    public static final String DATA = "web_data";

    public static void toGallery(Context context, String id) {
        context.startActivity(new Intent(context, GalleryActivity.class)
                .putExtra(GalleryActivity.ID, id));
    }

    public static void toGallery(Context context, String id, String title) {
        context.startActivity(new Intent(context, GalleryActivity.class)
                .putExtra(GalleryActivity.ID, id)
                .putExtra(GalleryActivity.TITLE, title)
        );
    }


    public static void toMain(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void toMain(Context context, ArrayList<Type> types) {
        context.startActivity(new Intent(context, MainActivity.class)
                .putParcelableArrayListExtra(MainActivity.types, types)
        );
    }


    public static void toWeb(Context context, String title, String url) {
        context.startActivity(new Intent(context, WebActivity.class)
                .putExtra(WebActivity.TITLE, title)
                .putExtra(WebActivity.URL, url)
        );
    }
}
