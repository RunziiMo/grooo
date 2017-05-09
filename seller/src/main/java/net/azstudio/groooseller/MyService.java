package net.azstudio.groooseller;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;


import com.runzii.lib.constant.MyActions;

import net.azstudio.groooseller.ui.activities.MainActivity;
import net.azstudio.groooseller.utils.SmallTools;

public class MyService extends Service {

    private MediaPlayer mediaPlayer;
    private MyReceiver myReceiver;
    private boolean isPause = false;
    private Uri path = SmallTools.resourceIdToUri(R.raw.beep);

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this);
        Notification notification = builder
                .setContentTitle("咕噜卖家")
                .setContentText("营业中，准备接受新订单")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.login_logo)
                .build();
        startForeground(0x1234, notification);

        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.start());

        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyActions.ACTION_PLAY);
        filter.addAction(MyActions.ACTION_PAUSE);
        registerReceiver(myReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (myReceiver != null)
            unregisterReceiver(myReceiver);
    }

    private void play() {
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        } else {
            try {
                mediaPlayer.reset();// 把各项参数恢复到初始状态
                mediaPlayer.setDataSource(this, path);
                mediaPlayer.prepare(); // 进行缓冲
                mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());// 注册一个监听器
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MyActions.ACTION_PLAY.equals(intent.getAction()))
                play();
            else if (MyActions.ACTION_PAUSE.equals(intent.getAction()))
                pause();
        }
    }
}
