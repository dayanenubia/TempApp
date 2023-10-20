package br.edu.ifsuldeminas.mch.cuidemaisapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import br.edu.ifsuldeminas.mch.cuidemaisapp.MainActivity;
import br.edu.ifsuldeminas.mch.cuidemaisapp.R;
import br.edu.ifsuldeminas.mch.cuidemaisapp.monitor.APIMonitor;
import br.edu.ifsuldeminas.mch.cuidemaisapp.monitor.MonitorSaude;

public class ForegrowndService extends Service {
    private String TAG = "ForegrowndService";
    public static boolean isServiceRunning;
    public static boolean internalStop;
    private String CHANNEL_ID = "CUIDE_MAIS_NOTIFICATION_CHANNEL";
    private APIMonitor monitor;

    public ForegrowndService() {
//        TAG = tag;
//        CHANNEL_ID = channelId;
        isServiceRunning = false;
        internalStop = false;
        Log.d(TAG, "ForegrowndService constructor called");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        isServiceRunning = true;
        internalStop = false;
        Log.d(TAG, "ForegrowndService onCreate called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.cuide_mais_service_title))
                .setContentText(getString(R.string.cuide_mais_service_message))
                .setSmallIcon(R.drawable.logo_app)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_app))
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.purple_200))
                .build();

        monitor = new MonitorSaude(this, MainActivity.class);
        monitor.startMonitor();

        startForeground(1, notification);
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String appName = getString(R.string.app_name);
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    appName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        isServiceRunning = false;
        stopForeground(true);

        if (!internalStop) {
            // call MyReceiver which will restart this service via a worker
            Intent broadcastIntent = new Intent(this, CuideMaisReceiver.class);
            sendBroadcast(broadcastIntent);
        }

        internalStop = false;
        monitor.stopMonitor();
        super.onDestroy();
    }
}
