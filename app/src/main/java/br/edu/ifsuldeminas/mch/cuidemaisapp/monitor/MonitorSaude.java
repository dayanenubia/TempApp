package br.edu.ifsuldeminas.mch.cuidemaisapp.monitor;

import android.app.Service;

import br.edu.ifsuldeminas.mch.cuidemaisapp.external.CuideMaisAPI;

public class MonitorSaude extends APIMonitor {
    private static final int sleep1minute = 60000;

    private enum Limits {
        //DIAPER_HUMIDITY_PERCENT(18),
        ENVIRONMENT_TEMPERATUE_C (8);
        //ENVIRONMENT_HUMIDITY(60);

        private int limit;

        private  Limits(int limit){
            this.limit = limit;
        }

        public int getLimit() {
            return limit;
        }
    }

    public MonitorSaude(Service service, Class<?> activityClass){
        super(service, activityClass);
    }

    @Override
    protected void monitor() {
        while (shouldRun()) {
            String msg = "Atenção! Variação de temperatura!\n";
            //String diaperHumidityMsg = msg.concat("Umidade da frauda em %d%%");
            String environmentTemperatureMsg = msg.concat("Temperatura do Refrigerador em %dºC");
            //String environmentHumidityMsg = msg.concat("Umidade do ambiente em %d%%");

            CuideMaisAPI cuideMaisAPI = new CuideMaisAPI();

            //int diaperHumidityPercent = cuideMaisAPI.getDiaperHumidityPercent();
            //if (diaperHumidityPercent > Limits.DIAPER_HUMIDITY_PERCENT.getLimit())
                //createNotification(String.format(diaperHumidityMsg, diaperHumidityPercent));

            int environmentTemperature = (int) cuideMaisAPI.getEnvironmentTemperature();
            if (environmentTemperature > Limits.ENVIRONMENT_TEMPERATUE_C.getLimit())
                createNotification(String.format(environmentTemperatureMsg, environmentTemperature));

            //int environmentHumidity = (int) cuideMaisAPI.getEnvironmentHumidity();
            //if (environmentHumidity < Limits.ENVIRONMENT_HUMIDITY.getLimit())
                //createNotification(String.format(environmentHumidityMsg, environmentHumidity));

            sleepOneMinute();
        }
    }

    private void sleepOneMinute() {
        try {
            Thread.sleep(120000); /* milisgundos */
        } catch (InterruptedException e) {}
    }
}
