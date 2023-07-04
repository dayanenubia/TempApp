package br.edu.ifsuldeminas.mch.cuidemaisapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import br.edu.ifsuldeminas.mch.cuidemaisapp.databinding.FragmentSettingsBinding;
import br.edu.ifsuldeminas.mch.cuidemaisapp.service.ForegrowndService;

public class SettingsFragment extends Fragment {

    private final static String TAG = "CuideMaisSettingsFragment";
    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonsEnable(ForegrowndService.isServiceRunning);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonStartServiceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
                setButtonsEnable(true);
            }
        });

        binding.buttonStopServiceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService();
                setButtonsEnable(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void startService() {
        Log.d(TAG, "startService called");
        if (!ForegrowndService.isServiceRunning) {
            Log.d(TAG, "Erro 1 - startService called");
            Intent serviceIntent = new Intent(getContext(), ForegrowndService.class);
            ContextCompat.startForegroundService(getContext(), serviceIntent);
            Log.d(TAG, "Erro 2 - startService called");
        }
    }

    private void stopService() {
        Log.d(TAG, "stopService called");
        if (ForegrowndService.isServiceRunning) {
            ForegrowndService.internalStop = true;
            Intent serviceIntent = new Intent(getContext(), ForegrowndService.class);
            getActivity().stopService(serviceIntent);
        }
    }

    private void setButtonsEnable(boolean isServiceRunning) {
        binding.buttonStopServiceId.setEnabled(isServiceRunning);
        binding.buttonStartServiceId.setEnabled(!isServiceRunning);
    }
}