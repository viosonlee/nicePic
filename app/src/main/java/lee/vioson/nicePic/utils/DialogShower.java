package lee.vioson.nicePic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import lee.vioson.utils.NetWorkUtils;


/**
 * Author:李烽
 * Date:2016-05-31
 * FIXME
 * Todo
 */
public class DialogShower {

    public static void showNetWorkDisconnect(final Context context) {
        new AlertDialog.Builder(context).setMessage("无网络连接，请检查网络设置")
                .setCancelable(false).setNegativeButton("好的",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去设置网络
                        NetWorkUtils.toSetNetWork(context);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ((Activity) context).finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .create().show();
    }


    public static void showSingleMessageDialog(final Context context, String msg) {
        new AlertDialog.Builder(context).setMessage(msg)
                .setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
    }

    public static void showSingleChooseDialog(final Context context, String msg, String chooseTxt,
                                              DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context).setMessage(msg)
                .setNegativeButton(chooseTxt,
                        listener).create().show();
    }

    public static void showCancelableChooseDialog(final Context context, String msg, String chooseTxt,
                                                  DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context).setMessage(msg)
                .setPositiveButton(chooseTxt,
                        listener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

}
