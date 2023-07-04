package br.edu.ifsuldeminas.mch.cuidemaisapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class CuideMaisReceiver extends BroadcastReceiver {
    private String TAG = "CuideMaisReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "CuideMaisReceiver onReceive called");

        // We are starting MyService via a worker and not directly because since Android 7
        // (but officially since Lollipop!), any process called by a BroadcastReceiver
        // (only manifest-declared receiver) is run at low priority and hence eventually
        // killed by Android.
        WorkManager workManager = WorkManager.getInstance(context);
        OneTimeWorkRequest startServiceRequest = new OneTimeWorkRequest.Builder(CuideMaisWorker.class)
                .build();
        workManager.enqueue(startServiceRequest);
    }
}
