package com.wenym.grooo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;
import com.wenym.grooo.GroooApplication;
import com.wenym.grooo.http.model.CheckUpdateData;
import com.wenym.grooo.http.model.CheckUpdateSuccessData;
import com.wenym.grooo.http.util.HttpCallBack;
import com.wenym.grooo.http.util.HttpConstants;
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
    private static final String FILE_NAME = FILE_PATH + "Groo.apk";
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
        HttpUtils.MakeAPICall(new CheckUpdateData(), GroooApplication.getGroooApplicationContext(), new HttpCallBack() {
            @Override
            public void onSuccess(Object object) {
                CheckUpdateSuccessData checkUpdateSuccessData = (CheckUpdateSuccessData) object;
                if (getVersionCode(GroooApplication.getGroooApplicationContext()) < checkUpdateSuccessData.getVersion()) {
                    showNoticeDialog(checkUpdateSuccessData.getContent());
                }
            }

            @Override
            public void onFailed() {
            }

            @Override
            public void onError(int statusCode) {
                Toasts.show(String.valueOf(statusCode));
            }
        });
    }

    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    "com.wenym.grooo", 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * ��ʾ��ʾ���¶Ի���
     *
     * @param string
     */
    private void showNoticeDialog(String string) {
        new MaterialDialog.Builder(context).title("有可用更新")
                .content(string + message).positiveText("确定")
                .negativeText("下次吧").callback(new ButtonCallback() {

            @Override
            public void onPositive(MaterialDialog dialog) {
                showDownloadDialog();
                super.onPositive(dialog);
            }
        }).show();
    }

    /**
     * ��ʾ���ؽ�ȶԻ���
     */
    private void showDownloadDialog() {
        new MaterialDialog.Builder(context).title("正在更新").content("更新中")
                .contentGravity(GravityEnum.CENTER).progress(false, 250, true)
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
                                    url = new URL(HttpConstants.UPDATEURL);
                                    conn = (HttpURLConnection) url
                                            .openConnection();
                                    conn.connect();
                                    long fileLength = conn.getContentLength();
                                    in = conn.getInputStream();
                                    File filePath = new File(FILE_PATH);
                                    if (!filePath.exists()) {
                                        filePath.mkdir();
                                    }
                                    out = new FileOutputStream(new File(
                                            FILE_NAME));
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
                                            handler.sendEmptyMessage(INSTALL_TOKEN);
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
        File appFile = new File(FILE_NAME);
        if (!appFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}