import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Individuele opdracht Mert Ozdal
 * Uitrekenen van de hoogste verschil in temperatuur gegeven periode.
 */
public class TempDifference_Mert {
    private static double highestTemp = -100.0;
    private static double lowestTemp = 100.0;
    private static double biggestDifference = 0.0;
    private static double currentDayDifference = 0.0;
    private static LocalDate dayOfBiggestDifference;

    Period p = new Period();

    public static LocalDate getDayOfBiggestDifference() {
        return dayOfBiggestDifference;
    }

    public double biggestDifference(Period period) {
        ArrayList<Measurement> measurements = period.getMeasurements();
        boolean isFirst = true;
        LocalDate rememberDate = null;

        if (!(measurements.size() == 0)) {
            for (Measurement measurement : measurements) {
                if (measurement.outsideTemperature() < 50) {
                    if (isFirst) {
                        rememberDate = measurement.getDateStamp().toLocalDate();
                        isFirst = false;
                    }
                    //Check if the current measurement date is the same
                    if (measurement.getDateStamp().toLocalDate().equals(rememberDate)) {
                        if (measurement.outsideTemperature() > highestTemp) {
                            highestTemp = measurement.outsideTemperature();
                        }
                        if (measurement.outsideTemperature() < lowestTemp) {
                            lowestTemp = measurement.outsideTemperature();
                        }
                    } else {
                        currentDayDifference = highestTemp - lowestTemp;
                        if (currentDayDifference > biggestDifference) {
                            biggestDifference = currentDayDifference;
                            dayOfBiggestDifference = measurement.getDateStamp().toLocalDate();
                        }
                        highestTemp = -100.0;
                        lowestTemp = 100.0;
                        rememberDate = measurement.getDateStamp().toLocalDate();
                        isFirst = true;
                    }
                }
                //check if the measurement is the first of the day.

            }
            return p.round(biggestDifference, 1);
        }
        return 999;
    }


}
