import java.util.ArrayList;
import java.util.Collections;

public class MaxRain_Quinten {
    public static double maxRegenVal(Period period) {

        ArrayList<Measurement> measurements = period.getMeasurements();
        double sum = 0.0;
        boolean isRaining = false;
        ArrayList<Double> rainFallAanEenGesloten = new ArrayList<>();

        for (Measurement measurement : measurements) {

            if (measurement.rainMeter() > 0.0) {
                sum += measurement.rainMeter();
                isRaining = true;
            }

            if (measurement.rainMeter() == 0.0 && isRaining == true) {
                isRaining = false;
                rainFallAanEenGesloten.add(period.round(sum, 1));
                sum = 0.0;
            }
        }

        Collections.sort(rainFallAanEenGesloten);

        if (rainFallAanEenGesloten.size() != 0) {
            return rainFallAanEenGesloten.get(rainFallAanEenGesloten.size() - 1);
        }
        return -1.0;
    }
}
