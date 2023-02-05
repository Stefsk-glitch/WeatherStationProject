import java.time.LocalDateTime;
import java.util.ArrayList;

public class GoodWeatherDays_Stef {
    public static int goodWeatherDays(Period period) {
        // Het is goed weer als:
        // Buiten temperatuur > 10 en < 25 (graden)
        // Gevallen neerslag < 1 (Als er geen regen is gevallen of bijna geen regen) (mm) per uur
        // windsnelheid < 13 (km per uur)

        int goodWeatherDays = 0;

        ArrayList<Measurement> measurements = period.getMeasurements();
        ArrayList<Double> tempValues = new ArrayList<>();
        ArrayList<Double> rainFallValues = new ArrayList<>();
        ArrayList<Double> windSpeedValues = new ArrayList<>();

        LocalDateTime rememberDate = measurements.get(0).getDateStamp();

        for (Measurement measurement : measurements) {
            // Is it another day ?
            if (measurement.getDateStamp().getDayOfYear() != rememberDate.getDayOfYear()) {
                double minTemp = 0;
                double maxTemp = 0;
                double maxRainFall = 0;
                double maxWindSpeed = 0;

                if (tempValues.size() > 0) {
                    minTemp = Measurement.lowestValue(tempValues);
                    maxTemp = Measurement.highestValue(tempValues);
                }

                if (rainFallValues.size() > 0) {
                    maxRainFall = Measurement.highestValue(rainFallValues);
                }

                if (windSpeedValues.size() > 0) {
                    maxWindSpeed = Measurement.highestValue(windSpeedValues);
                }

                tempValues.clear();
                rainFallValues.clear();
                windSpeedValues.clear();

                if (minTemp >= 10 && maxTemp <= 25) {
                    if (maxWindSpeed <= 13) {
                        if (maxRainFall <= 1) {
                            goodWeatherDays++;
                        }
                    }
                }
            } else {
                tempValues.add(measurement.outsideTemperature());
                rainFallValues.add(measurement.rainMeter());
                windSpeedValues.add(measurement.windSpeed());
            }
            rememberDate = measurement.getDateStamp();
        }
        return goodWeatherDays;
    }
}