package br.edu.ifsuldeminas.mch.cuidemaisapp.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CuideMaisWorker extends Worker {
    private final Context context;
    private String TAG = "CuideMaisWorker";

    public CuideMaisWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork called for: " + this.getId());
        Log.d(TAG, "Cuide Mais Service Running: " + ForegrowndService.isServiceRunning);
        if (!ForegrowndService.isServiceRunning) {
            Log.d(TAG, "starting service from doWork");
            Intent intent = new Intent(this.context, ForegrowndService.class);
            ContextCompat.startForegroundService(context, intent);
        }
        return Result.success();
    }

    @Override
    public void onStopped() {
        Log.d(TAG, "onStopped called for: " + this.getId());
        super.onStopped();
    }
}
