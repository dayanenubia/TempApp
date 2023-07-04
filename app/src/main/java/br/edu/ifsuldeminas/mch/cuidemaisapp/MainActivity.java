package br.edu.ifsuldeminas.mch.cuidemaisapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import br.edu.ifsuldeminas.mch.cuidemaisapp.databinding.ActivityMainBinding;
import br.edu.ifsuldeminas.mch.cuidemaisapp.service.CuideMaisWorker;

import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "CuideMaisMainActivity";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_content_main);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        NavigationUI.setupActionBarWithNavController(this, navController,
                appBarConfiguration);

        startServiceViaWorker();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla o menu para add items a actionbar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Trata os eventos de clique nos items da actionbar.
        // A actionbar trata o evento do botão voltar, desde que haja
        // um parent definido para a activity no AndroidManifest.xml
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            if (!isSettingCurrentFragment())
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_MainFragment_to_SettingsFragment);

            return true;
        }

//        if (itemId == R.id.action_notificar){
//            // Create an explicit intent for an Activity in your app
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CUIDE_MAIS_NOTIFICATION_CHANNEL")
//                    .setSmallIcon(R.drawable.logo)
//                    .setContentTitle(getString(R.string.cuide_mais_service_title) + " - Alerta")
//                    .setContentText("Inconformidade identificada - Atenção do cuidador requerida!")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            // notificationId is a unique int for each notification that you must define
//            int notificationId = 1234;
//            notificationManager.notify(notificationId, builder.build());
//        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isSettingCurrentFragment() {
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        return currentFragment != null &&
                currentFragment instanceof SettingsFragment;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, appBarConfiguration) ||
                super.onSupportNavigateUp();
    }

    public void startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called");
        String UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
        WorkManager workManager = WorkManager.getInstance(this);

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        CuideMaisWorker.class,
                        16,
                        TimeUnit.MINUTES)
                        .build();

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,
                request);
    }
}