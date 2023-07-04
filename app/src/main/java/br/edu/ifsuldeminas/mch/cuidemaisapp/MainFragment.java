package br.edu.ifsuldeminas.mch.cuidemaisapp;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import br.edu.ifsuldeminas.mch.cuidemaisapp.databinding.FragmentMainBinding;
import br.edu.ifsuldeminas.mch.cuidemaisapp.external.CuideMaisAPI;
import br.edu.ifsuldeminas.mch.cuidemaisapp.service.ForegrowndService;

public class MainFragment extends Fragment {

    private final static String TAG = "CuideMaisMainFragment";
    private FragmentMainBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.switchMonitoringId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.switchMonitoringId.setChecked(ForegrowndService.isServiceRunning);
                Toast.makeText(getContext(), R.string.switch_monitoring_set_checked_message,
                        Toast.LENGTH_LONG).show();
            }
        });

        binding.seekBarEnvironmentTemperatureId.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        binding.switchMonitoringId.setChecked(ForegrowndService.isServiceRunning);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        CuideMaisAPI cuideMaisAPI = new CuideMaisAPI();
        //int diaperHumidityPercent = (int )cuideMaisAPI.getDiaperHumidityPercent();

        //binding.progressBarHumidityId.setProgress(diaperHumidityPercent);
        //binding.textViewDiaperHumidityValueId.setText(String.format("%d%%", diaperHumidityPercent));

        double environmentTemperature  = cuideMaisAPI.getEnvironmentTemperature();
        if (Double.isFinite(environmentTemperature)) {
            binding.textViewEnvironmentTemperatureValueId.setText(String.format("%.1fºC", environmentTemperature));
            binding.seekBarEnvironmentTemperatureId.setProgress((int) (environmentTemperature));
        } else {
            // Valor inválido ou indeterminado, definir um valor padrão ou exibir uma mensagem de temperatura indisponível
            binding.textViewEnvironmentTemperatureValueId.setText("Temperatura indisponível");
            binding.seekBarEnvironmentTemperatureId.setProgress(0);
        }

        //int environmentHumidity  = (int) cuideMaisAPI.getEnvironmentHumidity();
        //binding.progressBarEnvironmentHumidityValueId.setProgress(environmentHumidity);
        //binding.textViewEnvironmentHumidityValueId.setText(String.format("%d%%", environmentHumidity));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}