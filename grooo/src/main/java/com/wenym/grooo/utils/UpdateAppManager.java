package com.wenym.grooo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wenym.grooo.http.model.CheckUpdateData;
import com.wenym.grooo.http.model.CheckUpdateSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpUtils;
import com.wenym.grooo.widgets.Toasts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateAppManager {
    private static final String FILE_SEPARATOR = "/";
    private static final String FILE_PATH = Environment
            .getExternalStorageDirectory()
            + FILE_SEPARATOR
            + "autoupdate"
            + FILE_SEPARATOR;
    private static final String FILE_NAME = FILE_PATH + "Grooo.apk";
    private static final int INSTALL_TOKEN = 0x31;

    private Context context;
    private String message = "\n是否立即更新";

    private int curProgress;
    private Handler handler;

    @SuppressLint("HandlerLeak")
    public UpdateAppManager(Context context) {
        this.context = context;
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case INSTALL_TOKEN:

                        installApp();
                        break;
                }
            }
        };
    }

    public void checkUpdateInfo() {
        HttpUtils.MakeAPICall(new CheckUpdateData(), GroooAppManager.getAppContext(), new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                CheckUpdateSuccessData checkUpdateSuccessData = (CheckUpdateSuccessData) object;
                if (GroooAppManager.getVersionCode() < checkUpdateSuccessData.getVersion()) {
                    showNoticeDialog(checkUpdateSuccessData.getContent(), checkUpdateSuccessData.getSource());
                }
            }

            @Override
            public void onFailed(String reason) {
                Toasts.show(reason);
            }


            @Override
            public void onError(int statusCode) {
                Toasts.show(String.valueOf(statusCode));
            }
        });
    }


    private void showNoticeDialog(String string, final String source) {
        new AlertDialogWrapper.Builder(context).setTitle("有可用更新")
                .setMessage(string + message)
                .setNegativeButton("下次吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startUpdate(source, context);
            }
        }).show();
    }

    private void showDownloadDialog(final String source) {
        new MaterialDialog.Builder(context).title("正在更新").content("更新中")
                .contentGravity(GravityEnum.CENTER).progress(false, 100, true)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        final MaterialDialog dialog = (MaterialDialog) dialogInterface;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                URL url = null;
                                InputStream in = null;
                                FileOutputStream out = null;
                                HttpURLConnection conn = null;
                                try {
                                    url = new URL(source);
                                    conn = (HttpURLConnection) url
                                            .openConnection();
                                    conn.connect();
                                    long fileLength = conn.getContentLength();
                                    in = conn.getInputStream();
                                    File filePath = new File(FILE_PATH);
                                    if (!filePath.exists()) {
                                        filePath.mkdir();
                                    }
                                    out = new FileOutputStream(new File(FILE_NAME));
                                    byte[] buffer = new byte[1024];
                                    int len = 0;
                                    long readedLength = 0l;
                                    while ((len = in.read(buffer)) != -1) {
                                        if (dialog.isCancelled()) {
                                            break;
                                        }
                                        out.write(buffer, 0, len);
                                        readedLength += len;
                                        curProgress = (int) (((float) readedLength / fileLength) * 100);
                                        dialog.setProgress(curProgress);
                                        if (readedLength >= fileLength) {
                                            dialog.setContent("更新完成");
                                            dialog.dismiss();
                                            handler.sendMessage(handler.obtainMessage(INSTALL_TOKEN));
                                            break;
                                        }
                                    }
                                    out.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (out != null) {
                                        try {
                                            out.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (in != null) {
                                        try {
                                            in.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (conn != null) {
                                        conn.disconnect();
                                    }
                                }
                            }
                        }).start();
                    }
                }).show();
    }

    private void installApp() {
        Toasts.show("fuck");
        File appFile = new File(FILE_NAME);
        if (!appFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开浏览器下载新版本
     *
     * @param context
     */
    public static void startUpdate(String source, Context context) {
        Uri uri = Uri.parse(source);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}