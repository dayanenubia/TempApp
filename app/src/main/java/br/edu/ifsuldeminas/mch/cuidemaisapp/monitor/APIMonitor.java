package br.edu.ifsuldeminas.mch.cuidemaisapp.monitor;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

import br.edu.ifsuldeminas.mch.cuidemaisapp.R;

public abstract class APIMonitor implements Runnable{
    private Service service;
    private Class<?> activityClass;
    private Thread monitorThread;
    private boolean shouldRun;

    public APIMonitor(Service service, Class<?> activityClass) {
        this.service = service;
        this.activityClass = activityClass;
        shouldRun = true;
    }

    protected Service getService(){
        return service;
    }

    public void startMonitor(){
        monitorThread = new Thread(this);
        monitorThread.start();
    }

    public void stopMonitor(){
        shouldRun = false;
    }

    public boolean shouldRun(){
        return shouldRun;
    }

    @Override
    public void run(){
        monitor();
    };

    protected void createNotification(String message){
        // Create an explicit intent for an Activity in your app
        Context context = service.getBaseContext();
        Intent intent = new Intent(context, this.activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(service.getBaseContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CUIDE_MAIS_NOTIFICATION_CHANNEL")
                .setSmallIcon(R.drawable.logo_app)
                .setLargeIcon(BitmapFactory.decodeResource( context.getResources(), R.drawable.logo_app))
                .setContentTitle(context.getString(R.string.cuide_mais_service_title) + " - Alerta")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // NotificationId is a unique int for each notification that you must define
        int notificationId = 2000;
        notificationId += new Random().nextInt(5000);
        notificationManager.notify(notificationId, builder.build());
    }

    protected abstract void monitor();
}
